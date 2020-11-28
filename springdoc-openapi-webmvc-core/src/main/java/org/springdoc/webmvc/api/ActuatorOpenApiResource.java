package org.springdoc.webmvc.api;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.RepositoryRestResourceProvider;
import org.springdoc.core.SecurityOAuth2Provider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;

@RestControllerEndpoint(id = DEFAULT_API_DOCS_ACTUATOR_URL)
public class ActuatorOpenApiResource extends WebMvcOpenApiResource {

	public ActuatorOpenApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
			AbstractRequestService requestBuilder, GenericResponseService responseBuilder,
			OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<ActuatorProvider> actuatorProvider,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider,
			Optional<RouterFunctionProvider> routerFunctionProvider,
			Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, actuatorProvider, operationCustomizers, openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider, routerFunctionProvider, repositoryRestResourceProvider);
	}

	public ActuatorOpenApiResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
			AbstractRequestService requestBuilder, GenericResponseService responseBuilder,
			OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<ActuatorProvider> actuatorProvider, Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties,
			Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider, Optional<RouterFunctionProvider> routerFunctionProvider,
			Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser,
				requestMappingHandlerMapping, actuatorProvider, operationCustomizers, openApiCustomisers,
				springDocConfigProperties, springSecurityOAuth2Provider, routerFunctionProvider, repositoryRestResourceProvider);
	}

	@Operation(hidden = true)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public String openapiJson(HttpServletRequest request)
			throws JsonProcessingException {
		return super.openapiJson(request, "");
	}


	@Operation(hidden = true)
	@GetMapping(value = "/yaml", produces = APPLICATION_OPENAPI_YAML)
	public String openapiYaml(HttpServletRequest request)
			throws JsonProcessingException {
		return super.openapiYaml(request, "yaml");
	}

	protected void calculateServerUrl(HttpServletRequest request, String apiDocsUrl) {
		ActuatorProvider actuatorProvider = optionalActuatorProvider.get();
		String path ="";
		int port;
		super.initOpenAPIBuilder();
		if (ACTUATOR_DEFAULT_GROUP.equals(this.groupName)) {
			port = actuatorProvider.getActuatorPort();
			path = actuatorProvider.getActuatorPath();
		}
		else{
			port = actuatorProvider.getApplicationPort();
			path = actuatorProvider.getServletContextPath();
			String mvcServletPath = this.openAPIService.getContext().getBean(Environment.class).getProperty("spring.mvc.servlet.path");
			if(StringUtils.isNotEmpty(mvcServletPath))
				path = path + mvcServletPath;
		}

		String calculatedUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + path;
		openAPIService.setServerBaseUrl(calculatedUrl);
	}

}
