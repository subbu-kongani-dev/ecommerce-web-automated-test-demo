# GitHub Actions - Finding Screenshots and Logs

## 📍 Where to Find Test Artifacts in GitHub Actions

### Quick Access Path:
```
GitHub Repository → Actions tab → Click on workflow run → Scroll to "Artifacts" section
```

## Step-by-Step Guide

### 1. **Navigate to Actions Tab**
1. Go to your GitHub repository: `https://github.com/YOUR_USERNAME/ecommerce-web-automated-test-demo`
2. Click on the **"Actions"** tab at the top

### 2. **Select Workflow Run**
You'll see a list of workflow runs. Each run shows:
- ✅ Green checkmark = Tests passed
- ❌ Red X = Tests failed
- 🟡 Yellow circle = Tests running

Click on any workflow run to see details.

### 3. **View Artifacts Section**
Scroll down to the **"Artifacts"** section at the bottom of the workflow run page.

You'll see separate downloadable artifacts:

#### 📸 **Screenshots**
```
screenshots-chrome-ubuntu-latest-123
screenshots-firefox-ubuntu-latest-123
```
- Contains all screenshots captured during test execution
- Includes screenshots from failed tests
- Named with test name and timestamp
- Format: `testName_YYYYMMDD_HHMMSS.png`

#### 📋 **Logs**
```
logs-chrome-ubuntu-latest-123
logs-firefox-ubuntu-latest-123
```
- Contains `automation.log` file
- Includes all test execution logs
- Shows INFO, WARN, ERROR messages
- Useful for debugging test failures

#### 📊 **Test Reports**
```
test-reports-chrome-ubuntu-latest-123
test-reports-firefox-ubuntu-latest-123
```
- Contains HTML test reports
- Format: `TestReport_YYYY-MM-DD_HH-MM-SS.html`
- Open in browser to view formatted results

#### 📈 **TestNG Results**
```
testng-results-chrome-ubuntu-latest-123
testng-results-firefox-ubuntu-latest-123
```
- Contains TestNG XML reports
- Includes `test-output/` directory
- Contains `surefire-reports/` for Maven results

## 🎯 Enhanced Features I've Added

### 1. **Separate Artifacts for Each Type**
Instead of one combined artifact, you now get:
- ✅ Individual artifacts for screenshots, logs, and reports
- ✅ Easier to download only what you need
- ✅ Clearer organization

### 2. **Run Number in Artifact Names**
Artifacts now include run number:
```
screenshots-chrome-ubuntu-latest-123
                                  ↑
                            Run number
```
- ✅ Easy to identify which run artifacts belong to
- ✅ No confusion between multiple runs

### 3. **Test Summary in GitHub UI**
After workflow runs, you'll see a summary showing:
```
## Test Artifacts Generated

✅ Test Reports: 3 file(s)
📸 Screenshots: 12 file(s)
📋 Log Files: 1 file(s)
📊 TestNG Output: Available
```

### 4. **Published Test Results**
Test results are published directly in GitHub:
- Navigate to workflow run
- Click on "Summary" tab
- See test results with pass/fail status
- No need to download artifacts to check basic results

## 📥 How to Download Artifacts

### Method 1: Manual Download (Browser)
1. Go to Actions → Select workflow run
2. Scroll to "Artifacts" section
3. Click on artifact name to download ZIP file
4. Extract ZIP to view files

### Method 2: GitHub CLI
```bash
# List artifacts for a run
gh run view RUN_ID --repo YOUR_USERNAME/REPO_NAME

# Download specific artifact
gh run download RUN_ID -n screenshots-chrome-ubuntu-latest-123

# Download all artifacts
gh run download RUN_ID
```

### Method 3: API (Automated)
```bash
# Get workflow run artifacts
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/YOUR_USERNAME/REPO_NAME/actions/runs/RUN_ID/artifacts
```

## 🔍 Viewing Different Artifact Types

### Screenshots
```
screenshots/
├── testSearchWithValidProduct_20251107_165129.png
├── testSearchWithInvalidProduct_20251107_165145.png
└── testSearchWithPartialName_20251107_165201.png
```

**How to View:**
1. Download artifact
2. Extract ZIP
3. Open PNG files with image viewer

### Logs
```
logs/
└── automation.log
```

**How to View:**
1. Download artifact
2. Extract ZIP
3. Open `automation.log` with text editor
4. Search for ERROR or WARN to find issues

**Example Log Entry:**
```log
2025-11-07 23:21:59 [main] INFO  BaseTest - === Test Setup Started ===
2025-11-07 23:22:01 [main] INFO  WebElementActions - Entered text in Search box: computer
2025-11-07 23:22:03 [main] ERROR BaseTest - Test failed: Element not found
```

### Test Reports (HTML)
```
reports/
├── TestReport_2025-11-07_22-30-10.html
├── TestReport_2025-11-07_22-33-29.html
└── TestReport_2025-11-07_22-35-28.html
```

**How to View:**
1. Download artifact
2. Extract ZIP
3. Open HTML file in web browser
4. See formatted test results with:
   - Test execution summary
   - Pass/Fail status
   - Execution time
   - Error messages

## 🎨 Workflow Run Summary View

When you click on a workflow run, you'll see:

```
📊 Test Results (chrome on ubuntu-latest)
  ✅ Passed: 8 tests
  ❌ Failed: 2 tests
  ⏭️  Skipped: 0 tests
  ⏱️  Duration: 2m 34s

📦 Artifacts (4)
  📸 screenshots-chrome-ubuntu-latest-123 (2.3 MB)
  📋 logs-chrome-ubuntu-latest-123 (156 KB)
  📊 test-reports-chrome-ubuntu-latest-123 (89 KB)
  📈 testng-results-chrome-ubuntu-latest-123 (234 KB)

## Test Artifacts Generated
✅ Test Reports: 3 file(s)
📸 Screenshots: 10 file(s)
📋 Log Files: 1 file(s)
📊 TestNG Output: Available
```

## 🔔 Setting Up Notifications

### Get Notified When Tests Fail

#### Email Notifications:
1. Go to GitHub Settings → Notifications
2. Enable "Actions" notifications
3. Choose email or web notifications

#### Slack Integration:
Add to your workflow:
```yaml
- name: Notify Slack on Failure
  if: failure()
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'Test automation failed! Check artifacts for details.'
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

## 📁 Artifact Retention

**Current Settings:** 30 days
- Artifacts are automatically deleted after 30 days
- Download important artifacts before expiration
- Can be configured in workflow file (`retention-days`)

## 💡 Pro Tips

### 1. **Quick Failure Investigation**
When tests fail:
1. Check "Summary" tab first (see which tests failed)
2. Download screenshots artifact (visual evidence)
3. Download logs artifact (detailed error messages)

### 2. **Compare Across Browsers**
If tests pass on Chrome but fail on Firefox:
1. Download both screenshot artifacts
2. Compare screenshots side-by-side
3. Check browser-specific issues

### 3. **Debugging Flaky Tests**
For intermittent failures:
1. Check multiple workflow runs
2. Download logs from failed runs
3. Look for timing issues or race conditions

### 4. **Local vs CI Differences**
If tests pass locally but fail in CI:
1. Check screenshots to see what CI sees
2. Review logs for environment differences
3. Look for timing or loading issues

## 🚀 Accessing Artifacts Programmatically

### Python Script Example:
```python
import requests
import os

REPO = "YOUR_USERNAME/REPO_NAME"
RUN_ID = "123456"
TOKEN = os.getenv("GITHUB_TOKEN")

# Get artifacts
response = requests.get(
    f"https://api.github.com/repos/{REPO}/actions/runs/{RUN_ID}/artifacts",
    headers={"Authorization": f"token {TOKEN}"}
)

artifacts = response.json()["artifacts"]

# Download screenshots
for artifact in artifacts:
    if "screenshots" in artifact["name"]:
        print(f"Downloading {artifact['name']}")
        # Download logic here
```

## 📞 Troubleshooting

### "No artifacts found"
**Possible causes:**
- Tests didn't generate any screenshots/logs
- Tests failed before artifact upload step
- Directory paths incorrect

**Solution:**
- Check workflow logs for errors
- Verify directories exist in workspace
- Add `if-no-files-found: warn` to upload step

### "Artifact expired"
**Cause:** Artifacts deleted after retention period (30 days)

**Solution:**
- Download important artifacts promptly
- Increase retention period if needed
- Archive critical test results elsewhere

### "Cannot download artifact"
**Possible causes:**
- No permissions to access repository
- Artifact not yet uploaded
- Browser/network issues

**Solution:**
- Check repository permissions
- Wait for workflow to complete
- Try GitHub CLI or API

## 📊 Artifact Size Limits

**GitHub Artifact Limits:**
- Free tier: 500 MB per artifact
- Pro tier: 2 GB per artifact
- Enterprise: 10 GB per artifact

**Optimization Tips:**
- Only capture screenshots on failure
- Compress logs if very large
- Clean up old screenshots before upload

## ✅ Summary

**Your workflow now provides:**
1. ✅ Separate artifacts for screenshots, logs, reports
2. ✅ Test results published in GitHub UI
3. ✅ Summary showing artifact counts
4. ✅ 30-day retention period
5. ✅ Easy access via Actions tab

**To view artifacts:**
1. Go to: `Repository → Actions → Workflow Run`
2. Scroll to: **"Artifacts"** section
3. Download: Click artifact name
4. Extract: Unzip the downloaded file
5. View: Open screenshots/logs/reports

---

**Your workflow file has been updated and is ready to use!**

Push your changes to GitHub and run the workflow to see the enhanced artifact organization in action! 🚀
