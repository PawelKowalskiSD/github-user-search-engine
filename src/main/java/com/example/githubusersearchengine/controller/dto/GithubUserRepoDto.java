package com.example.githubusersearchengine.controller.dto;

import java.util.List;

public record GithubUserRepoDto(String name, GithubUserOwnerDto owner, List<GithubUserBranchDto> branch) {
}