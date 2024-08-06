package com.example.githubusersearchengine.domain.user.model;

import java.util.List;

public record GithubUserRepo(String name, GithubUserOwner owner, boolean fork, List<GithubUserBranch> branches) {
}