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

import java.util.HashMap;
import java.util.Map;

/**
 * The interface Spring doc version strategy.
 *
 * @author bnasslahsen
 */
public abstract sealed class SpringDocVersionStrategy permits HeaderVersionStrategy, MediaTypeVersionStrategy, PathVersionStrategy, QueryParamVersionStrategy {

	/**
	 * The Version.
	 */
	protected String version;

	/**
	 * The Default version.
	 */
	protected final String defaultVersion;

	/**
	 * The Default values.
	 */
	protected Map<String, String > versionDefaultMap = new HashMap<>();

	/**
	 * Instantiates a new Spring doc version strategy.
	 *
	 * @param defaultVersion the default version
	 */
	protected SpringDocVersionStrategy(String defaultVersion) {
		this.defaultVersion = defaultVersion;
	}

	/**
	 * Gets version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets version.
	 *
	 * @param version the version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Update version.
	 *
	 * @param version the version
	 * @param params  the params
	 */
	public void updateVersion(String version, String[] params) {
		setVersion(version);
	}

	/**
	 * Update query params map.
	 *
	 * @param queryParams the query params
	 * @return the map
	 */
	public Map<String, String> updateQueryParams(Map<String, String> queryParams) {
		return queryParams;
	}

	/**
	 * Gets operation path.
	 *
	 * @param operationPath the operation path
	 * @param version       the version
	 * @return the operation path
	 */
	public String updateOperationPath(String operationPath, String version) {
		return operationPath;
	}

	public Map<String, String> getVersionDefaultMap() {
		return versionDefaultMap;
	}
}
