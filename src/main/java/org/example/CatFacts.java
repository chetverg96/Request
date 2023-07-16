package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.List;

public class CatFacts {
    public static void main(String[] args) {
        List<CatFact> facts = getFacts();
        List<CatFact> filteredFacts = filterFacts(facts);
        printFacts(filteredFacts);
    }

    private static List<CatFact> getFacts() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<CatFact>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<CatFact> filterFacts(List<CatFact> facts) {
        return facts.stream()
                .filter(fact -> fact.getUpvotes() != null)
                .collect(Collectors.toList());
    }

    private static void printFacts(List<CatFact> facts) {
        for (CatFact fact : facts) {
            System.out.println(fact.getText());
        }
    }
}
