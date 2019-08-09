package org.springdoc.core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;

@Component
public class OpenAPIBuilder {

	@Autowired(required = false)
	private OpenAPI openAPI;

	private OpenAPIBuilder() {
		super();
	}

	@PostConstruct
	public void init() {
		if (openAPI == null) {
			this.openAPI = new OpenAPI();
			this.openAPI.setComponents(new Components());
			this.openAPI.setPaths(new Paths());
		} else {
			if (openAPI.getComponents() == null)
				this.openAPI.setComponents(new Components());
			if (openAPI.getPaths() == null)
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
