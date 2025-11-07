# Contributing to E-Commerce Test Automation Framework

Thank you for your interest in contributing! This document provides guidelines for contributing to this project.

## 🔒 Repository Access & Security

### Important Notes:
- This is a **public repository** - anyone can view and fork the code
- Only the repository owner has **write access** to merge changes
- All contributions must go through **Pull Request review**
- Direct pushes to `main` and `develop` branches are **protected**

## 📋 How to Contribute

### 1. Fork the Repository
```bash
# Fork via GitHub UI, then clone your fork
git clone https://github.com/YOUR-USERNAME/ecommerce-web-automated-test-demo.git
cd ecommerce-web-automated-test-demo
```

### 2. Create a Feature Branch
```bash
git checkout -b feature/your-feature-name
```

### 3. Make Your Changes
- Write clean, well-documented code
- Follow existing code style and conventions
- Add tests for new functionality
- Update documentation as needed

### 4. Test Your Changes
```bash
# Run tests locally
mvn clean test

# Run specific browser tests
mvn test -Dbrowser=chrome -Dheadless=true
mvn test -Dbrowser=firefox -Dheadless=true
```

### 5. Commit Your Changes
```bash
git add .
git commit -m "feat: add new feature description"
```

Use conventional commit messages:
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `test:` - Test additions/changes
- `refactor:` - Code refactoring
- `chore:` - Maintenance tasks

### 6. Push to Your Fork
```bash
git push origin feature/your-feature-name
```

### 7. Create a Pull Request
1. Go to the original repository on GitHub
2. Click "New Pull Request"
3. Select your fork and branch
4. Provide a clear description of your changes
5. Wait for review and approval

## ✅ Pull Request Guidelines

### PR Requirements:
- [ ] Clear, descriptive title
- [ ] Detailed description of changes
- [ ] All tests pass in CI/CD pipeline
- [ ] Code follows project conventions
- [ ] Documentation updated if needed
- [ ] No merge conflicts with main branch

### PR Review Process:
1. **Automated Checks**: CI/CD pipeline must pass
2. **Code Review**: Repository owner reviews code
3. **Approval**: Owner approves and merges
4. **Merge**: Changes merged to main branch

## 🚫 What Gets Rejected

PRs will be rejected if they:
- Break existing tests
- Reduce code coverage significantly
- Don't follow coding standards
- Include malicious code or security vulnerabilities
- Modify CI/CD workflows without permission
- Change sensitive configuration without justification

## 📝 Coding Standards

### Java Code Style:
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Follow existing package structure
- Use Page Object Model pattern for new pages
- Keep methods focused and single-purpose

### Test Guidelines:
- One test method = one scenario
- Use descriptive test method names
- Include proper assertions
- Clean up test data in @AfterMethod
- Use TestNG annotations properly

### Commit Message Format:
```
type(scope): subject

body (optional)

footer (optional)
```

Example:
```
feat(login): add remember me functionality

- Added checkbox to login page
- Implemented cookie-based session storage
- Added tests for remember me feature

Closes #123
```

## 🐛 Reporting Issues

### Before Creating an Issue:
1. Check if issue already exists
2. Test with latest version
3. Gather relevant information

### Issue Template:
```markdown
**Description:**
Clear description of the issue

**Steps to Reproduce:**
1. Step 1
2. Step 2
3. Step 3

**Expected Behavior:**
What should happen

**Actual Behavior:**
What actually happens

**Environment:**
- OS: [e.g., Windows 10, macOS, Ubuntu]
- Java Version: [e.g., 11]
- Browser: [e.g., Chrome 119]
- Framework Version: [e.g., 1.0]

**Screenshots/Logs:**
Add relevant screenshots or logs
```

## 💡 Feature Requests

For new features:
1. Open an issue with "Feature Request" label
2. Describe the feature and use case
3. Wait for discussion and approval
4. Implement only after approval

## 🔐 Security Issues

**DO NOT** open public issues for security vulnerabilities!

Instead:
1. Email the repository owner directly
2. Provide detailed description privately
3. Wait for response and patch
4. Disclosure only after fix is released

## 📞 Questions?

- Open a Discussion on GitHub
- Check existing documentation
- Review closed issues for similar questions

## 🎯 Priority Areas for Contribution

We welcome contributions in these areas:
- [ ] New test scenarios for existing pages
- [ ] Page Objects for additional pages
- [ ] Utility methods and helpers
- [ ] Documentation improvements
- [ ] Bug fixes
- [ ] Performance optimizations
- [ ] Cross-browser compatibility fixes

## 📜 License

By contributing, you agree that your contributions will be licensed under the same license as this project.

---

**Thank you for contributing! 🎉**
