# Branch Protection Setup Guide

## 🎯 Purpose
This guide helps you configure GitHub branch protection rules to prevent unauthorized changes to your public repository.

## ⚠️ Current Risk (Without Protection)
Since this is a **public repository**:
- Anyone can fork and clone your code ✅ (This is fine)
- Anyone can submit Pull Requests ✅ (This is fine)
- **BUT:** Without branch protection, contributors with write access could push directly ❌

## 🛡️ Solution: Branch Protection Rules

### Step-by-Step Setup

#### 1. Go to Repository Settings
1. Navigate to your GitHub repository
2. Click **Settings** (top right)
3. Click **Branches** (left sidebar under "Code and automation")

#### 2. Add Branch Protection Rule for `main`

Click **"Add branch protection rule"**

**Configure these settings:**

##### A. Branch Name Pattern
```
main
```

##### B. Protection Rules to Enable

**✅ Require a pull request before merging**
- Check: "Require approvals" → Set to **1**
- Check: "Dismiss stale pull request approvals when new commits are pushed"
- Check: "Require review from Code Owners"
- Uncheck: "Allow specified actors to bypass" (no exceptions)

**✅ Require status checks to pass before merging**
- Check: "Require branches to be up to date before merging"
- Add required status checks:
  - `Run Automated Tests` (from your test-automation.yml workflow)
  - `test / Run Automated Tests (chrome, ubuntu-latest, 11)` (specific job)
  - `test / Run Automated Tests (firefox, ubuntu-latest, 11)` (specific job)

**✅ Require conversation resolution before merging**
- Ensures all PR comments are addressed

**✅ Require signed commits** (Optional but recommended)
- Forces contributors to sign commits with GPG keys

**✅ Require linear history**
- Prevents merge commits, keeps history clean

**✅ Require deployments to succeed before merging** (Optional)
- If you have deployment workflows

**❌ Do not allow bypassing the above settings**
- Ensures even admins follow the rules

**✅ Restrict who can push to matching branches**
- Check this option
- Don't add any users/teams (only you as owner can push)

**✅ Lock branch**
- Makes branch read-only (Optional, use for production only)

**✅ Do not allow deletions**
- Prevents accidental branch deletion

**✅ Do not allow force pushes**
- Prevents rewriting history

Click **"Create"** to save the rule.

#### 3. Add Branch Protection Rule for `develop`

Repeat the same steps for the `develop` branch:
- Branch name pattern: `develop`
- Apply the same protection rules as `main`

#### 4. Add Branch Protection Rule for `master` (if exists)

If you have a `master` branch:
- Branch name pattern: `master`
- Apply the same protection rules

### 📋 Quick Copy-Paste Checklist

When setting up branch protection, enable these:

```
☑️ Require a pull request before merging
  ☑️ Require approvals (1)
  ☑️ Dismiss stale pull request approvals
  ☑️ Require review from Code Owners
  
☑️ Require status checks to pass before merging
  ☑️ Require branches to be up to date
  ☑️ Status checks: test / Run Automated Tests
  
☑️ Require conversation resolution
☑️ Require linear history
☑️ Restrict who can push (no exceptions)
☑️ Do not allow deletions
☑️ Do not allow force pushes
```

## 🔒 What This Protects Against

### Scenario 1: Unauthorized Direct Push
```bash
# Someone tries to push directly to main
git push origin main
```
**Result:** ❌ **REJECTED**
```
! [remote rejected] main -> main (protected branch hook declined)
```

### Scenario 2: Bypassing Tests
**Result:** ❌ **BLOCKED**
- PR cannot be merged until all CI/CD tests pass
- Green checkmark required on all status checks

### Scenario 3: Merging Without Review
**Result:** ❌ **BLOCKED**
- PR shows "Review required" status
- Cannot merge until approved by code owner

### Scenario 4: Force Push to Rewrite History
```bash
git push --force origin main
```
**Result:** ❌ **REJECTED**
```
! [remote rejected] main -> main (force push not allowed)
```

## ✅ Correct Workflow After Protection

### For Repository Owner:
```bash
# 1. Create feature branch
git checkout -b feature/new-feature

# 2. Make changes and commit
git add .
git commit -m "feat: add new feature"

# 3. Push feature branch
git push origin feature/new-feature

# 4. Create PR on GitHub

# 5. Wait for tests to pass

# 6. Review and merge PR (via GitHub UI)
```

### For Contributors:
```bash
# 1. Fork repository on GitHub

# 2. Clone your fork
git clone https://github.com/YOUR-USERNAME/ecommerce-web-automated-test-demo.git

# 3. Create feature branch
git checkout -b feature/my-contribution

# 4. Make changes and commit
git add .
git commit -m "feat: add awesome feature"

# 5. Push to your fork
git push origin feature/my-contribution

# 6. Create PR from your fork to original repo

# 7. Wait for owner review and approval
```

## 🚨 Additional Security Settings

### 1. Enable Dependabot Alerts
**Path:** Settings → Security → Code security and analysis
- ✅ Dependabot alerts
- ✅ Dependabot security updates
- ✅ Grouped security updates

### 2. Enable Secret Scanning
**Path:** Settings → Security → Code security and analysis
- ✅ Secret scanning
- ✅ Push protection (blocks commits with secrets)

### 3. Enable Code Scanning
**Path:** Settings → Security → Code security and analysis
- ✅ CodeQL analysis
- Set up workflow: Use default configuration

### 4. Configure Workflow Permissions
**Path:** Settings → Actions → General → Workflow permissions
- Select: **"Read repository contents and packages permissions"**
- ✅ Allow GitHub Actions to create and approve pull requests (UNCHECK this)

### 5. Set Collaborator Permissions
**Path:** Settings → Collaborators and teams
- Review all collaborators
- Remove any unwanted access
- Set appropriate permission levels:
  - **Read:** Can view and fork
  - **Triage:** Can manage issues (no code access)
  - **Write:** Can push to non-protected branches ⚠️
  - **Maintain:** Can manage without sensitive access ⚠️
  - **Admin:** Full access ⚠️

## 📊 Verify Protection is Active

### Check 1: Branch Protection Badge
1. Go to repository main page
2. Click on **"branches"** (below repository name)
3. You should see a shield icon 🛡️ next to protected branches

### Check 2: Try Direct Push (Testing)
```bash
# In a test scenario, try to push to main
git checkout main
echo "test" >> test.txt
git add test.txt
git commit -m "test: verify protection"
git push origin main

# Expected: Should be rejected ✅
```

### Check 3: Create Test PR
1. Create a feature branch
2. Make a small change
3. Push and create PR
4. Verify you cannot merge without:
   - ✅ Tests passing
   - ✅ Review approval
   - ✅ All conversations resolved

## 🔄 Maintenance

### Weekly:
- [ ] Review open PRs
- [ ] Check for security alerts

### Monthly:
- [ ] Review branch protection rules
- [ ] Update status check requirements (if workflows change)
- [ ] Review collaborator access

### Quarterly:
- [ ] Audit repository settings
- [ ] Review and update security policies
- [ ] Check for outdated dependencies

## 📞 Need Help?

If you encounter issues setting up branch protection:

1. **GitHub Docs:** https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches
2. **GitHub Community:** https://github.community/
3. **Stack Overflow:** Tag with `github` and `branch-protection`

## ✨ Benefits After Setup

1. ✅ **No unauthorized changes** - All changes require PR review
2. ✅ **Quality assurance** - Tests must pass before merge
3. ✅ **Code review** - All code is reviewed before merging
4. ✅ **Clean history** - Linear history, no force pushes
5. ✅ **Audit trail** - All changes tracked in PRs
6. ✅ **Team safety** - Even mistakes by owner are caught
7. ✅ **Professional workflow** - Industry-standard process

---

**Setup Priority: HIGH** ⚠️

**Recommended Action:** Set up branch protection rules **immediately** after reading this guide.

**Estimated Time:** 10-15 minutes

**Difficulty:** Easy (just checkboxes and settings)

**Impact:** Critical security improvement
