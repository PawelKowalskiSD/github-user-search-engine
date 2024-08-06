package com.example.githubusersearchengine.infrastructure.api.client;

import com.example.githubusersearchengine.domain.exceptions.UserNotFoundException;
import com.example.githubusersearchengine.domain.user.GithubClient;
import com.example.githubusersearchengine.domain.user.model.GithubUserBranch;
import com.example.githubusersearchengine.domain.user.model.GithubUserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

public class GithubUserClient implements GithubClient {
    private final RestClient restClient;

    public GithubUserClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<GithubUserRepo> findAll(String username) {
        return List.of(Objects.requireNonNull(restClient.get()
                .uri("/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(code -> code.is4xxClientError() || code.is5xxServerError(), (request, response) -> {
                    if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                        throw new UserNotFoundException(response.getStatusText());
                    } else
                        throw new ResponseStatusException(response.getStatusCode(), response.getStatusText());
                })
                .body(GithubUserRepo[].class)));
    }

    @Override
    public List<GithubUserBranch> findAll(String username, String repoName) {
        return List.of(Objects.requireNonNull(restClient.get()
                .uri("repos/" + username + "/{repoName}/branches", repoName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(GithubUserBranch[].class)));
    }
}