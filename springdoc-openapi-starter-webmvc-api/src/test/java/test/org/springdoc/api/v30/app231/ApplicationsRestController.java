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
