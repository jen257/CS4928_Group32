#!/usr/bin/env python3
"""Measure AR, LT, LOC, assertion and test counts for a set of test classes.

This reproduces the TsDetect-based RQ2 measurement pipeline used for the
baseline. Run it on the untouched originals to reproduce the baseline, then on
the refactored copies to get the "after" numbers. Compilation, JUnit execution
and TSDetect all use the same pinned JDK 8 and JARs as the baseline.

Usage:
    python verify.py --root validated_samples --out results/before.csv
    python verify.py --root refactored        --out results/after.csv --before results/before.csv
"""
import argparse
import collections
import csv
import hashlib
import json
import os
import re
import shutil
import subprocess
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parent
PKG = ROOT.parent
MANIFEST = PKG / "validated_manifest.json"
DETECTOR = PKG / "evidence" / "TestSmellDetector.jar"
JAR_ROOT = PKG / "validation" / "runtime" / "jars"
CONSOLE = JAR_ROOT / "junit-platform-console-standalone-1.9.3.jar"

_COMMENT_OR_LITERAL = re.compile(
    r'"""[\s\S]*?"""|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'|//[^\r\n]*|/\*[\s\S]*?\*/'
)
_ASSERT = re.compile(r"\b(?:assert[A-Z]\w*|fail)\s*\(")
_PACKAGE = re.compile(r"^\s*package\s+([\w.]+)\s*;", re.M)


def strip_comments(code):
    def repl(match):
        value = match.group(0)
        if value.startswith("//") or value.startswith("/*"):
            return "".join("\n" if ch == "\n" else " " for ch in value)
        return value
    return _COMMENT_OR_LITERAL.sub(repl, code)


def source(path):
    return path.read_bytes().decode("utf-8-sig")


def code_loc(path):
    """Non-blank, comment-excluded line count (the baseline LOC rule)."""
    stripped = strip_comments(source(path))
    return sum(bool(line.strip()) for line in stripped.splitlines())


def count_assertions(path):
    code = strip_comments(source(path))
    total = with_message = 0
    for match in _ASSERT.finditer(code):
        total += 1
        depth, index = 0, match.end() - 1
        while index < len(code):
            if code[index] == "(":
                depth += 1
            elif code[index] == ")":
                depth -= 1
                if depth == 0:
                    break
            index += 1
        if '"' in code[match.end():index]:
            with_message += 1
    return total, with_message


def fqn(path):
    match = _PACKAGE.search(source(path))
    return f"{match.group(1)}.{path.stem}" if match else path.stem


def sha1(path):
    return hashlib.sha1(path.read_bytes()).hexdigest()


def run(argv, work, timeout=180):
    env = os.environ.copy()
    env["TEMP"] = env["TMP"] = str(work / "tmp")
    try:
        proc = subprocess.run([str(a) for a in argv], cwd=work, env=env,
                              stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                              timeout=timeout)
        return proc.returncode, proc.stdout, proc.stderr
    except subprocess.TimeoutExpired:
        return None, b"", b"timeout"


def find_jdk():
    homes = sorted((PKG / "validation" / "runtime" / "jdk8").glob("*/bin/java.exe"))
    if not homes:
        sys.exit("portable JDK8 not found under validation/runtime/jdk8")
    return homes[0]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", required=True, type=Path,
                        help="directory with <sample>_<Class>/test/... subfolders")
    parser.add_argument("--out", required=True, type=Path, help="output CSV path")
    parser.add_argument("--before", type=Path,
                        help="optional baseline CSV to compute reduction percentages")
    parser.add_argument("--jdk", type=Path, help="JDK8 home (default: pinned portable JDK)")
    args = parser.parse_args()
    args.root = args.root.resolve()
    args.out = args.out.resolve()

    java = args.jdk / "bin" / "java.exe" if args.jdk else find_jdk()
    javac = java.with_name("javac.exe")
    if not CONSOLE.exists():
        sys.exit(f"missing {CONSOLE}")
    if not DETECTOR.exists():
        sys.exit(f"missing {DETECTOR}")
    manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))

    version = subprocess.run([str(java), "-version"], capture_output=True)
    print("JDK:", version.stderr.decode("utf-8", "replace").strip().splitlines()[0])

    rows = []
    for entry in manifest:
        sample = entry["sample"]
        folder = (PKG / entry["test_file"]).relative_to(PKG / "validated_samples").parts[0]
        test_dir = args.root / folder / "test"
        production = PKG / entry["production_file"]

        if not test_dir.is_dir():
            print(f"{sample}: test dir missing -> {test_dir}", file=sys.stderr)
            continue
        if sha1(production) != entry["production_sha1"]:
            sys.exit(f"{sample}: production source changed, refusing to run")

        test_files = sorted(p for p in test_dir.rglob("*.java"))
        test_classes = [p for p in test_files if "@Test" in strip_comments(source(p))]
        if not test_classes:
            sys.exit(f"{sample}: no @Test class under {test_dir}")

        total_assertions = with_message = 0
        loc = 0
        for p in test_files:
            a, w = count_assertions(p)
            total_assertions += a
            with_message += w
            loc += code_loc(p)

        work = ROOT / "_verify_work" / sample
        shutil.rmtree(work, ignore_errors=True)
        (work / "tmp").mkdir(parents=True)
        classes = work / "classes"
        classes.mkdir(parents=True)

        jars = [JAR_ROOT / name for name in entry["active_dependency_jars"]]
        for jar in jars:
            if not jar.exists():
                sys.exit(f"missing jar {jar}")
        cp = os.pathsep.join(map(str, jars))

        rc, _, err = run(
            [javac, "-J-Dfile.encoding=UTF-8", "-encoding", "UTF-8", "-proc:none",
             "-source", "8", "-target", "8", "-cp", cp, "-d", classes,
             production, *test_files], work)
        if rc != 0:
            print(f"{sample}: COMPILE FAILED")
            print(err.decode("utf-8", "replace"))
            rows.append({"sample": sample, "test_class": entry["test_fqn"],
                         "compile": "FAIL", "tests": 0, "failures": 0, "errors": 0,
                         "skipped": 0, "all_tests_passed": False, "AR": 0, "LT": 0,
                         "LOC": loc, "assertions": total_assertions,
                         "assertions_with_message": with_message})
            continue

        runtime_cp = os.pathsep.join([str(classes)] + [str(j) for j in jars])
        select = []
        for p in test_classes:
            select += ["--select-class", fqn(p)]
        report_dir = work / "junit-reports"
        report_dir.mkdir(parents=True)
        rc, _, _ = run(
            [java, "-Dfile.encoding=UTF-8", "-Duser.home=" + str(work),
             "-Djava.io.tmpdir=" + str(work / "tmp"), "-jar", CONSOLE,
             "--class-path", runtime_cp, *select,
             "--disable-ansi-colors", "--details=summary",
             "--fail-if-no-tests", "--reports-dir", report_dir], work)
        counts = collections.Counter()
        for xml in report_dir.glob("TEST-*.xml"):
            suite = ET.parse(xml).getroot()
            for key in ["tests", "failures", "errors", "skipped"]:
                counts[key] += int(suite.attrib.get(key, "0"))
        all_passed = (rc == 0 and counts["tests"] > 0
                      and not any(counts[key] for key in ["failures", "errors", "skipped"]))

        ar = lt = 0
        for p in test_classes:
            detect = work / "tsdetect" / fqn(p).replace(".", "_")
            detect.mkdir(parents=True)
            (detect / "tmp").mkdir()
            input_csv = detect / "input.csv"
            with input_csv.open("w", encoding="utf-8", newline="") as stream:
                csv.writer(stream).writerow([fqn(p), str(p), str(production)])
            run([java, "-Dfile.encoding=UTF-8", "-jar", DETECTOR, input_csv], detect)
            for produced in detect.glob("*.csv"):
                if produced.name == "input.csv":
                    continue
                with produced.open(encoding="utf-8-sig", newline="") as stream:
                    for record in csv.DictReader(stream):
                        if "Assertion Roulette" in record:
                            ar += int(record["Assertion Roulette"])
                            lt += int(record["Lazy Test"])

        rows.append({"sample": sample, "test_class": entry["test_fqn"],
                     "compile": "PASS", "tests": counts["tests"],
                     "failures": counts["failures"], "errors": counts["errors"],
                     "skipped": counts["skipped"], "all_tests_passed": all_passed,
                     "AR": ar, "LT": lt, "LOC": loc,
                     "assertions": total_assertions,
                     "assertions_with_message": with_message})
        print(f"{sample} compile PASS tests {counts['tests']} passed {all_passed} "
              f"AR {ar} LT {lt} LOC {loc} assertions {total_assertions}")

    fieldnames = ["sample", "test_class", "compile", "tests", "failures", "errors",
                  "skipped", "all_tests_passed", "AR", "LT", "LOC",
                  "assertions", "assertions_with_message"]
    args.out.parent.mkdir(parents=True, exist_ok=True)
    with args.out.open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)

    def total(key):
        return sum(r[key] for r in rows)

    print("\nSUMMARY")
    print(f"samples {len(rows)}  AR {total('AR')}  LT {total('LT')}  "
          f"LOC {total('LOC')}  tests {total('tests')}  assertions {total('assertions')}")

    if args.before and args.before.exists():
        with args.before.open(encoding="utf-8-sig", newline="") as stream:
            previous = {r["sample"]: r for r in csv.DictReader(stream)}
        ar_b = sum(int(previous[s]["AR"]) for s in previous)
        lt_b = sum(int(previous[s]["LT"]) for s in previous)
        loc_b = sum(int(previous[s]["LOC"]) for s in previous)
        ar_a, lt_a, loc_a = total("AR"), total("LT"), total("LOC")

        def pct(before, after):
            return round((before - after) / before * 100, 2) if before else None

        print("\nREDUCTION vs baseline")
        print(f"AR  {ar_b} -> {ar_a}  ({pct(ar_b, ar_a)}%)")
        print(f"LT  {lt_b} -> {lt_a}  ({pct(lt_b, lt_a)}%)")
        print(f"LOC {loc_b} -> {loc_a}  ({loc_a - loc_b:+d})")


if __name__ == "__main__":
    main()
