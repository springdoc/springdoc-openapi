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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Query param version strategy.
 *
 * @author bnasslahsen
 */
public class QueryParamVersionStrategy extends SpringDocVersionStrategy {

	/**
	 * The Parameter name.
	 */
	private final String parameterName;

	/**
	 * Instantiates a new Query param version strategy.
	 *
	 * @param parameterName the parameter name
	 */
	public QueryParamVersionStrategy(String parameterName, String defaultVersion) {
		super(defaultVersion);
		this.parameterName = parameterName;
		if(defaultVersion != null)
			versionDefaultMap.put(parameterName, defaultVersion);
	}

	/**
	 * Gets parameter name.
	 *
	 * @return the parameter name
	 */
	public String getParameterName() {
		return parameterName;
	}


	@Override
	public void updateVersion(String version, String[] params) {
		for (String param : params) {
			if (param.contains("=")) {
				String[] paramValues = param.split("=", 2);
				String paramName = paramValues[0];
				String paramValue = paramValues[1];
				if (parameterName.equals(paramName)) {
					setVersion(paramValue);
					break;
				}
			}
		}
	}

	@Override
	public Map<String, String> updateQueryParams(Map<String, String> queryParams) {
		if (queryParams == null)
			queryParams = new LinkedHashMap<>();
		if(version !=null)
			queryParams.put(parameterName, version);
		return queryParams;
	}
}
