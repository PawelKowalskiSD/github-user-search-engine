package com.example.githubusersearchengine.controller;

import com.example.githubusersearchengine.controller.dto.GithubUserBranchDto;
import com.example.githubusersearchengine.controller.dto.GithubUserRepoDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubUserControllerTest {

    private final static String TEST_URL = "/v1/user/username";
    private final static String REPOSITORY_NAME = "project-crypto-wallet2023";
    private final static String USERNAME = "PawelKowalskiSD";
    private final static String BRANCH_NAME = "main";
    private final static String SHA = "40c6b2c3c57af6e439ebc19f1f751612ae84fb98";

    private static WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor(wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void should_return_exception() throws IOException {
        //Given
        String responseBody = IOUtils.resourceToString("/response_exception.json", StandardCharsets.UTF_8);
        stubFor(get(urlEqualTo(TEST_URL))
                .willReturn(
                        aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                )
        );
        //When
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(wireMockServer.baseUrl()));
        //Then
        assertThrows(RestClientException.class, () -> {
            ResponseEntity<GithubUserRepoDto[]> response = restTemplate.getForEntity(TEST_URL, GithubUserRepoDto[].class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        });
    }

    @Test
    void should_return_status_ok_and_return_body() throws IOException {
        //Given
        String responseBody = IOUtils.resourceToString("/response.json", StandardCharsets.UTF_8);
        stubFor(get(urlEqualTo(TEST_URL))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)));
        //When
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(wireMockServer.baseUrl()));
        ResponseEntity<GithubUserRepoDto[]> response = restTemplate.getForEntity(TEST_URL, GithubUserRepoDto[].class);
        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(REPOSITORY_NAME, getResponseRepoName(response));
        assertEquals(USERNAME, getResponseLogin(response));
        assertEquals(BRANCH_NAME, getResponseBranchName(response));
        assertEquals(SHA, getResponseSha(response));
    }

    private static String getResponseRepoName(ResponseEntity<GithubUserRepoDto[]> response) {
        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(GithubUserRepoDto::name)
                .reduce((a, b) -> a.equals(REPOSITORY_NAME) ? a : b)
                .orElse(null);
    }

    private static String getResponseLogin(ResponseEntity<GithubUserRepoDto[]> response) {
        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(githubUserRepoDto -> githubUserRepoDto
                        .owner()
                        .login())
                .reduce((a, b) -> a.equals(USERNAME) ? a : b)
                .orElse(null);
    }

    private static String getResponseBranchName(ResponseEntity<GithubUserRepoDto[]> response) {
        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .flatMap(repo -> repo.branch()
                        .stream())
                .map(GithubUserBranchDto::name)
                .reduce(BRANCH_NAME, Objects::toString);
    }

    private static String getResponseSha(ResponseEntity<GithubUserRepoDto[]> response) {
        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .flatMap(repo -> repo.branch()
                        .stream())
                .map(branch -> branch
                        .commit()
                        .sha())
                .reduce(SHA, Objects::toString);
    }
}