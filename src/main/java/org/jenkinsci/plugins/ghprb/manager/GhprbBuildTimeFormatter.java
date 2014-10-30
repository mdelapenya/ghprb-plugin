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

import java.util.concurrent.TimeUnit;

public class GhprbBuildTimeFormatter {

	public GhprbBuildTimeFormatter(long millisecs) {
		TimeUnit tuMilliseconds = TimeUnit.MILLISECONDS;
		TimeUnit tuMinutes = TimeUnit.MINUTES;

		minutes = tuMilliseconds.toMinutes(millisecs);

		millisecs -= tuMinutes.toMillis(minutes);

		seconds = tuMilliseconds.toSeconds(millisecs);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(minutes);

		if (minutes == 1) {
			sb.append(" min ");
		}
		else {
			sb.append(" mins ");
		}

		sb.append(seconds);

		if (seconds == 1) {
			sb.append(" sec");
		}
		else {
			sb.append(" secs");
		}

		return sb.toString();
	}

	public long getMinutes() {
		return minutes;
	}

	public long getSeconds() {
		return seconds;
	}

	private long minutes;
	private long seconds;

}