package com.example.githubusersearchengine.domain.user;

import com.example.githubusersearchengine.domain.user.model.GithubUserBranch;
import com.example.githubusersearchengine.domain.user.model.GithubUserRepo;

import java.util.List;

public interface GithubClient {
    List<GithubUserRepo> findAll(String username);
    List<GithubUserBranch> findAll(String username, String repoName);
}