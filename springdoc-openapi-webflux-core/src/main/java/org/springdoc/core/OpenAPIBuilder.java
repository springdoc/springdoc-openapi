package org.springdoc.core;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;

@Component
public class OpenAPIBuilder {

	private OpenAPI openAPI;
	private Components components;
	private Paths paths;

	private OpenAPIBuilder() {
		super();
		this.openAPI = new OpenAPI();
		this.components = new Components();
		this.openAPI.setComponents(components);
		this.paths = new Paths();
	}

	public OpenAPI getOpenAPI() {
		return openAPI;
	}

	public void setOpenAPI(OpenAPI openAPI) {
		this.openAPI = openAPI;
	}

	public Components getComponents() {
		return components;
	}

	public void setComponents(Components components) {
		this.components = components;
	}

	public Paths getPaths() {
		return paths;
	}

	public void setPaths(Paths paths) {
		this.paths = paths;
	}

}
