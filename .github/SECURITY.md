# Security Policy

## 🔒 Repository Security

This is a **public repository** for a test automation framework. While the code is open for learning and contribution, access control is strictly enforced.

## 📊 Repository Access Levels

### Public Access (Everyone)
✅ **CAN DO:**
- View all code
- Fork the repository
- Clone to local machine
- Create issues
- Submit pull requests
- Star and watch the repository

❌ **CANNOT DO:**
- Push directly to any branch
- Merge pull requests
- Modify repository settings
- Delete branches
- Manage releases
- Modify workflows

### Repository Owner Only
✅ **EXCLUSIVE PERMISSIONS:**
- Merge pull requests
- Push to protected branches
- Modify repository settings
- Manage branch protection rules
- Approve/reject contributions
- Manage releases and tags
- Configure GitHub Actions secrets

## 🛡️ Branch Protection

### Protected Branches: `main`, `master`, `develop`

**Rules Enforced:**
1. ✅ Require pull request before merging
2. ✅ Require status checks to pass (CI/CD tests)
3. ✅ Require review from code owner
4. ✅ Dismiss stale reviews on new commits
5. ✅ Require linear history
6. ❌ No force pushes allowed
7. ❌ No branch deletion allowed
8. ✅ Include administrators in restrictions

### Workflow:
```
Feature Branch → Pull Request → Review → Tests Pass → Merge by Owner
```

## 🚨 What Happens If Someone Tries to Push Directly?

### Scenario 1: Unauthorized User Pushes to Main/Develop
```bash
git push origin main
```
**Result:** ❌ **REJECTED**
```
remote: error: GH006: Protected branch update failed for refs/heads/main.
remote: error: Required status check "tests" is expected.
remote: error: At least 1 approving review is required by reviewers with write access.
To https://github.com/user/repo.git
 ! [remote rejected] main -> main (protected branch hook declined)
error: failed to push some refs to 'https://github.com/user/repo.git'
```

### Scenario 2: Contributor Submits Pull Request
```
1. Contributor creates PR
2. Automated tests run (CI/CD)
3. Owner reviews code
4. If approved → Owner merges
5. If rejected → PR closed with feedback
```

### Scenario 3: Malicious Changes Attempted
**GitHub automatically:**
- Blocks direct pushes to protected branches
- Requires owner approval for all PRs
- Runs security scans on new code
- Alerts owner of suspicious activity
- Requires 2FA for sensitive operations

## 🔐 Security Best Practices

### For Repository Owner:
1. ✅ Enable two-factor authentication (2FA)
2. ✅ Use strong, unique passwords
3. ✅ Rotate GitHub Personal Access Tokens regularly
4. ✅ Review PR changes carefully before merging
5. ✅ Keep GitHub Actions secrets secure
6. ✅ Monitor repository activity logs
7. ✅ Use branch protection rules
8. ✅ Sign commits with GPG keys

### For Contributors:
1. ✅ Fork the repository to your account
2. ✅ Work in feature branches
3. ✅ Never commit sensitive data (passwords, keys, tokens)
4. ✅ Test changes locally before PR
5. ✅ Follow contribution guidelines
6. ✅ Keep fork synchronized with upstream

## 🔍 What to Keep Secure

### ❌ NEVER Commit:
- Database passwords
- API keys and tokens
- AWS/Azure credentials
- SSH private keys
- OAuth secrets
- Personal email addresses (use GitHub email)
- Production URLs with credentials

### ✅ Safe to Commit:
- Test automation code
- Page Object Models
- Utility classes
- Configuration templates (without real credentials)
- Test data (non-sensitive)
- Documentation

## 🚦 GitHub Actions Security

### Secrets Management:
- All sensitive data stored in GitHub Secrets
- Secrets never exposed in logs
- Only owner can view/modify secrets
- Secrets not accessible in fork PRs (security feature)

### Workflow Permissions:
```yaml
permissions:
  contents: read       # Can read repo content
  checks: write        # Can write test results
  pull-requests: write # Can comment on PRs
```

## 📢 Reporting Security Vulnerabilities

### If You Find a Security Issue:

**DO NOT open a public GitHub issue!**

Instead:
1. **Email:** [Your contact email here]
2. **Subject:** "Security Issue: [Brief Description]"
3. **Include:**
   - Description of vulnerability
   - Steps to reproduce
   - Potential impact
   - Suggested fix (if known)

### Response Time:
- **Acknowledgment:** Within 48 hours
- **Initial Assessment:** Within 1 week
- **Fix Timeline:** Depends on severity
  - Critical: 24-48 hours
  - High: 1 week
  - Medium: 2 weeks
  - Low: 1 month

### Disclosure Policy:
- No public disclosure until fix is released
- Credit given to reporter (if desired)
- Security advisory published after fix

## 🛠️ Recommended GitHub Repository Settings

### Enable in Repository Settings:
1. **General:**
   - ✅ Disable Wiki (if not used)
   - ✅ Disable Projects (if not used)
   - ✅ Automatically delete head branches after merge

2. **Branches:**
   - ✅ Set `main` as default branch
   - ✅ Add branch protection rules

3. **Actions:**
   - ✅ Allow actions from verified creators only
   - ✅ Require approval for fork PRs
   - ✅ Set workflow permissions to read-only default

4. **Security:**
   - ✅ Enable Dependabot alerts
   - ✅ Enable Dependabot security updates
   - ✅ Enable secret scanning
   - ✅ Enable push protection

5. **Code scanning:**
   - ✅ Enable CodeQL analysis
   - ✅ Scan on push and PR

## 📋 Access Control Checklist

- [ ] Repository is public (anyone can view)
- [ ] Branch protection enabled on main/develop
- [ ] Code owners file configured
- [ ] Contributing guidelines published
- [ ] Security policy published
- [ ] Two-factor authentication enabled
- [ ] Dependabot alerts enabled
- [ ] Secret scanning enabled
- [ ] Code scanning enabled
- [ ] Workflow permissions restricted

## 🔄 Regular Security Reviews

### Monthly:
- Review access logs
- Check for outdated dependencies
- Review open PRs for suspicious changes
- Update security documentation

### Quarterly:
- Rotate access tokens
- Review and update branch protection rules
- Audit repository settings
- Review security alerts

## 📞 Contact

For security concerns or questions:
- **GitHub Discussions:** For general questions
- **Email:** [Your secure contact email]
- **GitHub Security Advisory:** For vulnerabilities

---

**Last Updated:** November 7, 2025

**Repository Owner:** @subramanyamkongani
