# R01 original test repair

The shared [TokenQueue](../shared/production/org/jsoup/parser/TokenQueue.java) replaces `peek()` with `peek(int offset)`. The [original R01 sources](../../../4_Original_Samples/R01_TokenQueueTest) at `894ecb6` provide the baseline.

| Build | Compilation | JUnit |
|---|---|---|
| Original API and original tests | Passed | 25/25 passed |
| New API and original tests | Failed at all five zero-argument calls | Not run |
| New API and repaired tests | Passed | 26/26 passed, 0 skipped |

Five calls were migrated to `peek(0)` across `testPeek` (2), `testAdvance` (2) and `testConsume` (1). One new test checks that `peek(1)` returns 'b' for "abc" and that the following `peek(0)` still returns 'a'.

| Source | Files | Methods | Lines added / deleted | Code LOC |
|---|---:|---|---:|---:|
| Original tests | 1 | 3 edited, 1 added | +12 / -5 | 172 → 178 |
| Shared production class | 1 | 1 edited | +14 / -4 | 213 → 220 |

The [build and test logs](evidence/) use JBR 17.0.4.1 with Java 8 compilation targeting. Machine-specific paths are abbreviated. The [patch](R01_changes.patch) shows both source changes.
