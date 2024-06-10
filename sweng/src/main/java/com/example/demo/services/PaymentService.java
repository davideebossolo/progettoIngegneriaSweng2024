package com.example.demo.services;

import com.example.demo.model.PaymentRequest;
import com.example.demo.model.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class PaymentService {
    private static final String PAYMENT_ENDPOINT = "http://localhost:6789/pay";

    public PaymentResponse processPayment(PaymentRequest paymentRequest) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(PAYMENT_ENDPOINT);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(paymentRequest);

        StringEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        PaymentResponse paymentResponse = objectMapper.readValue(entity.getContent(), PaymentResponse.class);

        response.close();
        httpClient.close();

        return paymentResponse;
    }
}
