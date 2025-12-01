package com.bajajfinserv.BajaFinServApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebhookGenerator implements CommandLineRunner {

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("name", "John Doe");
        body.put("regNo", "REG12347");
        body.put("email", "john@example.com");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Send the POST request
        Map<String, Object> response = restTemplate.postForObject(GENERATE_WEBHOOK_URL, request, Map.class);

        System.out.println("Webhook Generation Response: " + response);

        String webhookUrl = (String) response.get("webhook");
        String accessToken = (String) response.get("accessToken");

        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("Access Token: " + accessToken);

        String regNo = body.get("regNo");
        int lastTwoDigits = Integer.parseInt(regNo.substring(regNo.length() - 2));

        String questionUrl;
        if (lastTwoDigits % 2 != 0) {
            questionUrl = "https://drive.google.com/file/d/1LAPx2to9zmN5NDY0tkMrJRnVXf1guNr/view?usp=sharing";
            System.out.println("Question 1 URL: " + questionUrl);
        } else {
            questionUrl = "https://drive.google.com/file/d/1b0p5C-6fUrUQglJVaWWAAB3P12IfoBCH/view?usp=sharing";
            System.out.println("Question 2 URL: " + questionUrl);
        }

        // SQL Query for Question 1
        String finalSqlQuery = "WITH FilteredPayments AS (\n" +
                               "    SELECT\n" +
                               "        P.EMP_ID,\n" +
                               "        P.AMOUNT,\n" +
                               "        P.PAYMENT_TIME\n" +
                               "    FROM\n" +
                               "        PAYMENTS P\n" +
                               "    WHERE\n" +
                               "        DAY(P.PAYMENT_TIME) <> 1\n" +
                               "),\n" +
                               "EmployeeSalaries AS (\n" +
                               "    SELECT\n" +
                               "        E.EMP_ID,\n" +
                               "        E.FIRST_NAME,\n" +
                               "        E.LAST_NAME,\n" +
                               "        E.DOB,\n" +
                               "        D.DEPARTMENT_NAME,\n" +
                               "        FP.AMOUNT AS SALARY,\n" +
                               "        ROW_NUMBER() OVER(PARTITION BY D.DEPARTMENT_NAME ORDER BY FP.AMOUNT DESC) as rn\n" +
                               "    FROM\n" +
                               "        FilteredPayments FP\n" +
                               "    JOIN\n" +
                               "        EMPLOYEE E ON FP.EMP_ID = E.EMP_ID\n" +
                               "    JOIN\n" +
                               "        DEPARTMENT D ON E.DEPARTMENT = D.DEPARTMENT_ID\n" +
                               ")\n" +
                               "SELECT\n" +
                               "    DEPARTMENT_NAME,\n" +
                               "    SALARY,\n" +
                               "    CONCAT(FIRST_NAME, ' ', LAST_NAME) AS EMPLOYEE_NAME,\n" +
                               "    TIMESTAMPDIFF(YEAR, DOB, CURDATE()) AS AGE\n" +
                               "FROM\n" +
                               "    EmployeeSalaries\n" +
                               "WHERE\n" +
                               "    rn = 1;";

        // Send the SQL solution to the webhook URL
        HttpHeaders solutionHeaders = new HttpHeaders();
        solutionHeaders.setContentType(MediaType.APPLICATION_JSON);
        solutionHeaders.setBearerAuth(accessToken);

        Map<String, String> solutionBody = new HashMap<>();
        solutionBody.put("finalQuery", finalSqlQuery);

        HttpEntity<Map<String, String>> solutionRequest = new HttpEntity<>(solutionBody, solutionHeaders);

        if (webhookUrl != null && !webhookUrl.isEmpty()) {
            try {
                restTemplate.postForObject(webhookUrl, solutionRequest, String.class);
                System.out.println("SQL solution sent to webhook successfully.");
            } catch (Exception e) {
                System.err.println("Error sending SQL solution to webhook: " + e.getMessage());
            }
        } else {
            System.err.println("Webhook URL is null or empty. Cannot send SQL solution.");
        }
    }
}
