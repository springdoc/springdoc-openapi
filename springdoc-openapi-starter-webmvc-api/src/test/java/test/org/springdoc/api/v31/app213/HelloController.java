package test.org.springdoc.api.v31.app213;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author bnasslahsen
 */
@OpenAPIDefinition(
		info = @Info(

				extensions = { @Extension(
						name = "${api.extensions.name}",
						properties = {
								@ExtensionProperty(name = "type", value = "${api.extensions.properties.type}"),
								@ExtensionProperty(name = "connectionId", value = "${api.extensions.properties.connectionId}"),
								@ExtensionProperty(name = "httpMethod", value = "GET"),
								@ExtensionProperty(
										name = "uri",
										value = "${api.extensions.properties.uri}/testcontroller/getTest"),
								@ExtensionProperty(name = "passthroughBehavior", value = "${api.extensions.properties.passthroughBehavior}"),
								@ExtensionProperty(name = "connectionType", value = "${api.extensions.properties.connectionType}") }) },

				title = "${api.info.title}",
				version = "${api.info.version}",
				description = "${api.info.description}",
				termsOfService = "${api.info.termsOfService}"),
		servers = { @Server(description = "${api.server.description}", url = "${api.server.url}") })
@Tag(name = "${api.tag.name}", description = "${api.tag.description}")
@RestController
@RequestMapping(value = "${api.base-request-mapping}/testcontroller", produces = MediaType.APPLICATION_JSON_VALUE)
public class HelloController {

	@PostMapping(path = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	@Operation(
			summary = "Get Test",
			description = "Get Test",
			extensions = { @Extension(
					name = "${api.extensions.name}",
					properties = {
							@ExtensionProperty(name = "type", value = "${api.extensions.properties.type}"),
							@ExtensionProperty(name = "connectionId", value = "${api.extensions.properties.connectionId}"),
							@ExtensionProperty(name = "httpMethod", value = "GET"),
							@ExtensionProperty(
									name = "uri",
									value = "${api.extensions.properties.uri}/testcontroller/getTest"),
							@ExtensionProperty(name = "passthroughBehavior", value = "${api.extensions.properties.passthroughBehavior}"),
							@ExtensionProperty(name = "connectionType", value = "${api.extensions.properties.connectionType}") }) })

	public PersonDTO queryMyDto() {
		// This return a PageImpl with the data, the method parameter 'query' is a pojo containg filter properties
		return null;
	}

}