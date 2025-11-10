#!/bin/bash

# Quick Verification Script for GHA Fixes
echo "🔍 Verifying GHA Test Execution Fixes..."
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
grep -q "^browser=chrome" src/main/resources/config.properties && echo "   ✅ Default browser: chrome" || echo "   ⚠️  Check browser setting"

# Compile
echo ""
echo "4. Compiling project..."
mvn clean compile -q && echo "   ✅ Compilation successful" || echo "   ❌ Compilation failed"

echo ""
echo "✅ Verification complete!"
echo ""
echo "Next steps:"
echo "  1. git add ."
echo "  2. git commit -m 'Fix GHA test execution issues'"
echo "  3. git push origin main"
