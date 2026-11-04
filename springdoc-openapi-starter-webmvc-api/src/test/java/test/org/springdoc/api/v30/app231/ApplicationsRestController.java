/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v30.app231;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/applications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationsRestController {

	@Operation(summary = "Parameter object")
	@PostMapping("/parameter-object")
	public Application createWithParameterObject(
			@RequestBody @ParameterObject SubClass request
	) {
		return new Application();
	}

	static class Application {
		private String name = "unnamed_application";
		private String serviceName;
		private String clusterName;
		private String shardName;
		private Map<String, String> customTags = new HashMap();

		public String getServiceName() {
			return this.serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClusterName() {
			return this.clusterName;
		}

		public void setClusterName(String clusterName) {
			this.clusterName = clusterName;
		}

		public String getShardName() {
			return this.shardName;
		}

		public void setShardName(String shardName) {
			this.shardName = shardName;
		}

		public Map<String, String> getCustomTags() {
			return this.customTags;
		}

		public void setCustomTags(Map<String, String> customTags) {
			this.customTags = customTags;
		}
	}

}
