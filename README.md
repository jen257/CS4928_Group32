# CS4928_Group32

RQ1: What is the prevalence of "Assertion Roulette" (AR) and "Lazy Test" (LT) smells in GPT-3.5 generated unit tests versus EvoSuite-generated tests for the Defects4J benchmark, as detected by TsDetect?

RQ2: How does applying an AI-assisted refactoring pattern (extracting explicit assertions and modularizing test methods) resolve AR and LT in a bounded sample, and what is the trade-off in test lines of code (LOC)?

Bounded target / Sample IDs: Dataset: Benchmark-1 (Defects4J only). Generators: GPT-3.5-Turbo (Zero-shot) vs. EvoSuite. Target Smells: Assertion Roulette (AR) and Lazy Test (LT) via TsDetect only.

Method / Decision rule: 1. Reproduction: Run the provided TSDetect pipeline script on the Defects4J JSONL datasets. Compute prevalence percentages for AR and LT. 2. Refactoring (Decision): Select 5-10 test classes heavily flagged with AR and LT. Apply an AI-assisted refactoring pattern. 3. Metric: A successful intervention is defined as a reduction of AR and LT by >80% in the sample without causing compilation failures.

Expected result: Consistent with the paper, we expect both GPT-3.5 and EvoSuite tests to show high prevalence of AR and LT. The refactoring intervention should eliminate these structural smells but may increase overall Lines of Code (LOC).

What would count as non-reproduction: If our local TsDetect run reports a prevalence deviation of more than 15 percentage points for AR or LT on the Defects4J benchmark compared to the paper's reported results in Appendix A.

---

## Repository Map & Evidence Trail

- **Project Pack:** P31 — *Test-Smell Diffusion in LLM-Generated Java Unit Tests* (Group G32)
- **External Paper / Dataset:** [DOI 10.1145/3838597](https://doi.org/10.1145/3838597) · [Zenodo Record 18641786](https://zenodo.org/records/18641786)

| Stage | Milestone / Focus | Primary Evidence Locations on `main` |
|---|---|---|
| **M1 (Week 5)** | Preregistered RQs & Decision Rule | `README.md` (top section), Tag `M1_PLAN` |
| **M2 (Week 7)** | RQ1 Bounded Reproduction (Initial & Java 17 Re-run) | Initial run: `CS4298_Data_Verification.xlsx`, `CS4928_Data_Verification.md`, `TSDetect result/`, `result log/`, `Python script/`<br>Java 17 re-run & audit: `Benchmark1_Java17_Results.xlsx`, `RQ1_Defects4J_AR_LT.csv`, `paper_Table17a_Java17_RQ1_*`, `paper_alignment_java17_*`, `D_R01_R10_java17_crosscheck*`, `independent_export_verification*`, `workbook_formula_check.ndjson`, `zero_method_review.csv` |
| **Event A (Week 8)** | Transfer to Held-Out Mistral 7B | `Benchmark1_TSDetect_All_Results*`, `prevalence_21_smells.csv`, `prevalence_by_group.csv`, `prevalence_by_prompt.csv`, `summary.json`, `error_log.csv` |
| **M4 (Week 9)** | RQ2 AI-Assisted Refactoring (R01–R10) | `1_Final_Submission/` (`1_refactored_sources/`, `2_tsdetect_raw/`, `3_results/`, `4_prompt_evidence/`, `5_diffs/`), `2_Prompts/`, `3_Tools/`, `4_Original_Samples/`, `5_Data/`, `candidates/`, `jars/` |
| **Event B (Week 10)** | Production API Evolution (`TokenQueue.peek`) | `EventB/` (Original vs. Refactored code, diffs, compile & JUnit logs), `Event B_Test_Maintainability_Report.md` |

---

## Summary of Key Results

- **M2 (RQ1 Reproduction — Initial & Java 17 Re-run):**
  - *Initial M2 run:* Reproduced the EvoSuite baseline (AR 38.24%, LT 76.94% vs. paper 38.24% / 82.18%) but recorded non-reproduction on GPT-3.5 (AR 9.39%, LT 0.00% vs. paper 46.94% / 48.67%).
  - *Corrected Java 17 re-run:* Re-executing under JDK 17 resolved the environment/parsing discrepancy and aligned with the GPT-3.5 baseline; both the initial audit and the corrected Java 17 outputs are preserved on `main` for full traceability.
- **Event A (Mistral 7B Transfer):** Local TsDetect scan on the held-out Mistral 7B dataset yielded **54.54% AR** and **56.04% LT**, confirming that high test-smell prevalence transfers across LLMs.
- **M4 (RQ2 Refactoring, R01–R10):** Constrained AI-assisted refactoring across the 10 validated GPT-3.5 test classes reduced **AR from 113 → 0** (100% reduction) and **LT from 192 → 16** (91.7% reduction), exceeding the preregistered `>80%` threshold without compilation failures, while total test LOC changed from **1,297 → 1,014** (`1_Final_Submission/3_results/evaluation.csv`, `summary.md`).
- **Event B (`TokenQueue.peek()` → `peek(int offset)` on R01):** Chose minimal in-place migration (no new wrapper/abstraction). Original required **17 LOC (+12/−5)** across **3 methods** (26/26 JUnit pass); Refactored required **41 LOC (+36/−5)** across **1 method** (3 → 1 edit points; 11/11 JUnit pass). AR remained 0, but the refactored suite retains a 15-production-method God Test (`testToStringOperations`; official TsDetect reports **AR 0, Eager 8, Lazy 15** on the final R01). The Week 9 refactoring is **partly supported**.

---

## Reproducibility Notes

- **Environment:** Java 17 (JDK 17) and Python 3.x.
- **Dependencies Included:** All required JUnit, library JARs, and `TestSmellDetector.jar` are bundled in `jars/` and `3_Tools/`.
- **Verification Scripts & Artifacts:**
  - `3_Tools/verify.py` — compiles and verifies original and refactored R01–R10 test classes against `jars/` and executes `3_Tools/TestSmellDetector.jar`.
  - `3_Tools/fill_prompt.py`, `3_Tools/gen_short_prompts.py`, `3_Tools/build_final.py` — prompt generation and final submission packaging pipeline for R01–R10.
  - `Python script/` — RQ1 script screenshots and pipeline evidence from the initial M2 verification.
  - `EventB/` — contains exact `production_api.diff`, test diffs, `compile_result.txt`, and `junit_result.txt` for both Original and Refactored R01 variants.

---

## Material AI Usage & Verification

In accordance with the CS4928 brief, generative AI tools (ChatGPT, Gemini, and agentic coding/analysis assistants) were used as engineering and research assistants across the project lifecycle. All AI-generated code, scripts, and analyses were independently verified against actual source code, execution logs, JUnit runs, and `TestSmellDetector.jar` outputs:

- **M1 (Planning & Scope Definition):** Used GenAI to help scope the bounded Defects4J target (RQ1/RQ2) and structure the preregistered decision rules before freezing `M1_PLAN`. All RQs and thresholds were reviewed and confirmed by the team against the paper (`DOI 10.1145/3838597`).
- **M2 & Event A (Scripts, Execution & Data Verification):** Used GenAI to assist in writing and debugging helper scripts, data-processing workflows, and verification summaries for the Defects4J and held-out Mistral 7B TsDetect runs. Every reported prevalence figure (initial M2 run, corrected Java 17 re-run, and Mistral 7B 54.54% AR / 56.04% LT) was computed from raw TsDetect CSV/log outputs rather than AI estimates (`Benchmark1_Java17_Results.xlsx`, `RQ1_Defects4J_AR_LT.csv`, `prevalence_21_smells.csv`).
- **M4 (RQ2 Test Refactoring & Tooling):** Used constrained LLM prompts (`2_Prompts/master_prompt.md`) to generate refactored versions of the 10 validated test classes (R01–R10) and assist with automation scripts (`3_Tools/verify.py`, `fill_prompt.py`, `gen_short_prompts.py`, `build_final.py`). Every AI-refactored class was independently compiled against `jars/`, executed, and scanned with `3_Tools/TestSmellDetector.jar` (`1_Final_Submission/2_tsdetect_raw/` and `3_results/`).
- **Event B (API Evolution, Automated Analysis & Critical Rejection):** Used AI assistance to draft test updates, parse git diffs/LOC metrics, and run a rule-based check (`Event B_Test_Maintainability_Report.md`). Critically, we did **not** accept the M4 AI refactoring uncritically: Event B exposed that the AI over-merged tests during M4 (creating the 15-method `testToStringOperations` God Test). We also noted that the automated analyzer labeled a method-level Eager rule as Lazy Test, and cross-checked it against official TsDetect output (AR 0, Eager 8, Lazy 15) and JUnit execution (Original 26/26 pass; Refactored 11/11 pass) to conclude the M4 design is **partly supported**.
- **Documentation & Synthesis:** Used GenAI to assist with structuring and proofreading `README.md` and report drafts; all claims, file paths, and numbers were verified by team members against committed repository artifacts.
