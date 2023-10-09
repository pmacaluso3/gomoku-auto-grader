package learn.gomoku;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

public class TestResultReportClient {

    private final String key;


    ObjectMapper mapper = new ObjectMapper();

    OkHttpClient client = new OkHttpClient();


    public TestResultReportClient(String key) {
        this.key = key;
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
}
