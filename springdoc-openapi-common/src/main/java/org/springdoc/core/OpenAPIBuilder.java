package org.springdoc.core;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;

@Component
public class OpenAPIBuilder {

	private OpenAPI openAPI;

	public OpenAPIBuilder(Optional<OpenAPI> openAPI) {
		if (openAPI.isPresent()) {
			this.openAPI = openAPI.get();
			if (this.openAPI.getComponents() == null)
				this.openAPI.setComponents(new Components());
			if (this.openAPI.getPaths() == null)
				this.openAPI.setPaths(new Paths());
		} else {
			this.openAPI = new OpenAPI();
			this.openAPI.setComponents(new Components());
			this.openAPI.setPaths(new Paths());
		}
	}

	public OpenAPI getOpenAPI() {
		return openAPI;
	}

	public Components getComponents() {
		return openAPI.getComponents();
	}

	public Paths getPaths() {
		return openAPI.getPaths();
	}

}
