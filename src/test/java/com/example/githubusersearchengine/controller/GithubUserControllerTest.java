package com.example.githubusersearchengine.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubUserControllerTest {

    private final static String TEST_URL = "/v1/user/username";
    private final static String LOCALHOST = "http://localhost:";
    private final static String USER_NOT_FOUND = "Not Found";
    private final static String REPOSITORY_NAME = "project-crypto-wallet2023";
    private final static String USERNAME = "PawelKowalskiSD";
    private final static String BRANCH_NAME = "main";
    private final static String SHA = "40c6b2c3c57af6e439ebc19f1f751612ae84fb98";
    private final static Integer REPOSITORY_INDEX = 13;

    private static WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

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
        //When&Then
        webTestClient
                .get()
                .uri(LOCALHOST + wireMockServer.port() + TEST_URL)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[*]").isNotEmpty()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.message").isEqualTo(USER_NOT_FOUND);
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
        //When&Then
        webTestClient
                .get()
                .uri(LOCALHOST + wireMockServer.port() + TEST_URL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[*]").isNotEmpty()
                .jsonPath("$[*]").isArray()
                .jsonPath("$[" + REPOSITORY_INDEX + "].name").isEqualTo(REPOSITORY_NAME)
                .jsonPath("$[" + REPOSITORY_INDEX + "].branch.[0].name").isEqualTo(BRANCH_NAME)
                .jsonPath("$[" + REPOSITORY_INDEX + "].owner.login").isEqualTo(USERNAME)
                .jsonPath("$[" + REPOSITORY_INDEX + "].branch.[0].commit.sha").isEqualTo(SHA);
    }
}