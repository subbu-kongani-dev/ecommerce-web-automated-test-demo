# GitHub Actions CI/CD Setup Guide

## 📋 Overview

This document provides detailed information about the GitHub Actions CI/CD pipelines configured for this automation framework.

## 🔧 Workflow Files

### 1. test-automation.yml
**Purpose:** Main CI/CD pipeline for automated testing

**Jobs:**
- `test` - Runs on Ubuntu with Chrome and Firefox browsers
- `test-windows` - Runs on Windows (triggered on main branch or manually)
- `test-macos` - Runs on macOS (triggered on main branch or manually)
- `code-quality` - Performs code quality checks
- `notify` - Provides build status summary

**Matrix Strategy:**
```yaml
matrix:
  os: [ubuntu-latest]
  browser: [chrome, firefox]
  java: [11]
```

### 2. scheduled-tests.yml
**Purpose:** Daily automated smoke tests

**Schedule:** Runs daily at 2:00 AM UTC

**Benefits:**
- Early detection of application issues
- Regular health checks of test suite
- Automated monitoring without manual intervention

### 3. dependency-check.yml
**Purpose:** Weekly dependency and security checks

**Schedule:** Runs every Monday at 9:00 AM UTC

**Checks:**
- Dependency tree analysis
- Available dependency updates
- Available plugin updates
- Dependency conflicts

## 🚀 Getting Started

### Initial Setup

1. **Ensure GitHub Repository:**
   ```bash
   # If not already initialized
   git init
   git add .
   git commit -m "Initial commit with GitHub Actions"
   git branch -M main
   git remote add origin https://github.com/yourusername/ecommerce-web-automated-test-demo.git
   git push -u origin main
   ```

2. **Verify Workflows:**
   - Navigate to your repository on GitHub
   - Click on the "Actions" tab
   - You should see all three workflows listed

3. **First Run:**
   - Push any commit to trigger the CI/CD pipeline
   - Or manually trigger from the Actions tab

### Manual Workflow Execution

1. Go to your repository on GitHub
2. Click "Actions" tab
3. Select the workflow you want to run
4. Click "Run workflow" button
5. (For test-automation.yml) Choose browser and headless mode
6. Click "Run workflow" to start

## 📊 Understanding Workflow Results

### Successful Run
- ✅ Green checkmark indicates all tests passed
- Artifacts are available for download
- Test reports can be viewed in artifacts

### Failed Run
- ❌ Red X indicates test failures or build issues
- Check logs for detailed error messages
- Screenshots of failures are in artifacts
- Review the "Summary" page for quick overview

### Artifacts

Each workflow run produces artifacts:

**Test Reports:**
- `test-reports-chrome-ubuntu-latest/`
- `test-reports-firefox-ubuntu-latest/`
- `test-reports-windows/`
- `test-reports-macos/`

**Failure Screenshots:**
- `failure-screenshots-{browser}-{os}/`

**Retention:** 30 days

## 🔐 Secrets and Variables (Optional)

For future enhancements, you can add secrets:

1. Go to Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Add secrets like:
   - `TEST_EMAIL` - Test user email
   - `TEST_PASSWORD` - Test user password
   - `SLACK_WEBHOOK` - For notifications
   - `JIRA_API_TOKEN` - For issue tracking

Usage in workflow:
```yaml
env:
  TEST_EMAIL: ${{ secrets.TEST_EMAIL }}
  TEST_PASSWORD: ${{ secrets.TEST_PASSWORD }}
```

## 🎯 Best Practices

### 1. Branch Protection
- Require status checks before merging
- Require CI/CD to pass before merge
- Enable "Require branches to be up to date"

### 2. Caching
- Maven dependencies are cached automatically
- Reduces build time significantly
- Cache key: `${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}`

### 3. Parallel Execution
- Tests run in parallel across browsers
- Reduces overall execution time
- Matrix strategy handles coordination

### 4. Artifact Management
- Keep retention period appropriate (currently 30 days)
- Download critical reports before expiration
- Use for debugging and historical analysis

## 🐛 Troubleshooting

### Issue: Workflow Not Triggering

**Solution:**
- Check branch names match workflow triggers
- Ensure Actions are enabled in repository settings
- Verify `.github/workflows/` files are in main branch

### Issue: Tests Failing in CI but Pass Locally

**Possible Causes:**
- Timing issues (use explicit waits)
- Headless mode differences (add headless-specific configs)
- Environment differences (check OS-specific issues)

**Solutions:**
```bash
# Test locally in headless mode
mvn test -Dheadless=true

# Check browser versions
chrome --version
firefox --version
```

### Issue: Browser Installation Failed

**Solution:**
- Workflow includes automatic browser installation
- If issues persist, check GitHub Actions status page
- Browser actions are maintained by github.com/browser-actions

### Issue: Out of Storage

**Solution:**
- Reduce artifact retention period
- Clean up old workflow runs
- Optimize screenshot capture (only on failure)

## 📈 Monitoring and Notifications

### Current Setup
- Build status visible in Actions tab
- Badges in README show latest status
- Artifacts available for review

### Future Enhancements
- Add Slack/Email notifications
- Integrate with test management tools
- Set up dashboards for trends

### Example Slack Notification
```yaml
- name: Slack Notification
  if: failure()
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "❌ Tests failed in ${{ github.repository }}"
      }
```

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

## 🔄 Updating Workflows

To modify workflows:

1. Edit `.github/workflows/*.yml` files
2. Test changes in a feature branch
3. Review workflow syntax using GitHub's validator
4. Merge to main after verification

## 📊 Metrics to Monitor

- **Pass Rate:** Percentage of successful test runs
- **Execution Time:** Duration of test runs
- **Flaky Tests:** Tests that intermittently fail
- **Coverage:** Areas of application tested

## 🎓 Learning Resources

- GitHub Actions marketplace for additional actions
- Community forums for troubleshooting
- Sample workflows in awesome-actions repository
