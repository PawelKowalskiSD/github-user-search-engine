package com.example.githubusersearchengine.controller;

import com.example.githubusersearchengine.controller.dto.GithubUserRepoDto;
import com.example.githubusersearchengine.domain.user.GithubUserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/user")
public class GithubUserController {
    private final GithubUserService githubUserService;
    private final ConversionService conversionService;

    public GithubUserController(GithubUserService githubUserService, ConversionService conversionService) {
        this.githubUserService = githubUserService;
        this.conversionService = conversionService;
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<List<GithubUserRepoDto>> getUserRepositories(@PathVariable String username) {
        return ResponseEntity
                .ok()
                .body(
                        githubUserService.getAllRepository(username).stream()
                                .map(githubUserRepo -> conversionService.convert(githubUserRepo, GithubUserRepoDto.class))
                                .collect(Collectors.toList()));
    }
}