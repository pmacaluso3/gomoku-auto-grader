package learn.gomoku;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestResultReportClient {

    private final String token;
    private final String apiUrl;

    private final String submissionId;

    ObjectMapper mapper = new ObjectMapper();

    OkHttpClient client = new OkHttpClient();


    public TestResultReportClient() {
        this.token = loadRequiredEnvVar("ADMIN_TOKEN");
        this.apiUrl = loadRequiredEnvVar("API_URL");
        this.submissionId = loadRequiredEnvVar("SUBMISSION_ID");
    }

    public void report(String testName, Boolean success, String description, String boardState) {
        Map<String, Object> data = new HashMap<>();
        data.put("testName", testName);
        data.put("success", success);
        data.put("description", description);
        data.put("boardState", boardState);
        data.put("submissionId", submissionId);
        makePost(data);
    }

    private void makePost(Map<String, Object> data) {
        String json = null;
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(apiUrl + "/test_case_outcomes")
                .header("Authorization", String.format("Bearer %s", token))
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private class EnvironmentVariableNotFoundException extends RuntimeException {
        private EnvironmentVariableNotFoundException(String message) {
            super(message);
        }
    }

    private String loadRequiredEnvVar(String key) {
        String value = System.getProperties().get(key).toString();
        if (value == null || value.isBlank()) {
            throw new EnvironmentVariableNotFoundException(String.format("Environment variable %s not found", key));
        }
        return value;
    }
}
