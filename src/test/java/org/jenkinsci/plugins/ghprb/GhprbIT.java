package org.jenkinsci.plugins.ghprb;

import antlr.ANTLRException;

import com.coravy.hudson.plugins.github.GithubProjectProperty;
import com.google.common.collect.Lists;

import hudson.model.FreeStyleProject;
import hudson.plugins.git.BranchSpec;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.UserRemoteConfig;
import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.github.GHCommitPointer;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.kohsuke.github.GHIssueState.OPEN;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class GhprbIT {

    private static final int INITIAL_RATE_LIMIT = 5000;

    private static final String GHPRB_PLUGIN_NAME = "ghprb";

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Mock
    private GhprbGitHub ghprbGitHub;
    @Mock
    private GitHub gitHub;
    @Mock
    private GHRepository ghRepository;
    @Mock
    private GHPullRequest ghPullRequest;
    @Mock
    private GHUser ghUser;
    @Mock
    private GHCommitPointer commitPointer;

    // Stubs
    private GHRateLimit ghRateLimit = new GHRateLimit();
    
    private void mockCommitList() {
    	PagedIterator itr = Mockito.mock(PagedIterator.class);
    	PagedIterable pagedItr = Mockito.mock(PagedIterable.class);
    	Mockito.when(ghPullRequest.listCommits()).thenReturn(pagedItr);
    	Mockito.when(pagedItr.iterator()).thenReturn(itr);
    	Mockito.when(itr.hasNext()).thenReturn(false);
	}

    @Before
    public void beforeTest() throws IOException, ANTLRException {

        given(ghprbGitHub.get()).willReturn(gitHub);
        given(gitHub.getRateLimit()).willReturn(ghRateLimit);
        given(gitHub.getRepository(anyString())).willReturn(ghRepository);
        given(commitPointer.getRef()).willReturn("ref");
        given(ghRepository.getName()).willReturn("dropwizard");
        mockPR(ghPullRequest, commitPointer, new DateTime(), new DateTime().plusDays(1));
        given(ghRepository.getPullRequests(eq(OPEN)))
                .willReturn(newArrayList(ghPullRequest))
                .willReturn(newArrayList(ghPullRequest));

        given(ghPullRequest.getUser()).willReturn(ghUser);
        given(ghUser.getEmail()).willReturn("email@email.com");
        given(ghUser.getLogin()).willReturn("user");
        ghRateLimit.remaining = INITIAL_RATE_LIMIT;
                
        mockCommitList();
    }

    private void mockPR(GHPullRequest prToMock,
                        GHCommitPointer commitPointer,
                        DateTime... updatedDate) throws MalformedURLException {
        given(prToMock.getHead()).willReturn(commitPointer);
        given(prToMock.getBase()).willReturn(commitPointer);
        given(prToMock.getUrl()).willReturn(new URL("http://127.0.0.1"));
        given(prToMock.getApiURL()).willReturn(new URL("http://127.0.0.1"));
        if (updatedDate.length > 1) {
            given(prToMock.getUpdatedAt()).willReturn(updatedDate[0].toDate())
                    .willReturn(updatedDate[0].toDate())
                    .willReturn(updatedDate[1].toDate())
                    .willReturn(updatedDate[1].toDate())
                    .willReturn(updatedDate[1].toDate());
        } else {
            given(prToMock.getUpdatedAt()).willReturn(updatedDate[0].toDate());
        }
    }

    @Test
    public void shouldFailOnPurpose() throws Exception {
        org.fest.assertions.Fail.fail("Failing on purpose");
    }

    @Test
    public void shouldFailOnPurpose2() throws Exception {
        org.fest.assertions.Fail.fail("Failing on purpose");
    }

    @Test
    public void shouldFailOnPurpose3() throws Exception {
        org.fest.assertions.Fail.fail("Failing on purpose");
    }

    // Utility

    private GitSCM provideGitSCM() {
        return new GitSCM(
                newArrayList(new UserRemoteConfig("https://github.com/user/dropwizard", "", "+refs/pull/*:refs/remotes/origin/pr/*", "")),
                newArrayList(new BranchSpec("${sha1}")),
                false,
                null,
                null,
                "",
                null
        );
    }

    private JSONObject provideConfiguration() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("serverAPIUrl", "https://api.github.com");
        jsonObject.put("username", "user");
        jsonObject.put("password", "1111");
        jsonObject.put("accessToken", "accessToken");
        jsonObject.put("adminlist", "user");
        jsonObject.put("allowMembersOfWhitelistedOrgsAsAdmin", "false");
        jsonObject.put("publishedURL", "");
        jsonObject.put("requestForTestingPhrase", "test this");
        jsonObject.put("whitelistPhrase", "");
        jsonObject.put("okToTestPhrase", "ok to test");
        jsonObject.put("retestPhrase", "retest this please");
        jsonObject.put("skipBuildPhrase", "[skip ci]");
        jsonObject.put("cron", "*/1 * * * *");
        jsonObject.put("useComments", "true");
        jsonObject.put("logExcerptLines", "0");
        jsonObject.put("unstableAs", "");
        jsonObject.put("testMode", "true");
        jsonObject.put("autoCloseFailedPullRequests", "false");
        jsonObject.put("displayBuildErrorsOnDownstreamBuilds", "false");
        jsonObject.put("msgSuccess", "Success");
        jsonObject.put("msgFailure", "Failure");

        return jsonObject;
    }
}
