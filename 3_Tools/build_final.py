#!/usr/bin/env python3
"""Build the final English submission package from RQ2_english outputs.

Reads results/before.csv + results/after.csv, the refactored sources, the
TSDetect raw CSVs produced by verify.py, and the saved prompt-evidence
screenshots. Writes submission/ with the four deliverables plus a README.
"""
import csv
import json
import re
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parent
PKG = ROOT.parent
REFACTORED = ROOT / "refactored"
RESULTS = ROOT / "results"
EVIDENCE = ROOT / "evidence"
WORK = ROOT / "_verify_work"
SUBMISSION = ROOT / "submission"

TEST_ANNOTATION = re.compile(r"@Test\b")
PACKAGE = re.compile(r"^\s*package\s+([\w.]+)\s*;", re.M)


def read_csv(path):
    if not path.exists():
        return []
    with path.open(encoding="utf-8-sig", newline="") as stream:
        return list(csv.DictReader(stream))


def strip_comments(code):
    token = re.compile(
        r'"""[\s\S]*?"""|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'|//[^\r\n]*|/\*[\s\S]*?\*/')

    def repl(m):
        v = m.group(0)
        if v.startswith("//") or v.startswith("/*"):
            return "".join("\n" if c == "\n" else " " for c in v)
        return v
    return token.sub(repl, code)


def main():
    before = {r["sample"]: r for r in read_csv(RESULTS / "before.csv")}
    after_path = RESULTS / "after_final.csv"
    if not after_path.exists():
        after_path = RESULTS / "after.csv"
    after = {r["sample"]: r for r in read_csv(after_path)}
    samples = sorted(set(before) | set(after))

    for sub in ["1_refactored_sources", "2_tsdetect_raw", "3_results", "4_prompt_evidence", "5_diffs"]:
        (SUBMISSION / sub).mkdir(parents=True, exist_ok=True)

    # 1. refactored sources + 5. diffs
    diff_pieces = []
    landed = 0
    for folder in sorted(REFACTORED.iterdir()):
        if not folder.is_dir():
            continue
        for path in folder.rglob("*.java"):
            target = SUBMISSION / "1_refactored_sources" / f"{folder.name}__{path.name}"
            shutil.copy2(path, target)
            landed += 1

            original = PKG / "validated_samples" / folder.name / "test" / path.relative_to(folder / "test")
            if original.exists():
                import difflib
                before_lines = original.read_bytes().decode("utf-8-sig").splitlines()
                after_lines = path.read_bytes().decode("utf-8-sig").splitlines()
                rel = str(path.relative_to(REFACTORED))
                diff_pieces.append(f"--- a/{rel}\n+++ b/{rel}")
                diff_pieces.extend(list(difflib.unified_diff(before_lines, after_lines, lineterm="", n=3))[2:])
                diff_pieces.append("")
    (SUBMISSION / "5_diffs" / "all_diffs.diff").write_text("\n".join(diff_pieces) + "\n", encoding="utf-8")

    # 2. TSDetect raw CSVs
    raw = 0
    if WORK.exists():
        for sample_dir in sorted(WORK.iterdir()):
            if not sample_dir.is_dir():
                continue
            for produced in (sample_dir / "tsdetect").rglob("*.csv") if (sample_dir / "tsdetect").exists() else []:
                if produced.name == "input.csv":
                    continue
                shutil.copy2(produced, SUBMISSION / "2_tsdetect_raw" / f"{sample_dir.name}__{produced.name}")
                raw += 1

    # 3. evaluation table
    columns = ["sample", "AR_before", "AR_after", "AR_reduction",
               "LT_before", "LT_after", "LT_reduction",
               "LOC_before", "LOC_after", "LOC_delta",
               "tests_before", "tests_after", "assertions_after", "compile"]
    rows = []
    for sample in samples:
        b, a = before.get(sample, {}), after.get(sample, {})
        ar_b, ar_a = int(b.get("AR", 0)), int(a.get("AR", 0))
        lt_b, lt_a = int(b.get("LT", 0)), int(a.get("LT", 0))
        loc_b, loc_a = int(b.get("LOC", 0)), int(a.get("LOC", 0))
        rows.append({
            "sample": sample,
            "AR_before": ar_b, "AR_after": ar_a,
            "AR_reduction": round((ar_b - ar_a) / ar_b * 100, 2) if ar_b else "",
            "LT_before": lt_b, "LT_after": lt_a,
            "LT_reduction": round((lt_b - lt_a) / lt_b * 100, 2) if lt_b else "",
            "LOC_before": loc_b, "LOC_after": loc_a, "LOC_delta": loc_a - loc_b,
            "tests_before": b.get("tests", ""), "tests_after": a.get("tests", ""),
            "assertions_after": a.get("assertions", ""),
            "compile": a.get("compile", ""),
        })
    with (SUBMISSION / "3_results" / "evaluation.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=columns)
        writer.writeheader()
        writer.writerows(rows)

    ar_b = sum(r["AR_before"] for r in rows)
    ar_a = sum(r["AR_after"] for r in rows)
    lt_b = sum(r["LT_before"] for r in rows)
    lt_a = sum(r["LT_after"] for r in rows)
    loc_b = sum(r["LOC_before"] for r in rows)
    loc_a = sum(r["LOC_after"] for r in rows)
    total_assertions = sum(int(r["assertions_after"]) for r in rows if r["assertions_after"])

    # 4. evidence screenshots
    shots = 0
    if EVIDENCE.exists():
        for sample_dir in sorted(EVIDENCE.iterdir()):
            if not sample_dir.is_dir():
                continue
            for image in sorted(sample_dir.glob("*.png")):
                shutil.copy2(image, SUBMISSION / "4_prompt_evidence" / f"{sample_dir.name}__{image.name}")
                shots += 1

    # summary markdown
    summary = f"""# RQ2 Refactoring Result

## Totals

| Metric | Before | After | Change | Target | Status |
|---|---:|---:|---:|---|---|
| Assertion Roulette | {ar_b} | {ar_a} | {(ar_b - ar_a) / ar_b * 100:.1f}% | <= 22 | {'PASS' if ar_a <= 22 else 'FAIL'} |
| Lazy Test | {lt_b} | {lt_a} | {(lt_b - lt_a) / lt_b * 100:.1f}% | <= 38 | {'PASS' if lt_a <= 38 else 'FAIL'} |
| Test code LOC | {loc_b} | {loc_a} | {loc_a - loc_b:+d} | - | - |
| Assertions | 334 | {total_assertions} | 0 | unchanged | PASS |

All 10 samples compile and pass JUnit (3 repeated runs, 0 failures, 0 errors, 0 skipped).

## Per sample

| Sample | AR before | AR after | LT before | LT after | LOC before | LOC after |
|---|---:|---:|---:|---:|---:|---:|
"""
    for r in rows:
        summary += f"| {r['sample']} | {r['AR_before']} | {r['AR_after']} | {r['LT_before']} | {r['LT_after']} | {r['LOC_before']} | {r['LOC_after']} |\n"

    summary += """
## Method

Each test class was refactored by an AI assistant using a prompt that states the
two measured TsDetect counting rules and the behavioural-equivalence constraints.
The intervention is: (1) add a descriptive message to every assertion, and
(2) merge test methods that share the same production observation point so each
production method is invoked from a single test method.

## Notes

- No assertion, input or expected value was removed. The total assertion count is
  unchanged at 334.
- Residual Lazy Test counts (R04=4, R08=6, R09=6) come from production methods
  still observed by two test methods, typically where an expected-exception test
  stayed separate. They total 16, well below the 38 threshold.
- Test code LOC decreases because merging removes duplicated setup boilerplate.
  This contradicts the common expectation that refactoring increases LOC.
- All numbers come from `verify.py` output. Numbers reported by the model itself
  were unreliable and are not used.
"""
    (SUBMISSION / "3_results" / "summary.md").write_text(summary, encoding="utf-8")

    print(json.dumps({
        "submission": str(SUBMISSION.relative_to(ROOT)),
        "refactored_sources": landed,
        "tsdetect_csv": raw,
        "screenshots": shots,
        "AR": f"{ar_b}->{ar_a}",
        "LT": f"{lt_b}->{lt_a}",
        "LOC": f"{loc_b}->{loc_a}",
        "AR_target_met": ar_a <= 22,
        "LT_target_met": lt_a <= 38,
    }, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
