package org.jenkinsci.plugins.ghprb.manager.impl;

import static org.fest.assertions.Assertions.assertThat;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import com.coravy.hudson.plugins.github.GithubProjectProperty;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;

import net.sf.json.JSONObject;

import org.jenkinsci.plugins.ghprb.Ghprb;
import org.jenkinsci.plugins.ghprb.GhprbITBaseTestCase;
import org.jenkinsci.plugins.ghprb.GhprbTestUtil;
import org.jenkinsci.plugins.ghprb.GhprbTrigger;
import org.jenkinsci.plugins.ghprb.manager.GhprbBuildManager;
import org.jenkinsci.plugins.ghprb.manager.factory.GhprbBuildManagerFactoryUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;

import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author mdelapenya (Manuel de la Peña)
 */
@RunWith(MockitoJUnitRunner.class)
public class GhprbFreeStyleBuildManagerTest extends GhprbITBaseTestCase {

	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();

	@Before
	public void setUp() throws Exception {
		super.beforeTest();
	}

	@Test
	public void shouldCalculateUrlFromFreeStyleProject() throws Exception {
		// GIVEN
		FreeStyleProject project = givenThatGhprbHasBeenTriggeredForAFreeStyleProject();

		// THEN
		assertThat(project.getBuilds().toArray().length).isEqualTo(1);

		FreeStyleBuild freeStyleBuild = project.getBuilds().getLastBuild();

		GhprbBuildManager buildManager =
			GhprbBuildManagerFactoryUtil.getBuildManager(freeStyleBuild);

		assertThat(buildManager).isInstanceOf(GhprbDefaultBuildManager.class);

		assertThat(buildManager.calculateBuildUrl()).isEqualTo(
			"defaultPublishedURL/" + freeStyleBuild.getUrl());
	}

	private FreeStyleProject givenThatGhprbHasBeenTriggeredForAFreeStyleProject() throws Exception {
		FreeStyleProject project = jenkinsRule.createFreeStyleProject("FSPRJ");

		GhprbTrigger trigger = new GhprbTrigger("user", "user", "",
			"*/1 * * * *", "retest this please", false, false, false, false,
			false, null, null, false, null, null);

		given(commitPointer.getSha()).willReturn("sha");

		JSONObject jsonObject = GhprbTestUtil.provideConfiguration();

		jsonObject.put("publishedURL", "defaultPublishedURL");

		jenkinsRule.getPluginManager()
			.getPlugin(GhprbTestUtil.GHPRB_PLUGIN_NAME)
			.getPlugin()
			.configure(null, jsonObject);

		project.addProperty(new GithubProjectProperty("https://github.com/user/dropwizard"));

		given(ghPullRequest.getNumber()).willReturn(1);

		// Creating spy on ghprb, configuring repo
		Ghprb ghprb = spyCreatingGhprb(trigger, project);

		doReturn(ghprbGitHub).when(ghprb).getGitHub();

		setRepositoryHelper(ghprb);

		given(ghRepository.getPullRequest(1)).willReturn(ghPullRequest);

		// Configuring and adding Ghprb trigger
		project.addTrigger(trigger);

		project.getTriggers().keySet().iterator().next()
			.configure(null, jsonObject);

		// Configuring Git SCM
		project.setScm(GhprbTestUtil.provideGitSCM());

		trigger.start(project, true);

		setTriggerHelper(trigger, ghprb);

		// THEN
		Thread.sleep(130000);

		return project;
	}
}