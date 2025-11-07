# E-Commerce Web Automated Test Demo

A comprehensive, scalable, and maintainable web automation testing framework built with Selenium WebDriver, TestNG, and the Page Object Model (POM) design pattern. This framework is designed for testing e-commerce applications with a focus on best practices and enterprise-level patterns.

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.15.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-blue.svg)](https://maven.apache.org/)
[![CI/CD](https://github.com/yourusername/ecommerce-web-automated-test-demo/workflows/E-Commerce%20Test%20Automation%20CI/CD/badge.svg)](https://github.com/yourusername/ecommerce-web-automated-test-demo/actions)
[![Scheduled Tests](https://github.com/yourusername/ecommerce-web-automated-test-demo/workflows/Scheduled%20Smoke%20Tests/badge.svg)](https://github.com/yourusername/ecommerce-web-automated-test-demo/actions)

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

## 🚀 Installation & Setup

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

### 3. Verify Installation

```bash
mvn clean test -Dtest=LoginTest
```

## ⚙️ Configuration

### config.properties

Located at: `src/main/resources/config.properties`

```properties
# Application Configuration
app.url=https://demo.nopcommerce.com/
app.title=nopCommerce demo store

# Browser Configuration
browser=firefox                    # Options: chrome, firefox, edge
headless=false                     # Set to true for headless execution
implicit.wait=10                   # Implicit wait in seconds
explicit.wait=20                   # Explicit wait in seconds
page.load.timeout=30               # Page load timeout in seconds

# Execution Configuration
parallel.execution=true            # Enable parallel test execution
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

### Changing Browser

To run tests on a different browser, update the `browser` property:

```properties
browser=chrome    # For Chrome
browser=firefox   # For Firefox
browser=edge      # For Edge
```

Or pass it as a Maven parameter:

```bash
mvn test -Dbrowser=chrome
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

## 🚀 CI/CD with GitHub Actions

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

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Standards

- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Write clean, readable code
- Include unit tests for utilities
- Update documentation for new features

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
