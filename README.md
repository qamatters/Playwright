<h1 align="center">Mocker</h1>

<p align="center">
  <img alt="Github top language" src="https://img.shields.io/github/languages/top/KhushalJangid/Maven-test-repo?color=56BEB8">

  <img alt="Github language count" src="https://img.shields.io/github/languages/count/KhushalJangid/Maven-test-repo?color=56BEB8">

  <img alt="Repository size" src="https://img.shields.io/github/repo-size/KhushalJangid/Maven-test-repo?color=56BEB8">

  <img alt="License" src="https://img.shields.io/github/license/KhushalJangid/Maven-test-repo?color=56BEB8">

  <img alt="Github issues" src="https://img.shields.io/github/issues/KhushalJangid/Maven-test-repo?color=56BEB8" />

  <img alt="Github forks" src="https://img.shields.io/github/forks/KhushalJangid/Maven-test-repo?color=56BEB8" />

  <img alt="Github stars" src="https://img.shields.io/github/stars/KhushalJangid/Maven-test-repo?color=56BEB8" />

  <img alt="Visitors" src="https://visitor-badge.laobi.icu/badge?page_id=khushaljangid/Maven-test-repo&format=true">
</p>

## Index

- [About](#about)
- [Project Structure](#project-structure)
- [Features](#features)
- [Technologies](#technologies)
- [Requirements](#requirements)
- [How to use](#how-to-use)
  - [Setup](#setup)
  - [Running Tests](#running-tests)
  - [Test Configuration](#test-configuration)
  - [Output](#output)
- [Customization](#customization)

<br>

## About

Mocker is a simple mock testing project based on [Playwright](https://playwright.dev/) and [TestNG](https://testng.org/) in Java. It demonstrates API and UI testing, logging, reporting, and email notifications.

## Project Structure

```
.
├── pom.xml
├── testng.xml
├── pw-api.xml
├── pw-ui.xml
├── pw-amzn.xml
├── README.md
├── src/
│   ├── main/
│   └── test/
│       ├── java/
│       │   ├── listeners/
│       │   │   └── ExtentTestNGReporter.java
│       │   ├── tests/
│       │   │   └── Api/
│       │   │       ├── T001_JPH_API_Test.java
│       │   │   └── UI/
│       │   │       ├── T002_JPH_UI_Test.java
│       │   │       └── T003_Amazon _UI_Test.java
│       │   └── utils/
│       │       ├── EmailClient.java
│       │       ├── EmailReportBuilder.java
│       │       ├── FileLogger.java
│       │       ├── Logger.java
│       │       └── enums/
│       │           ├── BrowserEngine.java
│       │           └── LogMode.java
│       └── resources/
│           └── extent-config.xml
│           └── smtp.properties 
├── logs/
├── reports/
└── test-output/
```

## Features

- **API and UI Testing**: Uses Playwright for both API and browser-based UI tests.
- **TestNG Integration**: Test lifecycle and parameterization via TestNG.
- **Logging**: Custom logging to console and files ([`utils.Logger`](src/test/java/utils/Logger.java), [`utils.FileLogger`](src/test/java/utils/FileLogger.java)).
- **Reporting**: Generates HTML reports using ExtentReports ([`listeners.ExtentTestNGReporter`](src/test/java/listeners/ExtentTestNGReporter.java)).
- **Email Notifications**: Sends test summary and attaches reports via email ([`utils.EmailClient`](src/test/java/utils/EmailClient.java)).
- **Configurable**: Supports browser selection, headless mode, and log mode via CLI or suite parameters.


## Technologies

The following tools were used in this project:

- [Java 21+](https://www.java.com/en/)
- [Maven](https://maven.apache.org/)
- [Playwright](https://playwright.dev/)

## Requirements

Before starting, you need to have [Git](https://git-scm.com), [Java](https://www.java.com/en/) and [Maven](https://maven.apache.org/) installed to clone and build the Project.


## How to use

Follow these steps to run the project:

### Setup

1. **Clone the repository:**
    ```sh
    git clone https://github.com/KhushalJangid/Mocker.git
    cd Mocker
    ```

2. **Configure SMTP for email reports (Optional):**
    - Create `src/test/resources/smtp.properties` with your SMTP settings:
      ```
      username=your_email@example.com
      password=your_password
      host=smtp.example.com
      port=587
      ```

### Running Tests

- **Run all tests:**
    ```sh
    mvn test
    ```

- **Change browser or headless mode:**
    ```sh
    mvn test -Dbrowser=firefox -Dheadless=false
    ```

- **Run a specific test class:**
    ```sh
    mvn test -Dtest="T001_JPH_API_Test"

### Test Configuration

- TestNG suite file: [testing.xml](testing.xml)
- Logging modes: `All`, `Console`, `File`, `None`
- Browser engines: `Chromium`, `Firefox`, `Webkit`

### Output

- **Logs:** `logs/test-<timestamp>.log`
- **Reports:** `reports/ExtentReport-<timestamp>.html`
- **Email:** Sent after test suite completion (if SMTP is configured)

## Customization

- **Add new tests:** Place them in `src/test/java/tests/playwright/`.
- **Modify report appearance:** Edit [`extent-config.xml`](src/test/resources/extent-config.xml).
- **Change email recipients:** Update `username` in `smtp.properties`.

&#xa0;

<a href="#top">Back to top</a>
