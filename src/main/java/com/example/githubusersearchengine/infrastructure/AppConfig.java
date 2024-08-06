package com.example.githubusersearchengine.infrastructure;

import com.example.githubusersearchengine.domain.user.GithubClient;
import com.example.githubusersearchengine.domain.user.GithubUserService;
import com.example.githubusersearchengine.infrastructure.api.client.GithubUserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {
    @Value("${github.basicUrl}")
    private String basicUrl;

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl(basicUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    GithubClient GithubUserClient(RestClient restClient) {
        return new GithubUserClient(restClient);
    }

    @Bean
    GithubUserService githubUserService(GithubClient githubClient) {
        return new GithubUserService(githubClient);
    }
}