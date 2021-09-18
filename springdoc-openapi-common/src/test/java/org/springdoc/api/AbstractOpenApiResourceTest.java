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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.fn.RouterOperation;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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

		resource = new AbstractOpenApiResource(
				GROUP_NAME,
				openAPIBuilderObjectFactory,
				requestBuilder,
				responseBuilder,
				operationParser,
				Optional.empty(),
				Optional.empty(),
				new SpringDocConfigProperties(),
				Optional.empty()
		) {

			@Override
			protected void getPaths(final Map<String, Object> findRestControllers, Locale locale) { }
		};
	}

	@Test
	void calculatePathFromRouterOperation() {
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
}