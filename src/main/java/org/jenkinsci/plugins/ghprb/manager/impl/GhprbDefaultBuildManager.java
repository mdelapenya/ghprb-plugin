package org.jenkinsci.plugins.ghprb.manager.impl;

import org.jenkinsci.plugins.ghprb.manager.configuration.JobConfiguration;

import hudson.model.AbstractBuild;

/**
 * @author mdelapenya (Manuel de la Peña)
 */
public class GhprbDefaultBuildManager extends GhprbBaseBuildManager {

	public GhprbDefaultBuildManager(AbstractBuild build) {
		super(build);
	}

	public GhprbDefaultBuildManager(
		AbstractBuild build, JobConfiguration jobConfiguration) {

		super(build, jobConfiguration);
	}

}