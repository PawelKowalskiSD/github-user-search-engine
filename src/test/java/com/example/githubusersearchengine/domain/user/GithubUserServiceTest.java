package com.example.githubusersearchengine.domain.user;

import com.example.githubusersearchengine.domain.user.model.GithubUserBranch;
import com.example.githubusersearchengine.domain.user.model.GithubUserCommit;
import com.example.githubusersearchengine.domain.user.model.GithubUserOwner;
import com.example.githubusersearchengine.domain.user.model.GithubUserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubUserServiceTest {

    private final static String USERNAME = "Pawel";
    private final static String REPOSITORY_NAME = "TestRepo";

    @Mock
    private GithubClient githubClient;

    @InjectMocks
    private GithubUserService githubUserService;

    @Test
    void should_return_all_user_Repositories() {
        //Given
        List<GithubUserRepo> userRepos = getDataStub();
        when(githubUserService.getAllRepository(USERNAME)).thenReturn(userRepos);
        //When
        List<GithubUserRepo> result = githubUserService.getAllRepository(USERNAME);
        //Then
        assertNotNull(result);
        assertEquals(USERNAME, result.getFirst().owner().login());
        assertEquals(userRepos.getFirst().owner().login(), result.getFirst().owner().login());
        assertEquals(userRepos.size(), result.size());
    }

    @Test
    void should_return_all_branches() {
        //Given
        List<GithubUserRepo> userRepos = getDataStub();
        when(githubUserService.findAllBranch(USERNAME, REPOSITORY_NAME)).thenReturn(userRepos.get(0).branches());
        //When
        List<GithubUserBranch> result = githubUserService.findAllBranch(USERNAME, REPOSITORY_NAME);
        //Then
        assertNotNull(result);
        assertEquals(userRepos.getFirst().branches().getFirst().name(), result.getFirst().name());
        assertEquals(userRepos.getFirst().branches().getFirst().commit().sha(), result.getFirst().commit().sha());
        assertEquals(userRepos.getFirst().branches().size(), result.size());

        verify(githubClient, times(1)).findAll(USERNAME, REPOSITORY_NAME);
    }

    @Test
    void should_find_User_by_Username() {
        //Given
        List<GithubUserRepo> userRepos = getDataStub();
        when(githubUserService.findUserByUsername(USERNAME)).thenReturn(userRepos);
        //When
        List<GithubUserRepo> result = githubUserService.findUserByUsername(USERNAME);
        //Then
        assertNotNull(result);
        assertEquals(USERNAME, result.getFirst().owner().login());
    }

    private List<GithubUserRepo> getDataStub() {
        GithubUserCommit firstCommit = new GithubUserCommit("ff9627d2b314d02e9c2452b5afc850ca1480b542");
        GithubUserCommit secondCommit = new GithubUserCommit("ad9627d2b314d02e9c2452b5afc850ca1480b222");
        GithubUserCommit thirdCommit = new GithubUserCommit("cn9627d2b314d02e9c2452b5afc850ca1480b321");
        GithubUserBranch firstBranch = new GithubUserBranch("main", firstCommit);
        GithubUserBranch secondBranch = new GithubUserBranch("master", secondCommit);
        GithubUserBranch thirdBranch = new GithubUserBranch("test", thirdCommit);
        List<GithubUserBranch> branchList = List.of(firstBranch, secondBranch, thirdBranch);
        GithubUserRepo firsRepo = new GithubUserRepo(REPOSITORY_NAME, new GithubUserOwner(USERNAME), false, branchList);
        return List.of(firsRepo);
    }
}