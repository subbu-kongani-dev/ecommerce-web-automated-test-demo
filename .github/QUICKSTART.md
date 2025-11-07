# Quick Start Guide for CI/CD

## 🚀 For Developers

### First Time Setup

1. **Clone and setup:**
   ```bash
   git clone <your-repo-url>
   cd ecommerce-web-automated-test-demo
   mvn clean install
   ```

2. **Run tests locally:**
   ```bash
   mvn test
   ```

3. **Push to GitHub:**
   ```bash
   git add .
   git commit -m "Your changes"
   git push origin main
   ```
   ✅ Tests will automatically run in GitHub Actions!

## 🔄 Workflows Overview

| Workflow | When it Runs | What it Does |
|----------|--------------|--------------|
| **CI/CD Pipeline** | Every push/PR | Runs all tests on Chrome & Firefox |
| **Scheduled Tests** | Daily at 2 AM UTC | Runs smoke tests automatically |
| **Dependency Check** | Weekly (Monday 9 AM) | Checks for outdated dependencies |

## 📋 Quick Commands

### Run tests with specific browser:
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
```

### Run tests in headless mode:
```bash
mvn test -Dheadless=true
```

### Run specific test class:
```bash
mvn test -Dtest=LoginTest
```

### Compile without running tests:
```bash
mvn clean compile -DskipTests
```

## 🎯 Best Practices

✅ **DO:**
- Write tests in headless mode compatible way
- Add explicit waits for dynamic elements
- Commit workflow files with your code
- Check CI/CD results before merging PRs
- Review test reports in GitHub Actions artifacts

❌ **DON'T:**
- Commit sensitive data (use GitHub Secrets)
- Ignore failing tests in CI/CD
- Push directly to main (use PRs)
- Disable workflows without reason

## 🐛 Troubleshooting

### Test fails in CI but works locally?
```bash
# Test locally in headless mode (same as CI)
mvn test -Dheadless=true -Dbrowser=chrome
```

### Need to re-run failed tests?
- Go to Actions tab → Click on failed run → Click "Re-run jobs"

### Want to run tests on specific OS?
- Manually trigger workflow → Select parameters

## 📊 Viewing Results

1. **GitHub Actions Tab:**
   - See all workflow runs
   - Download test reports & screenshots
   - View execution logs

2. **PR Checks:**
   - Status checks appear on pull requests
   - Must pass before merge (if branch protection enabled)

3. **Badges:**
   - README shows current CI/CD status
   - Click badges to view latest runs

## 🔗 Important Links

- [Full CI/CD Guide](.github/CICD_GUIDE.md)
- [Main README](../README.md)
- [GitHub Actions Docs](https://docs.github.com/en/actions)

## ⚡ Pro Tips

1. **Use Draft PRs** for work in progress
2. **Enable notifications** for workflow failures
3. **Review artifacts** for debugging
4. **Keep workflows updated** with latest actions
5. **Monitor execution time** and optimize slow tests

## 📞 Need Help?

- Check the full [CI/CD Guide](.github/CICD_GUIDE.md)
- Review GitHub Actions logs
- Check TestNG reports in artifacts
- Consult team documentation
