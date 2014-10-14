package org.jenkinsci.plugins.ghprb.manager;

import java.util.Iterator;

/**
 * @author mdelapenya (Manuel de la Peña)
 */
public interface GhprbBuildManager {

	/**
	 * Calculate the build URL of a build
	 * 
	 * @return the build URL
	 */
	String calculateBuildUrl();

	/**
	 * Returns downstream builds as an iterator
	 * 
	 * @return the iterator
	 */
	Iterator downstreamProjects();

	/**
	 * Returns the formatted build time of a build
	 * 
	 * @return the build time as string
	 */
	String getBuildTimeMessage();

	/**
	 * Print tests result of a build
	 *
	 * @return the tests result
	 */
	String getTestResults();

}