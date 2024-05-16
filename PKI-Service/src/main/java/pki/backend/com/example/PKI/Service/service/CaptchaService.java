package pki.backend.com.example.PKI.Service.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";


    public boolean verify(String token) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(RECAPTCHA_VERIFY_URL);
            String json = String.format("{\"secret\":\"%s\", \"response\":\"%s\"}", recaptchaSecret, token);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseString = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseString);
                return jsonObject.getBoolean("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

