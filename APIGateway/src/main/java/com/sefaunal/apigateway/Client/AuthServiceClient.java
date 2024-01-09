package com.sefaunal.apigateway.Client;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author github.com/sefaunal
 * @since 2024-01-11
 */
@Component
@RequiredArgsConstructor
public class AuthServiceClient {
    private static final Logger LOG = LoggerFactory.getLogger(AuthServiceClient.class);

    private final DiscoveryClient discoveryClient;

    public Boolean isTokenValid(String token) {
        OkHttpClient okHttpClient = new OkHttpClient();

        List<ServiceInstance> instances = discoveryClient.getInstances("UmbrellaAuth");

        String url = instances.getFirst().getUri() + "/api/auth/verify/token?token=" + token;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null && Boolean.parseBoolean(response.body().string())) {
                LOG.info("Token Validation Successful");
                return true;
            } else {
                LOG.warn("Validation Fail, Token Not Valid");
                return false;
            }
        } catch (Exception e) {
            LOG.warn("Error -> {}", e.getMessage());
            return false;
        }
    }
}
