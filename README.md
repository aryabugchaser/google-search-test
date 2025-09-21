# QA Automation Framework – Selenium, TestNG & CI/CD 🚀

[![CI](https://github.com/aryabugchaser/google-search-test/actions/workflows/tests.yml/badge.svg)](https://github.com/aryabugchaser/google-search-test/actions/workflows/tests.yml)

## 📖 Overview
This project demonstrates a **scalable UI test automation framework** built with **Java, Selenium WebDriver, and TestNG**, fully integrated with a **GitHub Actions CI pipeline**.  
It follows industry best practices to ensure reliability, maintainability, and continuous validation of code quality.

The framework includes end-to-end test scenarios for:
- 🔑 **Authentication flows** – Valid and invalid login cases
- 🔍 **Search functionality** – Example search flows
- 🧪 **Negative test coverage** – Empty fields, incorrect inputs, locked users

---

## ⚡ Key Features
- ✅ **Selenium WebDriver + TestNG** for test automation
- ✅ **Maven** for build and dependency management
- ✅ **Page Object Model (POM)** ready structure for maintainability
- ✅ **Continuous Integration** with GitHub Actions
- ✅ **Cross-browser ready** (can extend to Firefox, Edge, etc.)

---

## 🖥️ Running Tests Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/aryabugchaser/google-search-test.git

Navigate into the project folder

cd google-search-test


Run all tests

mvn clean test -Dsurefire.suiteXmlFiles=testng-ci.xml


View reports
Reports are generated under:

target/surefire-reports/

📊 CI/CD Pipeline

This project uses GitHub Actions for Continuous Integration.
Every push or pull request triggers:

Code checkout

JDK & Maven setup

Maven build

Run all TestNG tests

Upload test reports

🔧 Tech Stack

Java 17

Selenium WebDriver

TestNG

Maven

GitHub Actions (CI/CD)

🚀 Future Improvements

Add cross-browser testing (Firefox, Edge)

Integrate with Allure Reports for advanced reporting

Add Docker + Selenium Grid for parallel test execution

Expand test coverage for additional features



