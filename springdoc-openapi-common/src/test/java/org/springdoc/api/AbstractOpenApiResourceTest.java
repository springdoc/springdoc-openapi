/*
 *
 *  *
 *  *  * Copyright 2019-2021 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *	  https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.api;

import java.util.*;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.*;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.fn.RouterOperation;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ExtendWith(MockitoExtension.class)
class AbstractOpenApiResourceTest {

	private static final String GROUP_NAME = "groupName";
	private static final String PATH = "/some/path";
	public static final String PARAMETER_REFERENCE = "#/components/parameters/MyParameter";
	public static final String PARAMETER_WITH_NUMBER_SCHEMA_NAME = "parameterWithNumberSchema";
	public static final String PARAMETER_WITHOUT_SCHEMA_NAME = "parameterWithoutSchema";

	@Mock
	private ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory;

	@Mock
	private OpenAPIService openAPIService;

	@Mock
	private AbstractRequestService requestBuilder;

	@Mock
	private GenericResponseService responseBuilder;

	@Mock
	private OperationService operationParser;

	@Mock
	private ApplicationContext context;

	private OpenAPI openAPI;

	private AbstractOpenApiResource resource;

	@BeforeEach
	public void setUp() {
		openAPI = new OpenAPI();
		openAPI.setPaths(new Paths().addPathItem(PATH, new PathItem()));

		when(openAPIService.getCalculatedOpenAPI()).thenReturn(openAPI);
		when(openAPIService.getContext()).thenReturn(context);

		when(openAPIBuilderObjectFactory.getObject()).thenReturn(openAPIService);
	}

	@Test
	void calculatePathFromRouterOperation() {
		resource = new EmptyPathsOpenApiResource(
				GROUP_NAME,
				openAPIBuilderObjectFactory,
				requestBuilder,
				responseBuilder,
				operationParser,
				Optional.empty(),
				Optional.empty(),
				new SpringDocConfigProperties(),
				Optional.empty()
		);

		final Parameter refParameter = new Parameter().$ref(PARAMETER_REFERENCE);

		final Parameter numberParameterInPath = new Parameter()
				.name(PARAMETER_WITH_NUMBER_SCHEMA_NAME)
				.in(ParameterIn.PATH.toString())
				.schema(new NumberSchema());

		final Parameter parameterWithoutSchema = new Parameter()
				.name(PARAMETER_WITHOUT_SCHEMA_NAME);

		final Operation operation = new Operation();
		operation.setParameters(asList(
				refParameter,
				numberParameterInPath,
				parameterWithoutSchema
		));

		final RouterOperation routerOperation = new RouterOperation();
		routerOperation.setMethods(new RequestMethod[]{ GET });
		routerOperation.setOperationModel(operation);
		routerOperation.setPath(PATH);

		resource.calculatePath(routerOperation, Locale.getDefault());

		final List<Parameter> parameters = resource.getOpenApi(Locale.getDefault()).getPaths().get(PATH).getGet().getParameters();
		assertThat(parameters.size(), is(3));
		assertThat(parameters, containsInAnyOrder(refParameter, numberParameterInPath, parameterWithoutSchema));

		assertThat(refParameter.getName(), nullValue());
		assertThat(refParameter.get$ref(), is(PARAMETER_REFERENCE));
		assertThat(refParameter.getSchema(), nullValue());
		assertThat(refParameter.getIn(), nullValue());

		assertThat(numberParameterInPath.getName(), is(PARAMETER_WITH_NUMBER_SCHEMA_NAME));
		assertThat(numberParameterInPath.get$ref(), nullValue());
		assertThat(numberParameterInPath.getSchema(), is(new NumberSchema()));
		assertThat(numberParameterInPath.getIn(), is(ParameterIn.PATH.toString()));

		assertThat(parameterWithoutSchema.getName(), is(PARAMETER_WITHOUT_SCHEMA_NAME));
		assertThat(parameterWithoutSchema.get$ref(), nullValue());
		assertThat(parameterWithoutSchema.getSchema(), is(new StringSchema()));
		assertThat(parameterWithoutSchema.getIn(), is(ParameterIn.QUERY.toString()));
	}

	@Test
	void preLoadingModeShouldNotOverwriteServers() throws InterruptedException {
		when(openAPIService.updateServers(any())).thenCallRealMethod();
		when(openAPIService.getCachedOpenAPI()).thenCallRealMethod();
		doAnswer(new CallsRealMethods()).when(openAPIService).setServersPresent(true);
		doAnswer(new CallsRealMethods()).when(openAPIService).setServerBaseUrl(any());
		doAnswer(new CallsRealMethods()).when(openAPIService).setCachedOpenAPI(any());

		String customUrl = "https://custom.com";
		String generatedUrl = "https://generated.com";
		OpenApiCustomiser openApiCustomiser = openApi -> openApi.setServers(singletonList(new Server().url(customUrl)));
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.setPreLoadingEnabled(true);

		resource = new EmptyPathsOpenApiResource(
				GROUP_NAME,
				openAPIBuilderObjectFactory,
				requestBuilder,
				responseBuilder,
				operationParser,
				Optional.empty(),
				Optional.of(singletonList(openApiCustomiser)),
				properties,
				Optional.empty()
		);

		// wait for executor to be done
		Thread.sleep(1_000);

		// emulate generating base url
		openAPIService.setServerBaseUrl(generatedUrl);
		openAPIService.updateServers(openAPI);

		OpenAPI after = resource.getOpenApi(Locale.getDefault());

		assertThat(after.getServers().get(0).getUrl(), is(customUrl));
	}


	private static class EmptyPathsOpenApiResource extends AbstractOpenApiResource {

		EmptyPathsOpenApiResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {
			super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
		}

		@Override
		public void getPaths(Map<String, Object> findRestControllers, Locale locale) {
		}
	}
}