package com.example.talendtmc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class TalendTmcService {

    @Value("${talend.client-id}")
    private String clientId;

    @Value("${talend.client-secret}")
    private String clientSecret;

    @Value("${talend.token-uri}")
    private String tokenUri;

    @Value("${talend.api-base-url}")
    private String apiBaseUrl;

    private String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String base64Creds = new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()));
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    public List<Map<String, Object>> listPlans() {
        String accessToken = getAccessToken();
        String url = apiBaseUrl + "/plans";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    public String executePlan(String planId) {
        String accessToken = getAccessToken();
        String url = apiBaseUrl + "/plans/" + planId + "/executions";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        return (String) response.getBody().get("id");
    }

    public Map<String, Object> getJobStatus(String executionId) {
        String accessToken = getAccessToken();
        String url = apiBaseUrl + "/executions/" + executionId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }
}
