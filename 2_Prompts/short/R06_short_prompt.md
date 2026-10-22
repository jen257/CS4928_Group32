Sample R06 (RandomStringUtilsTest).

I need to refactor the attached JUnit test class to eliminate two test smells
measured by the TsDetect tool: Assertion Roulette (AR) and Lazy Test (LT).
Keep every assertion, input, and expected value unchanged.

These are the exact counting rules of TsDetect; follow them precisely:

- Assertion Roulette (AR): a test method with 2 or more assertion statements
  AND at least one assertion without a message parameter counts once. To remove
  it, add a descriptive failure message to EVERY assertion. Do not split methods
  to fix AR.

- Lazy Test (LT): for each production method M, if M is called from 2 or more
  different test methods, TsDetect adds M's total invocation count to the score;
  if M is called from only one test method it contributes 0. To reduce LT, MERGE
  test methods that observe the same production method (for example toString(),
  peek(), get()) so each production method is called from exactly one test
  method. Splitting methods INCREASES LT - do not split.

Requirements:
1. Add a clear, descriptive failure message to every assertion.
2. Merge test methods that share the same production observation point, so each
   production method is called from a single test method.
3. Do not delete or change any assertion, input, expected value, or the
   setup/act/assert order. The total assertion count after refactoring must
   equal the original.
4. Do not move assertions into helper or private methods.
5. Keep the JUnit 4 @Test annotations, imports, package name and class name
   unchanged. Do not modify the production class.

Output:
1. A short list of which original test methods were merged into which new methods.
2. The complete refactored Java file, from the package declaration to the final closing brace, with no truncation.
3. The assertion count before and after refactoring - these must match.

The attached files are the test class and its production class.
Baseline (from TsDetect): AR=10, LT=12, LOC=104.
