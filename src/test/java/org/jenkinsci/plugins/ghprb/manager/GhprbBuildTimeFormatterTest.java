/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.jenkinsci.plugins.ghprb.manager;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GhprbBuildTimeFormatterTest {

	@Test
	public void testConstructor() {
		GhprbBuildTimeFormatter buildTimeFormatter =
			new GhprbBuildTimeFormatter(1);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(0);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(0);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(0);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(1);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 60);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(1);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(0);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 61);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(1);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(1);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3600);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(60);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(0);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3601);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(60);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(1);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3659);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(60);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(59);

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3660);

		assertThat(buildTimeFormatter.getMinutes()).isEqualTo(61);
		assertThat(buildTimeFormatter.getSeconds()).isEqualTo(0);
	}

	@Test
	public void testToString() {
		GhprbBuildTimeFormatter buildTimeFormatter =
			new GhprbBuildTimeFormatter(1);

		assertThat(buildTimeFormatter.toString()).isEqualTo("0 mins 0 secs");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND);

		assertThat(buildTimeFormatter.toString()).isEqualTo("0 mins 1 sec");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 60);

		assertThat(buildTimeFormatter.toString()).isEqualTo("1 min 0 secs");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 61);

		assertThat(buildTimeFormatter.toString()).isEqualTo("1 min 1 sec");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3600);

		assertThat(buildTimeFormatter.toString()).isEqualTo("60 mins 0 secs");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3601);

		assertThat(buildTimeFormatter.toString()).isEqualTo("60 mins 1 sec");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3659);

		assertThat(buildTimeFormatter.toString()).isEqualTo("60 mins 59 secs");

		buildTimeFormatter = new GhprbBuildTimeFormatter(MILLISECOND * 3660);

		assertThat(buildTimeFormatter.toString()).isEqualTo("61 mins 0 secs");
	}

	private static final long MILLISECOND = 1000L;

}