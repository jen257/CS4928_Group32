# Data Verification Report — Defects4J / TsDetect

## 1. Data Sources

| Field | Details |
| --- | --- |
| Research Paper | On the Diffusion of Test Smells in LLM-Generated Unit Tests |
| Reference Table | Appendix A, Table 17(a) — TsDetect |
| Dataset | Defects4J |
| Detection Tool | TsDetect |
| GPT-3.5 CSV | GPT_Merged_Results.csv |
| EvoSuite CSV | Benchmark1_TSDetect_Merged_Quarter.csv |
| Test Smells | Assertion Roulette (AR), Lazy Test (LT) |

## 2. Counting Method

| Field | Details |
| --- | --- |
| Unit of Analysis | One qualifying CSV record represents one test sample. |
| Dataset Filter | Only Defects4J records were included. |
| Positive Case Rule | A sample is positive if the detected AR or LT occurrence count is greater than 0. |
| Prevalence Formula | (Positive Samples / Total Samples) × 100 |
| Deviation Threshold | 15 percentage points |
| Decision Rule | Non-reproduction if the deviation exceeds 15 percentage points. |

## 3. Reproduction Comparison

| Generator | Test Smell | Paper Prevalence (%) | Local Prevalence (%) | Deviation (pp) | Result |
| --- | --- | ---: | ---: | ---: | --- |
| GPT-3.5 | AR | 46.94 | 9.39 | 37.55 | **FAIL** |
| GPT-3.5 | LT | 48.67 | 0.00 | 48.67 | **FAIL** |
| EvoSuite | AR | 38.24 | 38.24 | 0.00 | **PASS** |
| EvoSuite | LT | 82.18 | 76.94 | 5.24 | **PASS** |

**Comparison Summary:** GPT-3.5 failed both comparisons, while EvoSuite passed both comparisons.

**Maximum Deviation:** 48.67 percentage points (GPT-3.5 LT).

**Overall Result:** **NON-REPRODUCTION**.

## 4. Verification Conclusion

| Field | Details |
| --- | --- |
| GPT-3.5 Results | AR = 9.39% and LT = 0.00%. Both exceed the predefined 15-percentage-point deviation threshold. |
| EvoSuite Results | AR = 38.24% and LT = 76.94%. Both are within the predefined threshold. |
| Maximum Deviation | 48.67 percentage points (GPT-3.5 LT). |
| Reproduction Result | **NON-REPRODUCTION:** GPT-3.5 failed to reproduce the paper's reported AR and LT prevalence, while EvoSuite met the reproduction threshold. |
| Final Conclusion | The overall reproduction was unsuccessful because the GPT-3.5 results exceeded the predefined 15-percentage-point threshold. |
