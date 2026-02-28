/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */
package org.springdoc.core.versions;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Path version strategy.
 *
 * @author bnasslahsen
 */
public final class PathVersionStrategy extends SpringDocVersionStrategy {

	/**
	 * The Path segment index.
	 */
	private final int pathSegmentIndex;

	/**
	 * Instantiates a new Path version strategy.
	 *
	 * @param pathSegmentIndex the path segment index
	 */
	public PathVersionStrategy(int pathSegmentIndex, String defaultVersion) {
		super(defaultVersion);
		this.pathSegmentIndex = pathSegmentIndex;
	}

	/**
	 * Gets path segment index.
	 *
	 * @return the path segment index
	 */
	public int getPathSegmentIndex() {
		return pathSegmentIndex;
	}


	@Override
	public String updateOperationPath(String operationPath, String version) {
		if (operationPath == null || version == null) {
			return operationPath;
		}
		String[] segments = operationPath.split("/");
		List<String> updatedSegments = new ArrayList<>();
		int segmentCount = 0;

		for (String segment : segments) {
			if (segment.isEmpty()) {
				continue;
			}

			if (segmentCount == pathSegmentIndex) {
				String newSegment = version;
				if (segment.contains("{")) {
					// Extract static prefix before placeholder
					int placeholderStart = segment.indexOf('{');
					String prefix = segment.substring(0, placeholderStart);
					newSegment = prefix + version;
				}
				updatedSegments.add(newSegment);
			} else {
				updatedSegments.add(segment);
			}
			segmentCount++;
		}
		return "/" + String.join("/", updatedSegments);
	}

}



