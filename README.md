# Bajaj Finserv Health - Qualifier 1 (JAVA)

This is a Spring Boot application developed as part of the Bajaj Finserv Health Qualifier 1 task.

## Task Overview

The application performs the following actions on startup:
1.  Sends a POST request to a designated endpoint (`/hiring/generateWebhook/JAVA`) to generate a unique webhook URL and an access token.
2.  Based on the `regNo` provided in the initial request, it determines which SQL problem (Question 1 or Question 2) is assigned. (Note: The current implementation provides the solution for Question 1).
3.  Submits the solution (a final SQL query) to the received webhook URL using the generated access token as a JWT (Bearer Token) in the `Authorization` header.

## Features Implemented

*   Spring Boot application setup with `spring-boot-starter-web`.
*   `CommandLineRunner` implementation to trigger the flow on application startup.
*   `RestTemplate` for making HTTP POST requests.
*   Parsing JSON responses to extract `webhookUrl` and `accessToken`.
*   Logic to determine SQL question based on the last two digits of `regNo`.
*   Pre-formulated SQL query (for Question 1) as the solution.
*   Dynamic submission of the SQL query to the generated webhook with JWT authorization.

## Setup Instructions

1.  **Java Development Kit (JDK):** Ensure you have JDK 17 or later installed and configured (JAVA_HOME environment variable set).
2.  **Apache Maven:** Ensure Apache Maven (version 3.x or later) is installed and its `bin` directory is added to your system's PATH environment variable. If `mvn` command doesn't work directly, use the full path to `mvn.cmd` as demonstrated during development.

## How to Build the Project

Navigate to the project root directory (`BajaFinServApp`) in your terminal and run the Maven clean install command:

```bash
cd C:\Users\anant\OneDrive\Desktop\BajaFinServ\BajaFinServApp
# If 'mvn' command is not recognized, use the full path:
# & "C:\Users\anant\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin\mvn.cmd" clean install
mvn clean install
```

This will compile the code, run tests, and package the application into an executable JAR file in the `target/` directory.

## How to Run the Application

After building the project, navigate to the `target` directory and run the generated JAR file:

```bash
cd C:\Users\anant\OneDrive\Desktop\BajaFinServ\BajaFinServApp\target
java -jar BajaFinServApp-0.0.1-SNAPSHOT.jar
```

The application will execute its workflow and print output to the console, including the API responses and submission status.

## Important Note on Webhook Submission

During development and testing, the final webhook submission to `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA` consistently returned a **`401 Unauthorized`** error.

This occurs despite:
*   Successfully retrieving a new `accessToken` from the `/generateWebhook` endpoint on each run.
*   Implementing the `Authorization` header with the `Bearer` token as per standard JWT practices.

This suggests a potential issue with the external API's token validation, such as an extremely short token expiration time, or specific API-side requirements not explicitly documented. The client-side code correctly implements the authorization mechanism as instructed.

## Submission Details

*   **GitHub Repository:** `https://github.com/anantgupta001/BajajFinServ.git`
*   **JAR File Location in Repo:** `target/BajaFinServApp-0.0.1-SNAPSHOT.jar`
*   **Publicly Downloadable GitHub Raw Link for JAR:** https://raw.githubusercontent.com/anantgupta001/BajajFinServ/master/target/BajaFinServApp-0.0.1-SNAPSHOT.jar
