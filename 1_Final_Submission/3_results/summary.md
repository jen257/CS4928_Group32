# RQ2 Refactoring Result

## Totals

| Metric | Before | After | Change | Target | Status |
|---|---:|---:|---:|---|---|
| Assertion Roulette | 113 | 0 | 100.0% | <= 22 | PASS |
| Lazy Test | 192 | 16 | 91.7% | <= 38 | PASS |
| Test code LOC | 1297 | 1014 | -283 | - | - |
| Assertions | 334 | 334 | 0 | unchanged | PASS |

All 10 samples compile and pass JUnit (3 repeated runs, 0 failures, 0 errors, 0 skipped).

## Per sample

| Sample | AR before | AR after | LT before | LT after | LOC before | LOC after |
|---|---:|---:|---:|---:|---:|---:|
| R01 | 23 | 0 | 21 | 0 | 172 | 124 |
| R02 | 13 | 0 | 34 | 0 | 179 | 119 |
| R03 | 13 | 0 | 11 | 0 | 172 | 163 |
| R04 | 11 | 0 | 11 | 4 | 96 | 78 |
| R05 | 10 | 0 | 19 | 0 | 71 | 47 |
| R06 | 10 | 0 | 12 | 0 | 104 | 89 |
| R07 | 9 | 0 | 27 | 0 | 131 | 92 |
| R08 | 9 | 0 | 9 | 6 | 133 | 115 |
| R09 | 8 | 0 | 18 | 6 | 133 | 112 |
| R10 | 7 | 0 | 30 | 0 | 106 | 75 |

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
