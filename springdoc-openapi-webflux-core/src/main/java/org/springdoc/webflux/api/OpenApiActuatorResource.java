package org.springdoc.webflux.api;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;

@RestControllerEndpoint(id = DEFAULT_API_DOCS_ACTUATOR_URL)
public class OpenApiActuatorResource extends OpenApiResource {

	public OpenApiActuatorResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
	}

	public OpenApiActuatorResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
	}

	@Operation(hidden = true)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> openapiJson(ServerHttpRequest serverHttpRequest)
			throws JsonProcessingException {
		return super.openapiJson(serverHttpRequest, "");
	}


	@Operation(hidden = true)
	@GetMapping(value = "/yaml", produces = APPLICATION_OPENAPI_YAML)
	public Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest)
			throws JsonProcessingException {
		return super.openapiYaml(serverHttpRequest, "yaml");
	}

	protected void calculateServerUrl(ServerHttpRequest serverHttpRequest, String apiDocsUrl) {
		super.initOpenAPIBuilder();
		ActuatorProvider actuatorProvider = optionalActuatorProvider.get();
		String scheme = (serverHttpRequest.getURI().getScheme() != null) ? serverHttpRequest.getURI().getScheme() : "http";
		String host = (serverHttpRequest.getURI().getHost() != null) ? serverHttpRequest.getURI().getHost() : "localhost";
		String path;
		int port;
		if (ACTUATOR_DEFAULT_GROUP.equals(this.groupName)) {
			port = actuatorProvider.getActuatorPort();
			path = actuatorProvider.getActuatorPath();
		}
		else {
			port = actuatorProvider.getApplicationPort();
			path = actuatorProvider.getContextPath();
		}
		String calculatedUrl = scheme + "://" + host + ":" + port + path;
		openAPIService.setServerBaseUrl(calculatedUrl);
	}

}
