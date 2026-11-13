# Event B — Test Maintainability Comparison Report

**Subject:** `TokenQueueTest.java` — unit tests before vs after the production API change `peek()` → `peek(int offset)`.
**Analyses:** LOC change extraction and test-smell detection (TsDetect rules for Assertion Roulette and Lazy Test).

---

## 1. LOC changed (`TokenQueueTest.java`)

| Metric | Original Version | Refactored Version |
|---|---|---|
| Insertions | 12 | 36 |
| Deletions | 5 | 5 |
| **Total LOC changed** | **17** | **41** |
| Net test-code LOC | 172 → 178 (+6) | after = 191 lines |
| `peek()` → `peek(0)` call sites fixed | 5 calls across **3 methods** | 5 calls across **1 method** |
| New test methods added | 1 (`testPeekAheadDoesNotAdvance`) | 2 (`testPeekWithOffset`, `testPeekWithNegativeOffset`) |
| Total test methods | 26 | 11 |

```
Original Version LOC Changed:  17 (+12 / -5)
Refactored Version LOC Changed: 41 (+36 / -5)
```

**Evidence**
- Original: `R01_B_Original_for_D/For_D.md`; `evidence/diff_counts.json` and `evidence/git_numstat.txt` (`12 5 TokenQueueTest.java`).
- Refactored: `refactored/R01_TokenQueueTest/evidence/refactored_test.diff` (parsed: +36 / −5).
- The shared production change (`TokenQueue.java`, method `peek`, +14/−4) is identical in both versions and is counted once when comparing.

---

## 2. Test-smell detection (TsDetect rules)

**Rules applied**
- **Assertion Roulette (AR):** a test method has **> 1 assertion** AND **≥ 1 assertion without an explanatory message**.
- **Lazy Test (LT):** a test method invokes **> 1 distinct production method** of `TokenQueue` (constructors excluded).

> Note: TsDetect's binary could not be executed in this environment (no build tool / no outbound network from the shell), so the two detectors were reproduced from the published TsDetect rules ([testsmells.org](https://testsmells.org/), Virgínio et al. 2019, [TSDetect repo](https://github.com/TestSmells/tsdetect-intellij-plugin)) and applied to both files with a small analyzer (`analysis/TestSmellAnalyzer.java`).

### 2.1 Original Version

| Smell | Flagged methods |
|---|---|
| Assertion Roulette (AR) | **11 / 26** (`testIsEmpty`, `testPeek`, `testMatches`, `testMatchesCS`, `testMatchesAny`, `testMatchChomp`, `testMatchesWhitespace`, `testMatchesWord`, `testAdvance`, `testConsume`, `testConsumeWhitespace`) |
| Lazy Test (LT) | **17 / 26** (raw rule) — see note |

**Note:** of the 17 LT flags, 15 are only triggered because the assertion uses `tq.toString()` to read the queue state; the two genuine multi-behaviour tests are `testAdvance` and `testConsume` (each exercises 2 production methods).

### 2.2 Refactored Version

| Smell | Flagged methods |
|---|---|
| Assertion Roulette (AR) | **0 / 11** — every assertion now carries a descriptive message |
| Lazy Test (LT) | **3 / 11**: `testPeekAdvanceAndConsume` (3 methods), `testPeekWithOffset` (2 methods), `testToStringOperations` (**15 production methods — God Test**) |

**God Test note:** `testToStringOperations` merges 15 production methods (`addFirst`, `matchChomp`, `consumeTo`, `consumeToIgnoreCase`, `consumeToAny`, `chompTo`, `chompToIgnoreCase`, `chompBalanced`, `consumeWhitespace`, `consumeWord`, `consumeTagName`, `consumeElementSelector`, `consumeCssIdentifier`, `consumeAttributeKey`, `remainder`) into a single 60-line test method.

```
Original Version Test Smells:   AR = 11/26 methods, LT = 17 methods (2 genuine; 15 via toString() state-check)
Refactored Version Test Smells: AR = 0/11 methods,  LT = 3 methods (incl. one 15-method God Test)
```

---

## 3. Comparison table

| Dimension | Original Version | Refactored Version | Result |
|---|---|---|---|
| LOC to repair (test file) | 17 lines | 41 lines | Refactored larger |
| Edit points for the same `peek()` fix | 3 methods | 1 method | Refactored fewer |
| Assertion Roulette (AR) | 11 methods | 0 methods | Refactored better |
| Lazy Test (LT) | small focused tests (2 mild) | 1 God Test + 2 merged tests | Refactored worse |
| Test granularity | 26 methods | 11 methods | Refactored coarser |

---

## 4. Verdict

- **Assertion Roulette — eliminated.** The refactored version added explanatory messages to every assertion, dropping AR from 11 methods to 0.
- **Excessive abstraction — introduced.** The refactoring over-merged tests, creating Lazy Tests: a 15-production-method `testToStringOperations` God Test and a merged `testPeekAdvanceAndConsume`. Assertion Roulette was effectively traded for Lazy Test.
- **Maintenance LOC — not reduced.** The refactored version required more lines to repair (41 vs 17), chiefly because it added more comprehensive new tests. Its genuine benefit is qualitative: the five identical `peek()` → `peek(0)` fixes collapsed from three methods into one.

> **Bottom line:** the refactor improved *assertion* maintainability (AR removed) but degraded *test-structure* maintainability (Lazy/God Tests) and did not reduce repair effort. Both versions compile and pass (Original: 26/26; Refactored: 11/11).
