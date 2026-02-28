/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

/**
 * The type Header version strategy.
 *
 * @author bnasslahsen
 */
public final class HeaderVersionStrategy extends SpringDocVersionStrategy {

	/**
	 * The Header name.
	 */
	private final String headerName;


	/**
	 * Instantiates a new Header version strategy.
	 *
	 * @param headerName the header name
	 */
	public HeaderVersionStrategy(String headerName, String defaultVersion) {
		super(defaultVersion);
		this.headerName = headerName;
		if(defaultVersion != null)
			versionDefaultMap.put(headerName, defaultVersion);
	}

	/**
	 * Gets header name.
	 *
	 * @return the header name
	 */
	public String getHeaderName() {
		return headerName;
	}
}