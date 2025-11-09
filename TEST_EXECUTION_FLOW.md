# 🔄 Test Execution Flow Documentation

## 📋 Table of Contents
1. [Complete Execution Flow](#complete-execution-flow)
2. [Detailed Step-by-Step Flow](#detailed-step-by-step-flow)
3. [Class Interactions](#class-interactions)
4. [Sequence Diagram](#sequence-diagram)

---

## 🎯 Complete Execution Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          TEST EXECUTION START                            │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 1. TestNG Test Runner                                                    │
│    - Reads testng.xml or runs test class directly                       │
│    - Discovers @Test methods in LoginTest.java                          │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 2. TestListener.onStart(ITestContext)                                    │
│    ├─ Logs: "Test Suite Started: <suite-name>"                          │
│    └─ ExtentReportManager.getExtentReports()                            │
│       └─ Initializes HTML report generation                             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 3. TestListener.onTestStart(ITestResult)                                │
│    ├─ Logs: "Test Started: testLoginWithInvalidCredentials"             │
│    └─ ExtentReportManager.createTest(testName, description)             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 4. BaseTest.setUp(@Optional String browser)                             │
│    ├─ @BeforeMethod annotation triggers this                            │
│    ├─ Logs: "=== Test Setup Started ==="                                │
│    └─ Steps:                                                             │
│        ├─ config = ConfigurationManager.getInstance()                   │
│        ├─ driver = DriverManager.getDriver(browser)                     │
│        └─ driver.get(appUrl)                                             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 5. ConfigurationManager.getInstance()                                   │
│    ├─ Singleton Pattern - returns existing instance or creates new      │
│    └─ loadConfiguration()                                                │
│        ├─ Loads config.properties (required)                            │
│        ├─ Loads config.local.properties (optional)                      │
│        └─ Priority: System Props > Env Vars > Local > Default           │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 6. DriverManager.getDriver(browser)                                     │
│    ├─ ThreadLocal<WebDriver> checks if driver exists                    │
│    ├─ If null, creates new driver instance                              │
│    └─ Steps:                                                             │
│        ├─ Determines browser: param > config                            │
│        ├─ Gets platform from config (LOCAL/LAMBDATEST)                  │
│        └─ Calls: driverFactory.createDriver(browser, platform)          │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 7. DriverFactory.createDriver(browser, platform)                        │
│    ├─ Logs: "Creating chrome driver for LOCAL platform"                 │
│    └─ Steps:                                                             │
│        ├─ config = capabilityLoader.load(browser, platform)             │
│        ├─ Decision: LOCAL or LAMBDATEST?                                │
│        ├─ If LOCAL: createLocalDriver(config)                           │
│        ├─ If LAMBDATEST: createRemoteDriver(config)                     │
│        └─ configureDriver(driver, config)                               │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 8. CapabilityLoader.load(browser, platform)                             │
│    ├─ Reads YAML file: capabilities/chrome-local.yml                    │
│    ├─ Parses YAML into BrowserConfig object                             │
│    └─ Returns: BrowserConfig with:                                      │
│        ├─ browser: "chrome"                                              │
│        ├─ platform: "LOCAL"                                              │
│        ├─ headless: false                                                │
│        ├─ timeouts: {implicit, pageLoad, script}                        │
│        └─ window: {maximize, width, height}                             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 9. DriverFactory.createLocalDriver(config)                              │
│    ├─ Finds CapabilityBuilder: ChromeCapabilityBuilder                  │
│    ├─ setupDriverBinary("chrome")                                       │
│    │   └─ WebDriverManager.chromedriver().setup()                       │
│    ├─ options = builder.build(config)                                   │
│    │   └─ ChromeCapabilityBuilder creates ChromeOptions                 │
│    └─ Returns: new ChromeDriver(options)                                │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 10. ChromeCapabilityBuilder.build(config)                               │
│     ├─ Creates ChromeOptions                                            │
│     ├─ Adds arguments from YAML:                                        │
│     │   └─ --disable-notifications, --disable-popup-blocking, etc.      │
│     ├─ Sets preferences from YAML                                       │
│     └─ Returns ChromeOptions                                            │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 11. DriverFactory.configureDriver(driver, config)                       │
│     ├─ Sets implicit wait: 10 seconds                                   │
│     ├─ Sets pageLoad timeout: 30 seconds                                │
│     ├─ Sets script timeout: 30 seconds                                  │
│     └─ Maximizes window (if not headless/remote)                        │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 12. BaseTest.setUp() continues                                          │
│     ├─ driver.get(config.getAppUrl())                                   │
│     │   └─ Navigates to: https://demo.nopcommerce.com                   │
│     └─ Logs: "=== Test Setup Completed ==="                             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 13. TEST METHOD EXECUTION: testLoginWithInvalidCredentials()            │
│     ├─ homePage = new HomePage(driver)                                  │
│     │   └─ BasePage constructor calls PageFactory.initElements()        │
│     ├─ loginPage = homePage.clickLoginLink()                            │
│     │   ├─ WebElementActions.click(driver, loginLink, "Login link")    │
│     │   └─ Returns new LoginPage(driver)                                │
│     ├─ loginPage.enterEmail("invalid@test.com")                         │
│     │   └─ WebElementActions.sendKeys(emailField, email)                │
│     ├─ loginPage.enterPassword("WrongPassword123")                      │
│     ├─ loginPage.clickLoginButton()                                     │
│     ├─ boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed()   │
│     └─ Assert.assertTrue(isErrorDisplayed, "Error msg...")              │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                          ┌─────────┴─────────┐
                          │                   │
                    TEST PASSED         TEST FAILED
                          │                   │
                          ▼                   ▼
              ┌─────────────────┐   ┌──────────────────┐
              │ TestListener    │   │ TestListener     │
              │ .onTestSuccess()│   │ .onTestFailure() │
              │                 │   │                  │
              │ - Log success   │   │ - Log failure    │
              │ - Update report │   │ - Capture        │
              └─────────────────┘   │   screenshot     │
                          │         │ - Attach to      │
                          │         │   report         │
                          │         └──────────────────┘
                          │                   │
                          └─────────┬─────────┘
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 14. BaseTest.tearDown(ITestResult result)                               │
│     ├─ @AfterMethod annotation triggers this                            │
│     ├─ Logs: "=== Test Teardown Started ==="                            │
│     ├─ Gets test name and status from ITestResult                       │
│     ├─ If FAILURE:                                                       │
│     │   └─ ScreenshotUtil.captureScreenshot(driver, testName)           │
│     │       └─ Saves to: screenshots/testName_timestamp.png             │
│     └─ DriverManager.quitDriver()                                       │
│         └─ driver.quit() + ThreadLocal.remove()                         │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 15. Next Test Method (if any)                                           │
│     └─ Repeats from step 3 for each @Test method                        │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 16. TestListener.onFinish(ITestContext)                                 │
│     ├─ Logs: "Test Suite Finished: <suite-name>"                        │
│     └─ ExtentReportManager.flushReports()                               │
│         └─ Generates HTML report: reports/TestReport_<timestamp>.html   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          TEST EXECUTION END                              │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 🔍 Detailed Step-by-Step Flow

### Phase 1: Suite Initialization
```
TestNG Runner
    │
    ├─→ Discovers @Test annotations in LoginTest.java
    │   ├─ testLoginWithInvalidCredentials (priority=1)
    │   ├─ testLoginPageNavigation (priority=2)
    │   └─ testLoginPageElements (priority=3)
    │
    └─→ Triggers TestListener.onStart()
        └─→ Initializes ExtentReportManager
```

### Phase 2: Per-Test Setup (Before Each @Test Method)
```
@BeforeMethod: BaseTest.setUp()
    │
    ├─→ 1. Get ConfigurationManager Instance (Singleton)
    │      └─→ Loads config.properties + config.local.properties
    │
    ├─→ 2. Get WebDriver from DriverManager
    │      │
    │      ├─→ Check ThreadLocal<WebDriver>
    │      │   ├─ If null: Create new driver
    │      │   └─ If exists: Reuse (won't happen in @BeforeMethod)
    │      │
    │      ├─→ Call DriverFactory.createDriver(browser, platform)
    │      │   │
    │      │   ├─→ Load YAML config (CapabilityLoader)
    │      │   │   └─→ Reads: capabilities/chrome-local.yml
    │      │   │
    │      │   ├─→ Find CapabilityBuilder (ChromeCapabilityBuilder)
    │      │   │
    │      │   ├─→ Setup WebDriver binary (WebDriverManager)
    │      │   │   └─→ Downloads chromedriver if needed
    │      │   │
    │      │   ├─→ Build ChromeOptions from YAML
    │      │   │   ├─ Add arguments: --disable-notifications, etc.
    │      │   │   └─ Set preferences: download settings, etc.
    │      │   │
    │      │   ├─→ Create ChromeDriver instance
    │      │   │   └─→ new ChromeDriver(options)
    │      │   │
    │      │   └─→ Configure driver
    │      │       ├─ Set implicit wait: 10s
    │      │       ├─ Set pageLoad timeout: 30s
    │      │       ├─ Set script timeout: 30s
    │      │       └─ Maximize window
    │      │
    │      └─→ Store in ThreadLocal<WebDriver>
    │
    └─→ 3. Navigate to Application
        └─→ driver.get("https://demo.nopcommerce.com")
```

### Phase 3: Test Execution
```
@Test: testLoginWithInvalidCredentials()
    │
    ├─→ 1. Create HomePage object
    │      └─→ new HomePage(driver)
    │          └─→ BasePage constructor
    │              └─→ PageFactory.initElements(driver, this)
    │                  └─→ Initializes @FindBy elements
    │
    ├─→ 2. Navigate to Login Page
    │      └─→ loginPage = homePage.clickLoginLink()
    │          ├─→ WebElementActions.click(loginLink)
    │          │   ├─ Wait for element to be clickable
    │          │   ├─ Highlight element (if enabled)
    │          │   └─ Click element
    │          └─→ Returns new LoginPage(driver)
    │
    ├─→ 3. Enter Email
    │      └─→ loginPage.enterEmail("invalid@test.com")
    │          └─→ WebElementActions.sendKeys(emailField, email)
    │
    ├─→ 4. Enter Password
    │      └─→ loginPage.enterPassword("WrongPassword123")
    │
    ├─→ 5. Click Login Button
    │      └─→ loginPage.clickLoginButton()
    │
    ├─→ 6. Verify Error Message
    │      └─→ boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed()
    │
    └─→ 7. Assert Result
        └─→ Assert.assertTrue(isErrorDisplayed)
            ├─ If TRUE: Test continues to tearDown
            └─ If FALSE: AssertionError thrown → TestListener.onTestFailure()
```

### Phase 4: Test Teardown (After Each @Test Method)
```
@AfterMethod: BaseTest.tearDown(ITestResult result)
    │
    ├─→ 1. Extract Test Information
    │      ├─ Test name: result.getMethod().getMethodName()
    │      └─ Test status: result.getStatus()
    │
    ├─→ 2. Check Test Status
    │      │
    │      ├─→ If FAILURE:
    │      │   └─→ ScreenshotUtil.captureScreenshot(driver, testName)
    │      │       ├─ Take screenshot
    │      │       ├─ Save to: screenshots/testName_timestamp.png
    │      │       └─ Attach to ExtentReport
    │      │
    │      ├─→ If SUCCESS:
    │      │   └─→ Log success (no screenshot)
    │      │
    │      └─→ If SKIP:
    │          └─→ Log skip reason
    │
    └─→ 3. Cleanup WebDriver
        └─→ DriverManager.quitDriver()
            ├─→ driver.quit() (closes all browser windows)
            └─→ ThreadLocal.remove() (clears thread storage)
```

### Phase 5: Suite Finalization
```
TestListener.onFinish(ITestContext context)
    │
    ├─→ Get suite statistics
    │   ├─ Total tests: context.getAllTestMethods().size()
    │   ├─ Passed: context.getPassedTests().size()
    │   ├─ Failed: context.getFailedTests().size()
    │   └─ Skipped: context.getSkippedTests().size()
    │
    └─→ Generate Reports
        └─→ ExtentReportManager.flushReports()
            └─→ Creates: reports/TestReport_2025-11-10_<time>.html
```

---

## 🏗️ Class Interactions

```
┌──────────────┐
│  LoginTest   │ extends
│  .java       │◄────────────┐
└──────────────┘             │
                             │
                    ┌────────────────┐
                    │   BaseTest     │
                    │   .java        │
                    └────────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│Configuration │    │DriverManager │    │ TestListener │
│Manager.java  │    │  .java       │    │  .java       │
└──────────────┘    └──────────────┘    └──────────────┘
        │                    │                    │
        │                    ▼                    ▼
        │           ┌──────────────┐    ┌──────────────┐
        │           │DriverFactory │    │ExtentReport  │
        │           │  .java       │    │Manager.java  │
        │           └──────────────┘    └──────────────┘
        │                    │
        │         ┌──────────┼──────────┐
        │         │          │          │
        │         ▼          ▼          ▼
        │  ┌────────┐ ┌────────┐ ┌────────┐
        │  │Chrome  │ │Firefox │ │ Edge   │
        │  │Builder │ │Builder │ │Builder │
        │  └────────┘ └────────┘ └────────┘
        │         │          │          │
        │         └──────────┼──────────┘
        │                    ▼
        │           ┌──────────────┐
        └──────────►│Capability    │
                    │Loader.java   │
                    └──────────────┘
                             │
                             ▼
                    ┌──────────────┐
                    │YAML Config   │
                    │chrome-       │
                    │local.yml     │
                    └──────────────┘

Page Objects:
┌──────────────┐
│  LoginTest   │
└──────────────┘
        │ uses
        ▼
┌──────────────┐     ┌──────────────┐
│  HomePage    │────►│  LoginPage   │
│  .java       │     │  .java       │
└──────────────┘     └──────────────┘
        │                    │
        │ extends            │ extends
        ▼                    ▼
┌──────────────────────────────────┐
│         BasePage.java            │
│  (PageFactory initialization)    │
└──────────────────────────────────┘
```

---

## 📊 Sequence Diagram

```
User          TestNG         BaseTest        DriverManager      DriverFactory      ChromeDriver      LoginTest
  │             │               │                  │                  │                 │              │
  │─Run Test───►│               │                  │                  │                 │              │
  │             │─@BeforeMethod►│                  │                  │                 │              │
  │             │               │─getDriver()─────►│                  │                 │              │
  │             │               │                  │─createDriver()──►│                 │              │
  │             │               │                  │                  │─new Chrome─────►│              │
  │             │               │                  │                  │    Driver()     │              │
  │             │               │                  │                  │◄────driver──────│              │
  │             │               │                  │◄─────driver──────│                 │              │
  │             │               │◄─────driver──────│                  │                 │              │
  │             │               │─get(url)────────────────────────────────────────────►│              │
  │             │               │                  │                  │                 │              │
  │             │─@Test────────────────────────────────────────────────────────────────┼─────────────►│
  │             │               │                  │                  │                 │              │
  │             │               │                  │                  │                 │◄─actions────│
  │             │               │                  │                  │                 │  (click,    │
  │             │               │                  │                  │                 │   type,     │
  │             │               │                  │                  │                 │   verify)   │
  │             │               │                  │                  │                 │─────────────►│
  │             │               │                  │                  │                 │              │
  │             │◄─────────────────────────────────────────────────────────────────────┼──result─────│
  │             │               │                  │                  │                 │              │
  │             │─@AfterMethod─►│                  │                  │                 │              │
  │             │               │─quitDriver()────►│                  │                 │              │
  │             │               │                  │─quit()──────────────────────────►│              │
  │             │               │                  │◄────────────────────────────────┘              │
  │             │               │◄─────────────────│                  │                               │
  │◄──Report───│               │                  │                  │                               │
```

---

## 📝 Key Takeaways

### 1. **TestNG Annotations Order**
- `@BeforeMethod` → Runs before EACH test method
- `@Test` → Actual test execution
- `@AfterMethod` → Runs after EACH test method

### 2. **Driver Lifecycle**
- Created fresh for each test method (via @BeforeMethod)
- Stored in ThreadLocal for thread-safety
- Quit after each test (via @AfterMethod)

### 3. **Configuration Priority**
1. System Properties (`-Dbrowser=chrome`)
2. Environment Variables (`export BROWSER=chrome`)
3. Local Config (`config.local.properties`)
4. Default Config (`config.properties`)

### 4. **Reporting Flow**
- **TestListener** hooks into TestNG lifecycle
- **ExtentReportManager** generates HTML reports
- Screenshots attached on failure

### 5. **Page Object Pattern**
- Tests interact with Page Objects (HomePage, LoginPage)
- Page Objects extend BasePage
- BasePage uses PageFactory for element initialization

---

## 🎯 Quick Reference

### For LOCAL Execution:
```
TestNG → BaseTest.setUp() → ConfigurationManager → DriverManager
→ DriverFactory → CapabilityLoader (YAML) → ChromeCapabilityBuilder
→ WebDriverManager (downloads driver) → ChromeDriver → Test Execution
→ BaseTest.tearDown() → DriverManager.quitDriver()
```

### For REMOTE Execution (LambdaTest):
```
TestNG → BaseTest.setUp() → ConfigurationManager → DriverManager
→ DriverFactory → CapabilityLoader (YAML) → ChromeCapabilityBuilder
→ RemoteWebDriver (connects to cloud) → Test Execution
→ BaseTest.tearDown() → DriverManager.quitDriver()
```

---

## 📚 Related Files

| File | Purpose |
|------|---------|
| `BaseTest.java` | Test foundation with @BeforeMethod/@AfterMethod |
| `LoginTest.java` | Actual test methods with @Test |
| `DriverManager.java` | ThreadLocal driver management |
| `DriverFactory.java` | Driver creation logic |
| `ConfigurationManager.java` | Configuration loading (Singleton) |
| `TestListener.java` | TestNG lifecycle hooks |
| `ExtentReportManager.java` | HTML report generation |
| `chrome-local.yml` | Chrome browser capabilities |
| `config.properties` | Application configuration |

---

**Generated on:** November 10, 2025  
**Framework Version:** 2.0
