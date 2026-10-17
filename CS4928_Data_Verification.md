# Data Verification Report — Defects4J / TsDetect

> **Read-only GitHub version** of the Excel verification workbook. All reported values and notes below are sourced from the uploaded workbook. This Markdown is static and does not recalculate Excel formulas.

## Prevalence Comparison

| Generator | Test Smell | Total Samples | Positive Samples | Paper Prevalence (%) | Our Prevalence (%) | Deviation (pp) | Result |
| --- | --- | --- | --- | --- | --- | --- | --- |
| GPT-3.5 | AR | 9,760 | 4,581 | 46.94 | 46.94 | 0.00 | PASS |
| GPT-3.5 | LT | 9,760 | 4,750 | 48.67 | 48.67 | 0.00 | PASS |
| EvoSuite | AR | 4,346 | 1,662 | 38.24 | 38.24 | 0.00 | PASS |
| EvoSuite | LT | 4,346 | 3,344 | 82.18 | 76.94 | 5.24 | PASS |

**Decision rule:** Absolute deviation = |Paper prevalence − Our prevalence|, measured in percentage points (pp). A result passes when deviation is **≤ 15 pp**.

**Main observation:** EvoSuite Lazy Test (LT) is **76.94%** in the CSV-derived results versus **82.18%** in the paper: a **5.24 pp** difference. Its cause remains unknown.

## 1. Data Sources

| Field | Details |
| --- | --- |
| Research Paper | On the Diffusion of Test Smells in LLM-Generated Unit Tests |
| Reference Table | Appendix A, Table 17(a) - TsDetect |
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
| GPT-3.5 Samples | 9,760 |
| EvoSuite Samples | 4,346 |

## 3. Data Quality Checks

| Field | Details |
| --- | --- |
| Zero-Method Records | 13 GPT-3.5 records have NumberOfMethods = 0. |
| Primary Denominator | All 9,760 qualifying GPT-3.5 records were retained for the main comparison. |
| Alternative Denominator | Excluding the 13 records leaves 9,747 samples. |
| Sensitivity Analysis | With 9,747 samples, AR prevalence is 47.00% and LT prevalence is 48.73%. |
| Methodology Note | The paper does not explicitly specify how zero-method records should be handled. |
| Duplicate Records | No exact duplicate rows or repeated TestFilePath values were found in the filtered Defects4J data. |
| Missing Values | No missing values were found in TestFilePath, NumberOfMethods, AR, or LT. |
| Empty Path Fields | RelativeTestFilePath and RelativeProductionFilePath are empty but do not affect the prevalence calculations. |
| Numeric Validation | All NumberOfMethods, AR, and LT values are non-negative integers. |

## 4. Verification Conclusion

| Field | Details |
| --- | --- |
| GPT-3.5 Results | AR = 46.94% and LT = 48.67%. Both match the values reported in Table 17(a). |
| EvoSuite Results | AR = 38.24%, matching the paper. LT = 76.94%, compared with 82.18% in the paper. |
| Maximum Deviation | 5.24 percentage points (EvoSuite LT). |
| Verification Result | All four prevalence values are within the predefined 15-percentage-point threshold. |
| Limitation | The cause of the EvoSuite LT discrepancy remains unknown. |

## Calculation Reference

The prevalence rule recorded in the workbook is **positive samples / total samples × 100**; a positive sample has a detected AR or LT occurrence count **greater than 0**.

| Generator / smell | Calculation | Prevalence (%) |
| --- | --- | --- |
| GPT-3.5 / AR | 4,581 / 9,760 × 100 | 46.94 |
| GPT-3.5 / LT | 4,750 / 9,760 × 100 | 48.67 |
| EvoSuite / AR | 1,662 / 4,346 × 100 | 38.24 |
| EvoSuite / LT | 3,344 / 4,346 × 100 | 76.94 |

For live Excel formulas and the embedded reference-table image, consult the [original verification workbook](./CS4928_Data_Verification.xlsx). The raw CSVs and detector execution logs are not embedded in this Markdown file.
