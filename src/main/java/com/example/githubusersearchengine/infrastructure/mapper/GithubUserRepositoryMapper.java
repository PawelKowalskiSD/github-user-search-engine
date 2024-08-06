package com.example.githubusersearchengine.infrastructure.mapper;

import com.example.githubusersearchengine.controller.dto.GithubUserBranchDto;
import com.example.githubusersearchengine.controller.dto.GithubUserCommitDto;
import com.example.githubusersearchengine.controller.dto.GithubUserOwnerDto;
import com.example.githubusersearchengine.controller.dto.GithubUserRepoDto;
import com.example.githubusersearchengine.domain.user.model.GithubUserRepo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GithubUserRepositoryMapper implements Converter<GithubUserRepo, GithubUserRepoDto> {

    @Override
    public GithubUserRepoDto convert(GithubUserRepo source) {
        return new GithubUserRepoDto(
                source.name(),
                new GithubUserOwnerDto(
                        source.owner()
                                .login()
                ),
                source.branches().stream()
                        .parallel()
                        .map(b -> new GithubUserBranchDto(
                                b.name(),
                                new GithubUserCommitDto(
                                        b.commit()
                                                .sha())
                        ))
                        .collect(Collectors.toList()));
    }
}