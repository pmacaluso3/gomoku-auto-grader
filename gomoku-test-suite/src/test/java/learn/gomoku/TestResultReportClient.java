package learn.gomoku;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

public class TestResultReportClient {

    private final String token;
    private final String apiUrl;


    ObjectMapper mapper = new ObjectMapper();

    OkHttpClient client = new OkHttpClient();


    public TestResultReportClient() {
        this.token = loadRequiredEnvVar("ADMIN_TOKEN");
        this.apiUrl = loadRequiredEnvVar("API_URL");
    }

    public void report(boolean success) {
        String url = "http://localhost:3000";
        if (success) {
            url += "/success";
        } else {
            url += "/failure";
        }
        makePost(url);
    }

    private void makePost(String url) {
        String json = "{}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(url)
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
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new EnvironmentVariableNotFoundException(String.format("Environment variable %s not found", key));
        }
        return value;
    }
}
