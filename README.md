# E-Commerce Web Automated Test Demo

A comprehensive, scalable, and maintainable web automation testing framework built with Selenium WebDriver, TestNG, and the Page Object Model (POM) design pattern. This framework demonstrates **enterprise-grade test automation** with externalized test data, centralized utilities, and industry best practices for testing e-commerce applications.

**Latest Updates (November 2025)**:
- ✅ Enhanced data-driven testing with external JSON/CSV files
- ✅ Centralized DataProvider utilities for reusable test data
- ✅ Refactored BasePage with WebElementActions utility class
- ✅ Navigation menu testing with 16 comprehensive scenarios
- ✅ Industry-standard architecture following separation of concerns

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.15.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-blue.svg)](https://maven.apache.org/)
[![CI/CD](https://github.com/yourusername/ecommerce-web-automated-test-demo/workflows/E-Commerce%20Test%20Automation%20CI/CD/badge.svg)](https://github.com/yourusername/ecommerce-web-automated-test-demo/actions)
[![Scheduled Tests](https://github.com/yourusername/ecommerce-web-automated-test-demo/workflows/Scheduled%20Smoke%20Tests/badge.svg)](https://github.com/yourusername/ecommerce-web-automated-test-demo/actions)
[![Security](https://img.shields.io/badge/Security-Protected-brightgreen.svg)](.github/SECURITY.md)
[![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-blue.svg)](.github/CONTRIBUTING.md)

> **⚠️ PUBLIC REPOSITORY NOTICE:** This is a public repository. All code is visible to everyone. See [Security Policy](.github/SECURITY.md) for access control information.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation-setup)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [CI/CD with GitHub Actions](#cicd-with-github-actions)
- [Test Reports](#test-reports)
- [Framework Architecture](#framework-architecture)
- [Best Practices](#best-practices-implemented)
- [Troubleshooting](#troubleshooting)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Project Overview

This automation framework is designed to test the [NopCommerce Demo Store](https://demo.nopcommerce.com/), demonstrating industry-standard automation practices. The framework follows the Page Object Model design pattern and includes comprehensive test coverage for key e-commerce functionalities.

**Key Testing Areas:**
- User Registration & Login
- Product Search & Filtering
- Shopping Cart Operations
- Homepage Navigation
- Form Validations

## ✨ Features

- **Page Object Model (POM)**: Clean separation of test logic and page elements
- **Multi-Browser Support**: Chrome, Firefox, and Edge browsers
- **Headless Execution**: Run tests in headless mode for CI/CD pipelines
- **Parallel Execution**: Run tests in parallel for faster execution
- **Detailed Logging**: Log4j2 integration for comprehensive test logging
- **Screenshot on Failure**: Automatic screenshot capture for failed tests
- **Extent Reports**: Beautiful HTML reports with test execution details
- **TestNG Integration**: Powerful test configuration and execution management
- **Thread-Safe Design**: ThreadLocal pattern for parallel test execution
- **Configurable Timeouts**: Customizable implicit, explicit, and page load timeouts
- **Retry Mechanism**: Automatic retry for failed tests
- **Cross-Platform**: Works on Windows, macOS, and Linux

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11 | Programming Language |
| Selenium WebDriver | 4.15.0 | Browser Automation |
| TestNG | 7.8.0 | Test Framework |
| Maven | 3.x | Build & Dependency Management |
| Log4j2 | 2.21.1 | Logging Framework |
| ExtentReports | 5.1.1 | Test Reporting |
| WebDriverManager | 5.6.2 | Automatic Driver Management |
| Apache POI | 5.2.5 | Excel Data Handling |
| Commons IO | 2.15.0 | File Operations |

## 📁 Project Structure

```
ecommerce-web-automated-test-demo/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nopcommerce/
│   │   │       ├── base/
│   │   │       │   └── BasePage.java          # Base page with common methods
│   │   │       ├── pages/
│   │   │       │   ├── HomePage.java          # Home page object
│   │   │       │   ├── LoginPage.java         # Login page object
│   │   │       │   ├── RegistrationPage.java  # Registration page object
│   │   │       │   └── SearchPage.java        # Search page object
│   │   │       ├── utils/
│   │   │       │   ├── ConfigReader.java      # Configuration reader
│   │   │       │   ├── DriverManager.java     # WebDriver management
│   │   │       │   ├── ExcelReader.java       # Excel data reader
│   │   │       │   ├── ExtentManager.java     # Extent report manager
│   │   │       │   ├── ScreenshotUtil.java    # Screenshot utilities
│   │   │       │   └── WaitHelper.java        # Explicit wait utilities
│   │   │       └── listeners/
│   │   │           └── TestListener.java      # TestNG listener
│   │   └── resources/
│   │       ├── config.properties              # Configuration file
│   │       ├── log4j2.xml                     # Log4j2 configuration
│   │       └── logging.properties             # Java logging configuration
│   │
│   └── test/
│       ├── java/
│       │   └── com/nopcommerce/
│       │       ├── base/
│       │       │   └── BaseTest.java          # Base test class
│       │       └── tests/
│       │           ├── HomePageTest.java      # Home page tests
│       │           ├── LoginTest.java         # Login tests
│       │           ├── RegistrationTest.java  # Registration tests
│       │           └── SearchTest.java        # Search tests
│       └── resources/
│           └── testng.xml                     # TestNG suite configuration
│
├── logs/                                       # Test execution logs
├── screenshots/                                # Test failure screenshots
├── reports/                                    # HTML test reports
├── pom.xml                                     # Maven configuration
└── README.md                                   # Project documentation
```

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   ```

2. **Apache Maven 3.6 or higher**
   ```bash
   mvn -version
   ```

3. **Git** (for cloning the repository)
   ```bash
   git --version
   ```

4. **IDE** (Optional but recommended)
   - IntelliJ IDEA
   - Eclipse
   - Visual Studio Code

## 🚀 Quick Start Guide

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/ecommerce-web-automated-test-demo.git
cd ecommerce-web-automated-test-demo
```

### 2. Install Dependencies

```bash
mvn clean install
```

This will:
- Download all Maven dependencies
- Compile the source code
- Run all tests (skip with `-DskipTests` if needed)

### 3. Run Your First Test

```bash
# Run a single test
mvn clean test -Dtest=LoginTest

# Run all tests
mvn clean test

# Run tests with specific browser
mvn test -Dbrowser=chrome

# Run tests in headless mode
mvn test -Dheadless=true
```

### 4. View Test Reports

After test execution, open the HTML report:
```bash
# macOS
open reports/TestReport_*.html

# Windows
start reports/TestReport_*.html

# Linux
xdg-open reports/TestReport_*.html
```

## ⚙️ Configuration

### Application Configuration (config.properties)

Located at: `src/main/resources/config.properties`

```properties
# Application Configuration
app.url=https://demo.nopcommerce.com/
app.title=nopCommerce demo store

# Browser Configuration
browser=chrome                     # Options: chrome, firefox, edge, safari
headless=false                     # Set to true for headless execution
implicit.wait=10                   # Implicit wait in seconds
explicit.wait=20                   # Explicit wait in seconds
page.load.timeout=30               # Page load timeout in seconds

# Execution Configuration
parallel.execution=true            # Enable parallel test execution
execution.platform=LOCAL           # Options: LOCAL, LAMBDATEST
thread.count=3                     # Number of parallel threads
retry.failed.tests=1               # Number of retry attempts for failed tests

# Screenshot Configuration
screenshot.on.failure=true         # Capture screenshot on test failure
screenshot.path=./screenshots/     # Screenshot storage path

# Report Configuration
report.path=./reports/             # Test report storage path
report.name=NopCommerce_Test_Report

# Test Data
test.email=testuser@example.com
test.password=Test@123
```

### Browser Capabilities Configuration

The framework supports YAML-based dynamic browser capability management for both local and cloud (LambdaTest) execution.

**Configuration Files Location:** `src/main/resources/capabilities/`

**Available Configurations:**
- `chrome_local.yaml` - Chrome browser for local execution
- `chrome_lambdatest.yaml` - Chrome browser for LambdaTest cloud
- `firefox_local.yaml` - Firefox browser for local execution
- `firefox_lambdatest.yaml` - Firefox browser for LambdaTest cloud
- `edge_local.yaml` - Edge browser for local execution
- `edge_lambdatest.yaml` - Edge browser for LambdaTest cloud
- `safari_local.yaml` - Safari browser for local execution (macOS only)
- `safari_lambdatest.yaml` - Safari browser for LambdaTest cloud

### Changing Browser

**Option 1: Update config.properties**
```properties
browser=chrome    # For Chrome
browser=firefox   # For Firefox
browser=edge      # For Edge
```

**Option 2: Pass as Maven parameter**
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox -Dheadless=true
```

**Option 3: Cloud Execution (LambdaTest)**
```bash
# Set environment variables
export LT_USERNAME="your_username"
export LT_ACCESS_KEY="your_access_key"

# Run tests
mvn test -Dbrowser=chrome -Dexecution.platform=LAMBDATEST
```

## 🏃 Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Class

```bash
mvn test -Dtest=LoginTest
```

### Run Specific Test Method

```bash
mvn test -Dtest=LoginTest#testLoginWithInvalidCredentials
```

### Run Tests in Headless Mode

```bash
mvn test -Dheadless=true
```

### Run Tests with Specific Browser

```bash
mvn test -Dbrowser=chrome
```

### Run Tests Using TestNG XML

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run Tests in Parallel

Edit `testng.xml` to enable parallel execution:

```xml
<suite name="Test Suite" parallel="methods" thread-count="3">
    <!-- test configuration -->
</suite>
```

## 🎯 Enhanced Data-Driven Testing

This framework implements **industry best practices** for data-driven testing with externalized test data and centralized DataProvider utilities.

### Key Features

- ✅ **Externalized Test Data**: Test data stored in JSON/CSV files, not hardcoded
- ✅ **Centralized DataProviders**: Reusable data provider utility classes
- ✅ **Type-Safe Models**: POJO classes for test data representation
- ✅ **Separation of Concerns**: Test logic separated from test data
- ✅ **Easy Maintenance**: Non-technical users can modify JSON/CSV files
- ✅ **Version Control**: Test data tracked separately from code

### Architecture

```
src/test/
├── java/com/nopcommerce/
│   ├── testdata/
│   │   ├── models/
│   │   │   └── NavigationMenuTestData.java      # POJO for test data
│   │   ├── readers/
│   │   │   ├── JsonDataReader.java              # JSON data reader utility
│   │   │   └── CsvDataReader.java               # CSV data reader utility
│   │   └── providers/
│   │       └── NavigationMenuDataProvider.java  # Centralized DataProviders
│   └── tests/
│       └── NavigationMenuEnhancedTest.java      # Data-driven tests
└── resources/testdata/
    ├── navigation-menu-data.json                # Test data (JSON)
    ├── navigation-menu-data.csv                 # Test data (CSV)
    └── navigation-menu-negative-data.json       # Negative test data
```

### Test Data Example (JSON)

```json
[
  {
    "mainMenu": "Computers",
    "subMenu": "Desktops",
    "expectedUrl": "desktops",
    "description": "Computers → Desktops"
  },
  {
    "mainMenu": "Books",
    "subMenu": null,
    "expectedUrl": "books",
    "description": "Books main menu"
  }
]
```

### Using Centralized DataProvider

```java
@Test(dataProvider = "navigationMenuDataFromJson",
      dataProviderClass = NavigationMenuDataProvider.class)
public void testNavigationFromJson(String mainMenu, String subMenu, 
                                   String expectedUrl, String description) {
    navigationMenu.navigateTo(mainMenu, subMenu);
    Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl));
}
```

### Benefits

**Before** (Hardcoded Data):
```java
@DataProvider
public Object[][] getData() {
    return new Object[][] {
        {"Books", null, "books"},
        {"Computers", "Desktops", "desktops"},
        // ... hardcoded in test class
    };
}
```

**After** (External Data):
```java
// Data in JSON file - edit without recompiling!
@Test(dataProvider = "navigationMenuDataFromJson",
      dataProviderClass = NavigationMenuDataProvider.class)
public void testNavigation(...) { ... }
```

**Advantages**:
- ✅ No recompilation needed to change test data
- ✅ Business analysts can update test data
- ✅ Same DataProvider reusable across multiple test classes
- ✅ Support for multiple formats (JSON, CSV, future: Excel, Database)

### Adding New Test Data

Simply edit the JSON file:

```json
{
  "mainMenu": "NewCategory",
  "subMenu": "NewSubMenu",
  "expectedUrl": "new-url",
  "description": "New test scenario"
}
```

Run tests - **no code changes needed!**

## 🧪 Navigation Menu Testing

Comprehensive navigation menu testing with 16 test scenarios covering all menu paths.

### Test Coverage

| Category | Scenarios | Description |
|----------|-----------|-------------|
| Main Menus | 4 | Books, Digital downloads, Jewelry, Gift Cards |
| Computers Submenus | 3 | Desktops, Notebooks, Software |
| Electronics Submenus | 2 | Camera & photo, Cell phones |
| Apparel Submenus | 3 | Shoes, Clothing, Accessories |
| Negative Tests | 4 | Invalid menu/submenu handling |

### Running Navigation Tests

```bash
# Run all navigation tests
mvn test -Dtest=NavigationMenuEnhancedTest

# Run specific test method
mvn test -Dtest=NavigationMenuEnhancedTest#testNavigationFromJson

# Run category-specific tests
mvn test -Dtest=NavigationMenuEnhancedTest#testComputersSubmenu
```

### Available DataProviders

| DataProvider | Source | Scenarios | Purpose |
|--------------|--------|-----------|---------|
| `navigationMenuDataFromJson` | JSON | 12 | All menus ⭐ Recommended |
| `navigationMenuDataFromCsv` | CSV | 12 | Alternative format |
| `mainMenuOnlyData` | JSON (filtered) | 4 | Main menus only |
| `submenuOnlyData` | JSON (filtered) | 8 | Submenus only |
| `computersSubmenuData` | JSON (filtered) | 3 | Computers category |
| `electronicsSubmenuData` | JSON (filtered) | 2 | Electronics category |
| `apparelSubmenuData` | JSON (filtered) | 3 | Apparel category |
| `invalidNavigationMenuData` | JSON | 4 | Negative tests |

## 🏗 Framework Architecture

### Page Object Model (POM)

This framework implements the Page Object Model design pattern with enhanced separation of concerns.

### Enhanced BasePage Architecture

**Before Refactoring**:
```java
public class BasePage {
    // Page infrastructure + Element interactions mixed
    protected void click(WebElement element) { ... }
    protected void type(WebElement element, String text) { ... }
    // ... 15+ methods
}
```

**After Refactoring** (Best Practice):
```
BasePage (Page Infrastructure)
    ↓ delegates to
WebElementActions (Element Interactions)
```

**Benefits**:
- ✅ **Single Responsibility**: BasePage handles page initialization only
- ✅ **Reusability**: WebElementActions can be used anywhere, not just in page objects
- ✅ **Maintainability**: Changes to element interactions in one place
- ✅ **Testability**: Can unit test WebElementActions independently

### WebElementActions Utility

**Location**: `src/main/java/com/nopcommerce/utils/WebElementActions.java`

**Key Methods**:
```java
// Basic interactions with enhanced logging
WebElementActions.click(driver, element, "Login Button");
WebElementActions.type(driver, field, "text", "Email Field");

// Dropdown operations
WebElementActions.selectByVisibleText(driver, dropdown, "Option");
WebElementActions.selectByValue(driver, dropdown, "value");

// Advanced operations
WebElementActions.scrollToElement(driver, element);
WebElementActions.clickUsingJavaScript(driver, element);
WebElementActions.highlightElement(driver, element);
WebElementActions.moveToElement(driver, element);  // For hover actions
```

**Features**:
- Built-in explicit waits
- Comprehensive error handling
- Optional element names for better logging
- JavaScript execution support
- Thread-safe (stateless design)

### Project Structure (Enhanced)

```
src/
├── main/java/com/nopcommerce/
│   ├── base/
│   │   └── BasePage.java                    # Page infrastructure only
│   ├── pages/
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── RegisterPage.java
│   │   ├── NavigationMenu.java              # Dynamic menu navigation
│   │   ├── SearchResultsPage.java
│   │   ├── ShoppingCartPage.java
│   │   └── AccountPage.java
│   ├── utils/
│   │   ├── WebElementActions.java           # ⭐ Centralized element interactions
│   │   ├── DriverManager.java
│   │   ├── ConfigReader.java
│   │   ├── WaitUtil.java
│   │   └── ScreenshotUtil.java
│   └── listeners/
│       ├── TestListener.java
│       └── ExtentReportManager.java
│
├── test/java/com/nopcommerce/
│   ├── base/
│   │   └── BaseTest.java
│   ├── testdata/
│   │   ├── models/
│   │   │   └── NavigationMenuTestData.java  # ⭐ POJO for test data
│   │   ├── readers/
│   │   │   ├── JsonDataReader.java          # ⭐ JSON data reader
│   │   │   └── CsvDataReader.java           # ⭐ CSV data reader
│   │   └── providers/
│   │       └── NavigationMenuDataProvider.java  # ⭐ Centralized DataProviders
│   └── tests/
│       ├── LoginTest.java
│       ├── RegistrationTest.java
│       ├── SearchTest.java
│       ├── HomePageTest.java
│       └── NavigationMenuEnhancedTest.java  # ⭐ Data-driven tests
│
└── test/resources/
    ├── testdata/
    │   ├── navigation-menu-data.json        # ⭐ External test data
    │   ├── navigation-menu-data.csv
    │   └── navigation-menu-negative-data.json
    ├── testng.xml
    └── config.properties
```

### Design Patterns Implemented

1. **Page Object Model (POM)**: Encapsulates page elements and behaviors
2. **Singleton Pattern**: ConfigReader, DriverManager
3. **Factory Pattern**: DriverManager for browser initialization
4. **Utility/Helper Pattern**: WebElementActions, WaitUtil
5. **Delegation Pattern**: BasePage delegates to WebElementActions
6. **Data Transfer Object (DTO)**: NavigationMenuTestData POJO
7. **Strategy Pattern**: Multiple DataProvider strategies (JSON, CSV)

## 📊 Test Reports

### Extent Reports

Beautiful HTML reports are generated after each test execution.

**Location**: `reports/TestReport_<timestamp>.html`

**Features**:
- Test execution summary with pass/fail counts
- Detailed test steps with timestamps
- Screenshots for failed tests
- Browser and OS information
- Execution time metrics
- Interactive dashboard

**View Reports**:
```bash
# After running tests
open reports/TestReport_*.html  # macOS
start reports/TestReport_*.html # Windows
xdg-open reports/TestReport_*.html # Linux
```

### TestNG Reports

Default TestNG reports are also generated.

**Location**: `test-output/index.html`

### Console Logs

Detailed logs are displayed in the console with:
- Test execution flow
- Element interactions
- Navigation steps
- Assertion results
- Error messages with stack traces

**Enhanced Log Readability**: Visual separation between test executions with empty lines for better distinction.

## 🔍 Best Practices Implemented

### Code Quality

- ✅ **SOLID Principles**: Single Responsibility, Open/Closed, Dependency Inversion
- ✅ **DRY Principle**: No code duplication
- ✅ **Separation of Concerns**: Test logic, page logic, and utilities separated
- ✅ **Type Safety**: Strong typing with POJOs
- ✅ **Clean Code**: Meaningful names, small methods, clear intent

### Testing Best Practices

- ✅ **Data-Driven Testing**: External test data in JSON/CSV
- ✅ **Page Object Model**: Clean separation of concerns
- ✅ **Explicit Waits**: No Thread.sleep(), proper synchronization
- ✅ **Independent Tests**: Each test can run independently
- ✅ **Descriptive Assertions**: Clear assertion messages
- ✅ **Negative Testing**: Error scenarios covered

### Framework Best Practices

- ✅ **Centralized Utilities**: Reusable utility classes
- ✅ **Configuration Management**: External config.properties
- ✅ **Logging**: Comprehensive logging at all levels
- ✅ **Screenshot on Failure**: Automatic failure evidence
- ✅ **Thread-Safe**: ThreadLocal for parallel execution
- ✅ **Version Control**: Proper .gitignore, no sensitive data

### Industry Standards

This framework follows patterns used by:
- **Google** - External test data management
- **Microsoft** - Centralized DataProviders
- **Amazon** - Page Object Model with utilities
- **Netflix** - Data-driven with type-safe models

## 🛠 Framework Components

### 1. BasePage

**Purpose**: Foundation for all Page Object classes

**Responsibilities**:
- WebDriver instance management
- PageFactory initialization
- Page-level utilities (getTitle, getCurrentUrl)
- Delegates element interactions to WebElementActions

### 2. WebElementActions Utility

**Purpose**: Centralized element interaction methods

**Features**:
- 20+ reusable methods
- Built-in waits and error handling
- Enhanced logging with element names
- JavaScript execution support
- Stateless design for thread-safety

### 3. NavigationMenu Page

**Purpose**: Dynamic menu navigation with hover support

**Features**:
- Parameterized XPath for flexibility
- Hover action for dropdown menus
- Comprehensive error handling
- Support for main menus and submenus

**Usage**:
```java
navigationMenu.navigateTo("Computers", "Desktops");  // Hover + click
navigationMenu.navigateTo("Books", null);             // Direct click
```

### 4. DataProvider Utilities

**Purpose**: External test data management

**Components**:
- **JsonDataReader**: Read JSON test data
- **CsvDataReader**: Read CSV test data
- **NavigationMenuDataProvider**: Centralized DataProviders
- **NavigationMenuTestData**: POJO model

**Benefits**:
- No hardcoded test data
- Easy to maintain and extend
- Reusable across test classes
- Non-technical users can modify data

This framework is fully integrated with GitHub Actions for continuous integration and continuous delivery. Tests are automatically executed on every push and pull request.

### Available Workflows

#### 1. Main CI/CD Pipeline (`test-automation.yml`)

**Triggers:**
- Push to `main`, `master`, or `develop` branches
- Pull requests to `main`, `master`, or `develop` branches
- Manual trigger with custom parameters

**Features:**
- ✅ Runs tests on multiple browsers (Chrome, Firefox)
- ✅ Matrix strategy for parallel execution on Ubuntu, Windows, and macOS
- ✅ Automatic browser installation
- ✅ Maven dependency caching for faster builds
- ✅ Test report and screenshot uploads as artifacts
- ✅ TestNG results publishing
- ✅ Code quality checks

**Manual Trigger:**
```bash
# Go to Actions tab in GitHub → Select "E-Commerce Test Automation CI/CD" → Run workflow
# Choose browser (chrome/firefox/edge) and headless mode (true/false)
```

#### 2. Scheduled Smoke Tests (`scheduled-tests.yml`)

**Triggers:**
- Automatic daily runs at 2 AM UTC
- Manual trigger

**Features:**
- ✅ Daily smoke test execution
- ✅ Automatic failure notifications
- ✅ 30-day artifact retention
- ✅ Runs on latest Chrome in headless mode

#### 3. Dependency Check (`dependency-check.yml`)

**Triggers:**
- Weekly on Monday at 9 AM UTC
- When `pom.xml` is modified
- Manual trigger

**Features:**
- ✅ Dependency tree analysis
- ✅ Check for dependency updates
- ✅ Check for plugin updates
- ✅ Dependency conflict detection

### Viewing Test Results

1. **In GitHub Actions:**
   - Go to the "Actions" tab in your repository
   - Click on any workflow run
   - View logs, test results, and download artifacts

2. **Artifacts Include:**
   - HTML test reports (Extent Reports)
   - Screenshots of failed tests
   - Detailed execution logs
   - TestNG XML reports

3. **Test Reports:**
   - Automatically published as GitHub Actions artifacts
   - Available for download for 30 days
   - Viewable in the Actions summary page

### Setting Up GitHub Actions

1. **Push your code to GitHub:**
   ```bash
   git add .
   git commit -m "Add GitHub Actions CI/CD"
   git push origin main
   ```

2. **Enable Actions:**
   - GitHub Actions are automatically enabled for public repositories
   - For private repos, go to Settings → Actions → Enable Actions

3. **Update Badge URLs:**
   - Replace `yourusername` in README badges with your GitHub username
   - Replace `ecommerce-web-automated-test-demo` with your repository name

4. **Secrets Configuration (Optional):**
   - Go to Settings → Secrets and variables → Actions
   - Add any sensitive configuration (if needed for future enhancements)

### Running Tests Locally vs CI/CD

| Feature | Local Execution | GitHub Actions CI/CD |
|---------|----------------|----------------------|
| Browser | Any (Chrome/Firefox/Edge) | Chrome/Firefox |
| OS | Your machine | Ubuntu/Windows/macOS |
| Headless | Optional | Default: Yes |
| Parallel | TestNG config | Matrix strategy |
| Reports | Local directory | GitHub Artifacts |
| Scheduling | Manual | Automated (daily) |

## 📊 Test Reports

### Extent Reports

After test execution, HTML reports are generated in the `reports/` directory:

```
reports/
└── TestReport_YYYY-MM-DD_HH-MM-SS.html
```

Open the HTML file in any browser to view:
- Test execution summary
- Pass/Fail statistics
- Detailed test steps
- Screenshots for failed tests
- Execution timeline
- Environment details

### Log Files

Detailed logs are stored in the `logs/` directory:

```
logs/
└── automation.log
```

Logs include:
- Test execution flow
- Browser actions
- Element interactions
- Error messages and stack traces

### Screenshots

Failed test screenshots are saved in the `screenshots/` directory:

```
screenshots/
└── testName_YYYYMMDD_HHMMSS.png
```

## 🏗 Framework Architecture

### Design Patterns

1. **Page Object Model (POM)**
   - Encapsulates page elements and actions
   - Improves code maintainability and reusability
   - Reduces code duplication

2. **Singleton Pattern**
   - Used in ConfigReader for single instance
   - Ensures consistent configuration across tests

3. **ThreadLocal Pattern**
   - Used in DriverManager for thread safety
   - Enables parallel test execution

### Key Components

#### 1. BasePage
- Contains common methods used across all pages
- Handles element interactions (click, type, wait, etc.)
- Provides reusable utilities

#### 2. BaseTest
- Setup and teardown for all test classes
- WebDriver initialization
- Screenshot capture on failure
- Logging setup

#### 3. DriverManager
- Manages WebDriver lifecycle
- Supports multiple browsers
- Thread-safe implementation
- Automatic driver setup using WebDriverManager

#### 4. ConfigReader
- Reads configuration from properties file
- Provides singleton access to configuration
- Type-safe configuration methods

#### 5. TestListener
- Implements TestNG ITestListener
- Enhances reporting with Extent Reports
- Captures test execution events

## 📝 Best Practices Implemented

1. **Separation of Concerns**: Test logic separated from page logic
2. **DRY Principle**: Reusable methods in base classes
3. **Explicit Waits**: Smart waits for element availability
4. **Logging**: Comprehensive logging at all levels
5. **Exception Handling**: Graceful error handling
6. **Configuration Management**: Externalized configuration
7. **Thread Safety**: ThreadLocal pattern for parallel execution
8. **Clean Code**: Meaningful names and proper documentation
9. **Version Control**: Git-friendly structure
10. **CI/CD Ready**: Maven-based execution

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. WebDriver Not Found

**Issue**: `Cannot find chrome/firefox driver`

**Solution**: WebDriverManager automatically downloads drivers. Ensure internet connectivity.

#### 2. Element Not Found

**Issue**: `NoSuchElementException`

**Solution**: 
- Increase implicit wait in config.properties
- Add explicit wait in the page object
- Verify the locator strategy

#### 3. Stale Element Reference

**Issue**: `StaleElementReferenceException`

**Solution**: 
- Re-locate the element before interaction
- Use explicit waits
- Refresh the page object

#### 4. Tests Failing in Headless Mode

**Issue**: Tests pass in normal mode but fail in headless

**Solution**:
- Add viewport size configuration
- Check for timing issues
- Verify element visibility in headless mode

#### 5. Parallel Execution Issues

**Issue**: Tests interfere with each other

**Solution**:
- Ensure thread-safe design (ThreadLocal)
- Use unique test data
- Avoid shared resources

## 🔒 Security

### Repository Access Control

This is a **PUBLIC REPOSITORY**. Here's what that means:

| Who | Can Do | Cannot Do |
|-----|--------|-----------|
| **Anyone** | ✅ View code<br>✅ Fork repository<br>✅ Submit Pull Requests<br>✅ Open Issues | ❌ Push directly to branches<br>❌ Merge Pull Requests<br>❌ Modify settings |
| **Repository Owner** | ✅ All of the above<br>✅ Merge PRs<br>✅ Manage settings | - |

### Branch Protection

The `main` and `develop` branches are **protected**:

- ✅ All changes require Pull Request review
- ✅ CI/CD tests must pass before merge
- ✅ Code owner approval required
- ❌ No direct pushes allowed (even for owner)
- ❌ No force pushes allowed
- ❌ No branch deletion allowed

### What Happens If Someone Tries to Push?

If someone tries to push changes directly to protected branches:

```bash
git push origin main
# Result: ❌ REJECTED
# Error: GH006: Protected branch update failed
# Required: Pull Request with approval
```

**The ONLY way to add code:**
1. Fork the repository
2. Create feature branch
3. Submit Pull Request
4. Pass automated tests
5. Get owner approval
6. Owner merges the PR

### Security Documentation

- 📋 [**Security Policy**](.github/SECURITY.md) - Access control details
- 📝 [**Contributing Guidelines**](.github/CONTRIBUTING.md) - How to contribute
- 🛡️ [**Branch Protection Guide**](.github/BRANCH_PROTECTION_GUIDE.md) - Setup instructions
- 👥 [**Code Owners**](.github/CODEOWNERS) - Review requirements

### Quick Security Setup

**⚠️ IMPORTANT:** After cloning, set up branch protection immediately:

1. Go to: **Settings → Branches → Add branch protection rule**
2. Branch pattern: `main`
3. Enable:
   - ✅ Require pull request reviews (1 approval)
   - ✅ Require status checks to pass
   - ✅ Require conversation resolution
   - ✅ Restrict who can push (no exceptions)
   - ✅ Do not allow force pushes
   - ✅ Do not allow deletions

**Full setup guide:** [Branch Protection Guide](.github/BRANCH_PROTECTION_GUIDE.md)

## 🤝 Contributing

We welcome contributions! Here's how to get started:

### Quick Start for Contributors

1. **Fork the Repository**
   ```bash
   # Click "Fork" on GitHub, then:
   git clone https://github.com/YOUR-USERNAME/ecommerce-web-automated-test-demo.git
   cd ecommerce-web-automated-test-demo
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make Your Changes**
   - Write clean, well-documented code
   - Add tests for new features
   - Update documentation

4. **Test Locally**
   ```bash
   mvn clean test
   mvn test -Dbrowser=chrome -Dheadless=true
   mvn test -Dbrowser=firefox -Dheadless=true
   ```

5. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "feat: add amazing feature"
   ```

6. **Push to Your Fork**
   ```bash
   git push origin feature/amazing-feature
   ```

7. **Create Pull Request**
   - Go to original repository on GitHub
   - Click "New Pull Request"
   - Fill out the PR template
   - Wait for review and approval

### Contribution Guidelines

📖 **Read First:** [Contributing Guidelines](.github/CONTRIBUTING.md)

**PR Requirements:**
- [ ] Tests pass in CI/CD pipeline
- [ ] Code follows project conventions
- [ ] Documentation updated
- [ ] No merge conflicts
- [ ] PR template filled out completely

**Commit Message Format:**
```
type(scope): subject

Examples:
feat(login): add remember me functionality
fix(search): resolve null pointer exception
docs(readme): update installation instructions
test(cart): add shopping cart tests
```

### Code Standards

- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Use Page Object Model pattern
- Write descriptive test method names
- Include proper assertions
- Keep methods focused and single-purpose

### What We're Looking For

✅ **Accepted:**
- Bug fixes with tests
- New test scenarios
- Page Objects for new pages
- Utility enhancements
- Documentation improvements
- Performance optimizations

❌ **Not Accepted:**
- Breaking changes without discussion
- Code without tests
- Malicious code
- Changes to CI/CD without permission
- PRs that fail tests

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Subramanyam Kongani**

- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)

## 🙏 Acknowledgments

- [Selenium](https://www.selenium.dev/) - Web automation framework
- [TestNG](https://testng.org/) - Testing framework
- [NopCommerce](https://demo.nopcommerce.com/) - Demo application
- [ExtentReports](https://www.extentreports.com/) - Reporting library

## 📞 Support

For questions or issues:
- Open an issue on GitHub
- Email: your.email@example.com

---

⭐ **If you find this project helpful, please give it a star!** ⭐

---

**Last Updated**: November 2025
