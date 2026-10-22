#!/usr/bin/env python3
"""Fill master_prompt.md for one sample with its real sources and baseline numbers.

Usage:
    python fill_prompt.py R01
    python fill_prompt.py            (all samples)

Output: prompts/<SAMPLE>_prompt_filled.md  (ready to copy-paste to the AI)
"""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent
PKG = ROOT.parent
MANIFEST = PKG / "validated_manifest.json"
TEMPLATE = ROOT / "master_prompt.md"
OUT = ROOT / "prompts"

_PLACEHOLDER = re.compile(r"\{\{(\w+)\}\}")


def main(argv):
    template = TEMPLATE.read_text(encoding="utf-8")
    manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
    wanted = set(argv[1:]) or None
    OUT.mkdir(parents=True, exist_ok=True)

    written = []
    for entry in manifest:
        sample = entry["sample"]
        if wanted and sample not in wanted:
            continue

        test_file = PKG / entry["test_file"]
        production_file = PKG / entry["production_file"]
        values = {
            "SAMPLE": sample,
            "AR": str(entry["AR_local"]),
            "LT": str(entry["LT_local"]),
            "LOC": str(entry["test_code_LOC"]),
            "TEST_SOURCE": test_file.read_bytes().decode("utf-8-sig").strip(),
            "PRODUCTION_SOURCE": production_file.read_bytes().decode("utf-8-sig").strip(),
        }
        filled = _PLACEHOLDER.sub(lambda m: values.get(m.group(1), m.group(0)), template)
        leftover = _PLACEHOLDER.findall(filled)
        if leftover:
            print(f"{sample}: unresolved {leftover}", file=sys.stderr)
            continue
        target = OUT / f"{sample}_prompt_filled.md"
        target.write_text(filled + "\n", encoding="utf-8")
        written.append((sample, len(filled)))

    print(json.dumps(
        {"written": [{"sample": s, "chars": n} for s, n in written],
         "directory": str(OUT.relative_to(ROOT))},
        ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main(sys.argv)
