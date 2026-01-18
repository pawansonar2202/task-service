package com.task_service.task_service.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PublicKeyFetcher {

    private final PublicKeyHolder publicKeyHolder;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @PostConstruct
    public void fetchPublicKey()
    {
        try {

            RestTemplate restTemplate = new RestTemplate();

            // Call Auth Service
            String keyBase64 = restTemplate.getForObject(
                    authServiceUrl + "/auth/public-key",
                    String.class
            );

            // Convert Base64 → RSAPublicKey
            byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

            RSAPublicKey publicKey = (RSAPublicKey)
                    KeyFactory.getInstance("RSA").generatePublic(keySpec);

            // Store in memory
            publicKeyHolder.setRsaPublicKey(publicKey);

        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to fetch public key");
        }
    }
}
