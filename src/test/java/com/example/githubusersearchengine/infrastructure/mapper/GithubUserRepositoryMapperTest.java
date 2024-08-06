package com.example.githubusersearchengine.infrastructure.mapper;

import com.example.githubusersearchengine.controller.dto.GithubUserRepoDto;
import com.example.githubusersearchengine.domain.user.model.GithubUserBranch;
import com.example.githubusersearchengine.domain.user.model.GithubUserCommit;
import com.example.githubusersearchengine.domain.user.model.GithubUserOwner;
import com.example.githubusersearchengine.domain.user.model.GithubUserRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GithubUserRepositoryMapperTest {

    @Test
    void should_convert_github_user_dto_to_github_user() {
        //Given
        GithubUserRepositoryMapper githubUserRepositoryMapper = new GithubUserRepositoryMapper();
        String repoName = "RepoTest";
        GithubUserOwner userOwner = new GithubUserOwner("Pawel");
        GithubUserCommit userCommit = new GithubUserCommit("ff9627d2b314d02e9c2452b5afc850ca1480b542");
        List<GithubUserBranch> branchList = List.of(new GithubUserBranch("main", userCommit));
        GithubUserRepo requestUserRepo = new GithubUserRepo(repoName, userOwner, false, branchList);
        //When
        GithubUserRepoDto response = githubUserRepositoryMapper.convert(requestUserRepo);
        //Then
        assertNotNull(response);
        assertEquals(repoName, response.name());
        assertEquals(userOwner.login(), response.owner().login());
        assertEquals(branchList.size(), response.branch().size());
        assertEquals(branchList.getFirst().name(), response.branch().getFirst().name());
        assertEquals(userCommit.sha(), response.branch().getFirst().commit().sha());
    }
}