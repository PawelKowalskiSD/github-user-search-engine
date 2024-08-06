package com.example.githubusersearchengine.domain.user;

import com.example.githubusersearchengine.domain.user.model.GithubUserBranch;
import com.example.githubusersearchengine.domain.user.model.GithubUserCommit;
import com.example.githubusersearchengine.domain.user.model.GithubUserRepo;

import java.util.List;
import java.util.stream.Collectors;

public class GithubUserService {

    private final GithubClient githubClient;

    public GithubUserService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<GithubUserRepo> getAllRepository(String name) {
        return findUserByUsername(name).stream()
                .parallel()
                .filter(githubUserRepo -> !githubUserRepo.fork())
                .map(b -> new GithubUserRepo(
                        b.name(),
                        b.owner(),
                        false,
                        getBranchForRepo(name, b.name())
                ))
                .collect(Collectors.toList());
    }

    public List<GithubUserBranch> findAllBranch(String username, String repoName) {
        return githubClient.findAll(username, repoName);
    }

    public List<GithubUserRepo> findUserByUsername(String username) {
        return githubClient.findAll(username);
    }

    private List<GithubUserBranch> getBranchForRepo(String username, String repoName) {
        return findAllBranch(username, repoName).stream()
                .parallel()
                .map(branch -> new GithubUserBranch(
                        branch.name(),
                        new GithubUserCommit(
                                branch.commit().sha())
                ))
                .collect(Collectors.toList());
    }
}