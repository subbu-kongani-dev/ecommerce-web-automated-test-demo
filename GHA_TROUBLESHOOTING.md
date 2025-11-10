# GitHub Actions Test Execution - Fix & Troubleshooting Guide

## 🎯 Quick Status
**Date:** November 10, 2025  
**Status:** ✅ All GHA test execution issues FIXED and VERIFIED  
**Tested:** Chrome & Firefox in headless mode - Working perfectly!

## Overview
This guide documents the fixes applied for GitHub Actions (GHA) test execution issues and provides troubleshooting for both Local and LambdaTest execution platforms.

---

## 🚀 Quick Command Reference

### Local Execution
```bash
# Default (Chrome, visible)
mvn clean test

# Chrome headless
mvn clean test -Dheadless=true

# Firefox headless
mvn clean test -Dbrowser=firefox -Dheadless=true

# Specific test
mvn test -Dtest=LoginTest
```

### LambdaTest Execution
```bash
export LT_USERNAME="your_username"
export LT_ACCESS_KEY="your_access_key"
mvn clean test -Dexecution.platform=LAMBDATEST -Dbrowser=chrome
```

### Simulate GHA Locally
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-ci.xml \
  -Dbrowser=chrome -Dheadless=true -Dexecution.platform=Local
```

---

## Recent Fixes (November 2025)

### 1. Firefox YAML Parsing Errors
**Problem:** 
- Tests failed with `NullPointerException: Cannot invoke "java.util.Map.get(Object)" because "data" is null`
- `IndexOutOfBoundsException` in YAML parser

**Root Cause:**
- YAML loader was not validating empty or null data after parsing
- No fallback mechanism for malformed YAML files

**Solution:**
```java
// Added validation in CapabilityLoader.java
if (yamlData == null || yamlData.isEmpty()) {
    throw new ConfigurationException("YAML file is empty or invalid: " + fileName);
}
```

### 2. Chrome Headless Mode Issues on GHA
**Problem:**
- Chrome instance exited with error: `session not created: Chrome instance exited`
- Missing proper headless configuration for CI/CD environments

**Root Cause:**
- Old headless flag (`--headless`) not compatible with modern Chrome
- Missing critical arguments for containerized environments (GHA runners)

**Solution:**
```java
// Updated ChromeCapabilityBuilder.java
if (config.isHeadless()) {
    options.addArguments("--headless=new");  // Modern headless mode
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
}
```

### 3. System Property Override for Headless Mode
**Problem:**
- `-Dheadless=true` from Maven command line was not being applied
- YAML file `headless: false` was always used

**Root Cause:**
- No mechanism to override YAML configuration with system properties

**Solution:**
```java
// Added in CapabilityLoader.java
String headlessOverride = System.getProperty("headless");
if (headlessOverride != null) {
    boolean isHeadless = Boolean.parseBoolean(headlessOverride);
    return browserConfig.toBuilder().headless(isHeadless).build();
}
```

### 4. Browser Configuration Issues
**Problem:**
- config.properties had `browser=safari` which doesn't work on Linux GHA runners
- No browser parameter passed to tests

**Root Cause:**
- Default browser was platform-specific
- TestNG XML didn't accept browser parameters

**Solution:**
- Changed default browser to `chrome` in config.properties
- Created `testng-ci.xml` for GHA execution
- Updated workflow to pass: `-Dbrowser=${{ matrix.browser }}`

## ✅ Verification Results

**Tests executed successfully with:**
- ✅ Chrome headless mode - Working
- ✅ Firefox headless mode - Working  
- ✅ YAML configuration loading - Working
- ✅ System property override - Working
- ✅ WebDriver initialization - No errors

**Key Success Logs:**
```
Loading capabilities from: capabilities/chrome_local.yaml
Overriding headless mode from system property: true
✅ Headless mode enabled with window size: 1920x1080
✅ chrome driver created successfully for Local platform
✅ Driver initialized successfully
```

**Files Modified:**
- `CapabilityLoader.java` - YAML validation & headless override
- `BrowserConfig.java` - Added toBuilder() method
- `ChromeCapabilityBuilder.java` - Modern headless mode
- `FirefoxCapabilityBuilder.java` - Window size configuration
- `config.properties` - Default browser to chrome
- `chrome_local.yaml` - CI/CD arguments
- `test-automation.yml` - CI-specific suite

**Files Created:**
- `testng-ci.xml` - CI/CD optimized test suite

---

## Configuration Files

### 1. Local Execution
```properties
# config.properties
execution.platform=Local
browser=chrome
headless=false
```

**Command:**
```bash
mvn clean test
```

### 2. Local Execution with Different Browser
```bash
mvn clean test -Dbrowser=firefox
```

### 3. Local Execution in Headless Mode
```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

### 4. LambdaTest Execution
```properties
# config.properties or config.local.properties
execution.platform=LAMBDATEST
browser=chrome
```

**Set Environment Variables:**
```bash
export LT_USERNAME="your_username"
export LT_ACCESS_KEY="your_access_key"
```

**Command:**
```bash
mvn clean test -Dexecution.platform=LAMBDATEST
```

### 5. GitHub Actions Execution
The workflow automatically handles:
- Browser installation (Chrome, Firefox)
- Headless mode configuration
- Platform setting (Local)
- Test suite selection (testng-ci.xml)

**Workflow Command:**
```bash
mvn test \
  -Dsurefire.suiteXmlFiles=src/test/resources/testng-ci.xml \
  -Dbrowser=${{ matrix.browser }} \
  -Dheadless=true \
  -Dexecution.platform=Local
```

## Test Suite Files

### testng.xml (Default)
- Used for local development
- No browser parameters
- Uses config.properties settings

### testng-ci.xml (CI/CD)
- Used for GitHub Actions
- Sequential execution
- Accepts browser from system property
- Optimized for GHA runners

### testng-parallel-browsers.xml
- Multi-browser parallel execution
- Explicit browser parameters in XML
- Best for cross-browser testing locally

## Common Issues & Solutions

### Issue 1: "Failed to load capabilities: capabilities/firefox_local.yaml"
**Symptoms:**
- `ConfigurationException` during test setup
- Cannot find or parse YAML file

**Solutions:**
1. Verify YAML file exists: `ls -la src/main/resources/capabilities/`
2. Check YAML syntax: No tabs, proper indentation
3. Ensure file is not empty
4. Rebuild project: `mvn clean compile`

### Issue 2: Chrome fails to start on GHA
**Symptoms:**
- "Chrome instance exited"
- Session not created error

**Solutions:**
1. Ensure headless mode is enabled: `-Dheadless=true`
2. Verify Chrome is installed in workflow:
   ```yaml
   - name: Install Chrome Browser
     uses: browser-actions/setup-chrome@latest
   ```
3. Check chrome_local.yaml has required arguments:
   - `--no-sandbox`
   - `--disable-dev-shm-usage`
   - `--disable-gpu`

### Issue 3: Tests skip with "Failed to create WebDriver"
**Symptoms:**
- All tests skip after first failure
- RuntimeException in setUp

**Solutions:**
1. Check execution platform: `-Dexecution.platform=Local`
2. Verify browser parameter: `-Dbrowser=chrome`
3. Ensure LambdaTest credentials are set (if using LAMBDATEST)

### Issue 4: Parallel execution issues
**Symptoms:**
- Browser windows interfere with each other
- Random test failures

**Solutions:**
1. Reduce thread count: `thread-count="1"` for CI/CD
2. Use `testng-ci.xml` for GHA (sequential execution)
3. For parallel local testing, use `testng-parallel-browsers.xml`

## Verification Commands

### 1. Verify Configuration Loading
```bash
mvn test -Dtest=HomePageTest -Dbrowser=chrome -X | grep "Loading capabilities"
```

### 2. Check YAML Files
```bash
# Validate Firefox YAML
cat src/main/resources/capabilities/firefox_local.yaml | head -20

# Validate Chrome YAML
cat src/main/resources/capabilities/chrome_local.yaml | head -20
```

### 3. Test Local Execution
```bash
# Chrome headless
mvn clean test -Dbrowser=chrome -Dheadless=true -Dtest=HomePageTest

# Firefox headless
mvn clean test -Dbrowser=firefox -Dheadless=true -Dtest=HomePageTest
```

### 4. Simulate GHA Environment
```bash
mvn test \
  -Dsurefire.suiteXmlFiles=src/test/resources/testng-ci.xml \
  -Dbrowser=chrome \
  -Dheadless=true \
  -Dexecution.platform=Local
```

## Best Practices

### 1. Local Development
- Use `headless=false` in config.properties
- Use default testng.xml
- Test one browser at a time

### 2. CI/CD (GitHub Actions)
- Always use `headless=true`
- Use testng-ci.xml for sequential execution
- Run matrix strategy for multiple browsers
- Set `execution.platform=Local` explicitly

### 3. LambdaTest Execution
- Store credentials in GitHub Secrets
- Use `execution.platform=LAMBDATEST`
- Monitor usage in LambdaTest dashboard
- Use LambdaTest-specific YAML files

## GitHub Actions Workflow Matrix

Current matrix configuration:
```yaml
matrix:
  os: [ubuntu-latest]
  browser: [chrome, firefox]
  java: [17]
```

This creates 2 jobs:
1. ubuntu-latest + chrome + Java 17
2. ubuntu-latest + firefox + Java 17

To add more browsers:
```yaml
matrix:
  browser: [chrome, firefox, edge]
```

## Environment Variables

### Local Development
```bash
# config.properties takes precedence
export BROWSER=firefox
export HEADLESS=false
export EXECUTION_PLATFORM=Local
```

### GitHub Actions
```yaml
env:
  BROWSER: ${{ matrix.browser }}
  HEADLESS: true
  EXECUTION_PLATFORM: Local
  LT_USERNAME: ${{ secrets.LT_USERNAME }}
  LT_ACCESS_KEY: ${{ secrets.LT_ACCESS_KEY }}
```

## Support

For issues not covered here:
1. Check logs: `logs/automation.log`
2. Review screenshots: `screenshots/`
3. Check TestNG reports: `test-output/`
4. Enable debug logging: `mvn test -X`

## Related Files
- `config.properties` - Main configuration
- `testng-ci.xml` - CI/CD test suite
- `chrome_local.yaml` - Chrome capabilities
- `firefox_local.yaml` - Firefox capabilities
- `.github/workflows/test-automation.yml` - GHA workflow
