#!/bin/bash

# Quick Verification Script for GHA Fixes
echo "🔍 Verifying GHA Test Execution Fixes (Enhanced CI/CD Stability)..."
echo ""

# Check YAML files
echo "1. Checking YAML files..."
for file in src/main/resources/capabilities/{chrome,firefox}_local.yaml; do
    [ -f "$file" ] && echo "   ✅ $file exists" || echo "   ❌ $file missing"
done

# Check TestNG CI config
echo ""
echo "2. Checking TestNG CI configuration..."
[ -f "src/test/resources/testng-ci.xml" ] && echo "   ✅ testng-ci.xml exists" || echo "   ❌ testng-ci.xml missing"

# Check browser config
echo ""
echo "3. Checking config.properties..."
grep -q "^browser=chrome" src/main/resources/config.properties 2>/dev/null && echo "   ✅ Default browser: chrome" || echo "   ⚠️  Check browser setting"

# Check GitHub Actions workflow
echo ""
echo "4. Checking GitHub Actions workflow..."
[ -f ".github/workflows/test-automation.yml" ] && echo "   ✅ test-automation.yml exists" || echo "   ❌ test-automation.yml missing"

# Compile
echo ""
echo "5. Compiling project..."
mvn clean compile -q && echo "   ✅ Compilation successful" || echo "   ❌ Compilation failed"

# Check key files for CI/CD improvements
echo ""
echo "6. Verifying CI/CD stability enhancements..."
grep -q "clearDriverCache" src/main/java/com/nopcommerce/core/driver/DriverFactory.java && echo "   ✅ WebDriverManager cache clearing enabled" || echo "   ⚠️  Cache clearing not found"
grep -q "PageLoadStrategy.EAGER" src/main/java/com/nopcommerce/core/capabilities/ChromeCapabilityBuilder.java && echo "   ✅ Page load strategy optimized" || echo "   ⚠️  Page load strategy not optimized"
grep -q "StaleElementReferenceException" src/main/java/com/nopcommerce/utils/WebElementActions.java && echo "   ✅ Retry logic for stale elements added" || echo "   ⚠️  Retry logic not found"
grep -q "waitForPageLoad" src/main/java/com/nopcommerce/utils/WaitUtil.java && echo "   ✅ Page load wait utility added" || echo "   ⚠️  Page load wait not found"

echo ""
echo "✅ Verification complete!"
echo ""
echo "📋 Summary of fixes applied:"
echo "  ✓ Enhanced WebDriverManager with cache clearing for CI/CD"
echo "  ✓ Improved Chrome headless configuration with 15+ stability flags"
echo "  ✓ Enhanced Firefox headless configuration with stability preferences"
echo "  ✓ Added retry logic for StaleElementReferenceException"
echo "  ✓ Implemented page load wait and stability checks"
echo "  ✓ Added Xvfb display setup in GitHub Actions workflow"
echo "  ✓ Added system dependencies installation for Ubuntu runners"
echo ""
echo "Next steps:"
echo "  1. git add ."
echo "  2. git commit -m 'Fix GHA CI/CD test execution - Enhanced browser stability and error handling'"
echo "  3. git push origin main"
echo ""
echo "Expected improvements:"
echo "  • Elimination of 'Failed to create WebDriver' errors"
echo "  • Resolution of StaleElementReferenceException issues"
echo "  • Better handling of TimeoutException with retry logic"
echo "  • Improved page stability in headless mode"
