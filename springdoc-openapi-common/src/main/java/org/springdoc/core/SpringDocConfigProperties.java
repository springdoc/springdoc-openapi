/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConfigurationProperties(prefix = "springdoc")
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocConfigProperties {

	private boolean showActuator;

	private Webjars webjars = new Webjars();

	private ApiDocs apiDocs = new ApiDocs();

	private List<String> packagesToScan;

	private List<String> packagesToExclude;

	private List<String> pathsToMatch;

	private List<String> pathsToExclude;

	private Cache cache = new Cache();

	private List<GroupConfig> groupConfigs = new ArrayList<>();

	private boolean autoTagClasses = true;

	private boolean modelAndViewAllowed;

	private boolean overrideWithGenericResponse = true;

	private String defaultConsumesMediaType = MediaType.APPLICATION_JSON_VALUE;

	private String defaultProducesMediaType = MediaType.ALL_VALUE;

	/**
	 * To override calculated server scheme in order to permit run on a https proxy
	 */
	private String calculatedServerScheme;

	public boolean isAutoTagClasses() {
		return autoTagClasses;
	}

	public void setAutoTagClasses(boolean autoTagClasses) {
		this.autoTagClasses = autoTagClasses;
	}

	public boolean isModelAndViewAllowed() {
		return modelAndViewAllowed;
	}

	public void setModelAndViewAllowed(boolean modelAndViewAllowed) {
		this.modelAndViewAllowed = modelAndViewAllowed;
	}

	public List<String> getPackagesToExclude() {
		return packagesToExclude;
	}

	public void setPackagesToExclude(List<String> packagesToExclude) {
		this.packagesToExclude = packagesToExclude;
	}

	public List<String> getPathsToExclude() {
		return pathsToExclude;
	}

	public void setPathsToExclude(List<String> pathsToExclude) {
		this.pathsToExclude = pathsToExclude;
	}

	public List<String> getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public boolean isShowActuator() {
		return showActuator;
	}

	public void setShowActuator(boolean showActuator) {
		this.showActuator = showActuator;
	}

	public Webjars getWebjars() {
		return webjars;
	}

	public void setWebjars(Webjars webjars) {
		this.webjars = webjars;
	}

	public ApiDocs getApiDocs() {
		return apiDocs;
	}

	public void setApiDocs(ApiDocs apiDocs) {
		this.apiDocs = apiDocs;
	}

	public List<String> getPathsToMatch() {
		return pathsToMatch;
	}

	public void setPathsToMatch(List<String> pathsToMatch) {
		this.pathsToMatch = pathsToMatch;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public String getCalculatedServerScheme() {
		return calculatedServerScheme;
	}

	public void setCalculatedServerScheme(String calculatedServerScheme) {
		this.calculatedServerScheme = calculatedServerScheme;
	}

	public static class Webjars {
		private String prefix = "/webjars";

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}

	public static class ApiDocs {
		/**
		 * Path to the generated OpenAPI documentation. For a yaml file, append ".yaml" to the path.
		 */
		private String path = Constants.DEFAULT_API_DOCS_URL;

		/**
		 * Weather to generate and serve a OpenAPI document.
		 */
		private boolean enabled = true;

		private Groups groups = new Groups();

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public Groups getGroups() {
			return groups;
		}

		public void setGroups(Groups groups) {
			this.groups = groups;
		}
	}

	public static class Groups {
		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class Cache {
		private boolean disabled;

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
	}

	public List<GroupConfig> getGroupConfigs() {
		return groupConfigs;
	}

	public void setGroupConfigs(List<GroupConfig> groupConfigs) {
		this.groupConfigs = groupConfigs;
	}

	public void addGroupConfig(GroupConfig groupConfigs) {
		this.groupConfigs.add(groupConfigs);
	}

	public static class GroupConfig {
		public GroupConfig() {
		}

		public GroupConfig(String group, List<String> pathsToMatch, List<String> packagesToScan, List<String> packagesToExclude, List<String> pathsToExclude) {
			this.pathsToMatch = pathsToMatch;
			this.pathsToExclude = pathsToExclude;
			this.packagesToExclude = packagesToExclude;
			this.packagesToScan = packagesToScan;
			this.group = group;
		}

		private List<String> pathsToMatch;

		private List<String> packagesToScan;

		private List<String> packagesToExclude;

		private List<String> pathsToExclude;

		private String group;

		public List<String> getPathsToMatch() {
			return pathsToMatch;
		}

		public void setPathsToMatch(List<String> pathsToMatch) {
			this.pathsToMatch = pathsToMatch;
		}

		public List<String> getPackagesToScan() {
			return packagesToScan;
		}

		public void setPackagesToScan(List<String> packagesToScan) {
			this.packagesToScan = packagesToScan;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public List<String> getPackagesToExclude() {
			return packagesToExclude;
		}

		public void setPackagesToExclude(List<String> packagesToExclude) {
			this.packagesToExclude = packagesToExclude;
		}

		public List<String> getPathsToExclude() {
			return pathsToExclude;
		}

		public void setPathsToExclude(List<String> pathsToExclude) {
			this.pathsToExclude = pathsToExclude;
		}
	}

	public String getDefaultConsumesMediaType() {
		return defaultConsumesMediaType;
	}

	public void setDefaultConsumesMediaType(String defaultConsumesMediaType) {
		this.defaultConsumesMediaType = defaultConsumesMediaType;
	}

	public String getDefaultProducesMediaType() {
		return defaultProducesMediaType;
	}

	public void setDefaultProducesMediaType(String defaultProducesMediaType) {
		this.defaultProducesMediaType = defaultProducesMediaType;
	}

	public boolean isOverrideWithGenericResponse() {
		return overrideWithGenericResponse;
	}

	public void setOverrideWithGenericResponse(boolean overrideWithGenericResponse) {
		this.overrideWithGenericResponse = overrideWithGenericResponse;
	}
}
