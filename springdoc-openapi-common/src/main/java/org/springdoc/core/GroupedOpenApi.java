/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;

import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.GROUP_NAME_NOT_NULL;

/**
 * The type Grouped open api.
 * @author bnasslahsen
 */
public class GroupedOpenApi {

	/**
	 * The Group.
	 */
	private final String group;

	/**
	 * The Open api customisers.
	 */
	private final List<OpenApiCustomiser> openApiCustomisers;

	/**
	 * The Operation customizers.
	 */
	private final List<OperationCustomizer> operationCustomizers;

	/**
	 * The Paths to match.
	 */
	private final List<String> pathsToMatch;

	/**
	 * The Packages to scan.
	 */
	private final List<String> packagesToScan;

	/**
	 * The Packages to exclude.
	 */
	private final List<String> packagesToExclude;

	/**
	 * The Paths to exclude.
	 */
	private final List<String> pathsToExclude;

	/**
	 * Instantiates a new Grouped open api.
	 *
	 * @param builder the builder
	 */
	private GroupedOpenApi(Builder builder) {
		this.group = Objects.requireNonNull(builder.group, GROUP_NAME_NOT_NULL);
		this.pathsToMatch = builder.pathsToMatch;
		this.packagesToScan = builder.packagesToScan;
		this.packagesToExclude = builder.packagesToExclude;
		this.pathsToExclude = builder.pathsToExclude;
		this.openApiCustomisers = Objects.requireNonNull(builder.openApiCustomisers);
		this.operationCustomizers = Objects.requireNonNull(builder.operationCustomizers);
		if (CollectionUtils.isEmpty(this.pathsToMatch)
				&& CollectionUtils.isEmpty(this.packagesToScan)
				&& CollectionUtils.isEmpty(this.pathsToExclude)
				&& CollectionUtils.isEmpty(this.packagesToExclude)
				&& CollectionUtils.isEmpty(openApiCustomisers)
				&& CollectionUtils.isEmpty(operationCustomizers))
			throw new IllegalStateException("Packages to scan or paths to filter or openApiCustomisers/operationCustomizers can not be all null for the group:" + this.group);
	}

	/**
	 * Builder builder.
	 *
	 * @return the builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Gets group.
	 *
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Gets paths to match.
	 *
	 * @return the paths to match
	 */
	public List<String> getPathsToMatch() {
		return pathsToMatch;
	}

	/**
	 * Gets packages to scan.
	 *
	 * @return the packages to scan
	 */
	public List<String> getPackagesToScan() {
		return packagesToScan;
	}

	/**
	 * Gets packages to exclude.
	 *
	 * @return the packages to exclude
	 */
	public List<String> getPackagesToExclude() {
		return packagesToExclude;
	}

	/**
	 * Gets paths to exclude.
	 *
	 * @return the paths to exclude
	 */
	public List<String> getPathsToExclude() {
		return pathsToExclude;
	}

	/**
	 * Gets open api customisers.
	 *
	 * @return the open api customisers
	 */
	public List<OpenApiCustomiser> getOpenApiCustomisers() {
		return openApiCustomisers;
	}

	/**
	 * Gets operation customizers.
	 *
	 * @return the operation customizers
	 */
	public List<OperationCustomizer> getOperationCustomizers() {
		return operationCustomizers;
	}

	/**
	 * The type Builder.
	 * @author bnasslahsen
	 */
	public static class Builder {
		/**
		 * The Open api customisers.
		 */
		private final List<OpenApiCustomiser> openApiCustomisers = new ArrayList<>();

		/**
		 * The Operation customizers.
		 */
		private final List<OperationCustomizer> operationCustomizers = new ArrayList<>();

		/**
		 * The Group.
		 */
		private String group;

		/**
		 * The Paths to match.
		 */
		private List<String> pathsToMatch;

		/**
		 * The Packages to scan.
		 */
		private List<String> packagesToScan;

		/**
		 * The Packages to exclude.
		 */
		private List<String> packagesToExclude;

		/**
		 * The Paths to exclude.
		 */
		private List<String> pathsToExclude;

		/**
		 * Instantiates a new Builder.
		 */
		private Builder() {
			// use static factory method in parent class
		}

		/**
		 * Sets group.
		 *
		 * @param group the group
		 * @return the group
		 * @deprecated Since v1.4.0, GroupedOpenApi.setGroup is marked as deprecated. Use {@link #group(String) } instead.
		 * will be removed with v1.5.0
		 */
		@Deprecated
		public Builder setGroup(String group) {
			return this.group(group);
		}

		/**
		 * Group builder.
		 *
		 * @param group the group
		 * @return the builder
		 */
		public Builder group(String group) {
			this.group = group;
			return this;
		}

		/**
		 * Paths to match builder.
		 *
		 * @param pathsToMatch the paths to match
		 * @return the builder
		 */
		public Builder pathsToMatch(String... pathsToMatch) {
			this.pathsToMatch = Arrays.asList(pathsToMatch);
			return this;
		}

		/**
		 * Packages to scan builder.
		 *
		 * @param packagesToScan the packages to scan
		 * @return the builder
		 */
		public Builder packagesToScan(String... packagesToScan) {
			this.packagesToScan = Arrays.asList(packagesToScan);
			return this;
		}

		/**
		 * Paths to exclude builder.
		 *
		 * @param pathsToExclude the paths to exclude
		 * @return the builder
		 */
		public Builder pathsToExclude(String... pathsToExclude) {
			this.pathsToExclude = Arrays.asList(pathsToExclude);
			return this;
		}

		/**
		 * Packages to exclude builder.
		 *
		 * @param packagesToExclude the packages to exclude
		 * @return the builder
		 */
		public Builder packagesToExclude(String... packagesToExclude) {
			this.packagesToExclude = Arrays.asList(packagesToExclude);
			return this;
		}

		/**
		 * Add open api customiser builder.
		 *
		 * @param openApiCustomiser the open api customiser
		 * @return the builder
		 */
		public Builder addOpenApiCustomiser(OpenApiCustomiser openApiCustomiser) {
			this.openApiCustomisers.add(openApiCustomiser);
			return this;
		}

		/**
		 * Add operation customizer builder.
		 *
		 * @param operationCustomizer the operation customizer
		 * @return the builder
		 */
		public Builder addOperationCustomizer(OperationCustomizer operationCustomizer) {
			this.operationCustomizers.add(operationCustomizer);
			return this;
		}

		/**
		 * Build grouped open api.
		 *
		 * @return the grouped open api
		 */
		public GroupedOpenApi build() {
			return new GroupedOpenApi(this);
		}
	}
}
