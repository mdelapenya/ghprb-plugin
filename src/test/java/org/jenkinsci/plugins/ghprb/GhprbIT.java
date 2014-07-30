package org.jenkinsci.plugins.ghprb;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Fail.fail;
import static org.kohsuke.github.GHIssueState.OPEN;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import hudson.plugins.git.BranchSpec;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.UserRemoteConfig;
import hudson.plugins.git.util.DefaultBuildChooser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.github.GHCommitPointer;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import antlr.ANTLRException;

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
    }

    private void mockPR(GHPullRequest prToMock,
                        GHCommitPointer commitPointer,
                        DateTime... updatedDate) throws MalformedURLException {
        given(prToMock.getHead()).willReturn(commitPointer);
        given(prToMock.getBase()).willReturn(commitPointer);
        given(prToMock.getUrl()).willReturn(new URL("http://127.0.0.1"));
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
    public void shouldFailOnNewPR() throws Exception {
        fail("Failing for testing purpose");
    }

    // Utility

    private GitSCM provideGitSCM() {
        return new GitSCM(
                "",
                newArrayList(new UserRemoteConfig("https://github.com/user/dropwizard", "", "+refs/pull/*:refs/remotes/origin/pr/*")),
                newArrayList(new BranchSpec("${sha1}")),
                null,
                false,
                null,
                false,
                false,
                new DefaultBuildChooser(),
                null,
                "",
                false,
                "",
                "",
                "",
                "",
                "",
                false,
                false,
                false,
                false,
                "",
                "",
                false,
                "",
                false
        );
    }

    private JSONObject provideConfiguration() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("serverAPIUrl", "https://api.github.com");
        jsonObject.put("username", "user");
        jsonObject.put("password", "1111");
        jsonObject.put("accessToken", "accessToken");
        jsonObject.put("adminlist", "user");
        jsonObject.put("publishedURL", "");
        jsonObject.put("requestForTestingPhrase", "test this");
        jsonObject.put("whitelistPhrase", "");
        jsonObject.put("okToTestPhrase", "ok to test");
        jsonObject.put("retestPhrase", "retest this please");
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
