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

package org.springdoc.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.CacheProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.utils.PropertyResolverUtils;

import org.springframework.core.MethodParameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Verifies that parameter constraints do not mutate a schema cached by swagger-core.
 *
 * @author Dmytro Shubchynskyi
 */
class RequestSchemaCloningTest {

	@ParameterizedTest
	@EnumSource(OpenApiVersion.class)
	void constraintsDoNotLeakAfterEarlySchemaResolution(OpenApiVersion version) throws Exception {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.getApiDocs().setVersion(version);
		ObjectMapper sharedMapper = ObjectMapperFactory.createJson31();
		sharedMapper.setSerializerProvider(new CustomSerializerProvider());
		// Warm a real deserializer before the provider installs SchemaTypeMixin.
		sharedMapper.convertValue(sharedMapper.createObjectNode(), JsonSchema.class);
		try (MockedStatic<Json31> json31 = mockStatic(Json31.class, CALLS_REAL_METHODS);
				MockedStatic<Json> json = mockStatic(Json.class, CALLS_REAL_METHODS)) {
			json31.when(Json31::mapper).thenReturn(sharedMapper);
			json.when(Json::mapper).thenReturn(ObjectMapperFactory.createJson());
			ObjectMapperProvider provider = new ObjectMapperProvider(properties);
			Schema<?> source = properties.isOpenapi31() ? new JsonSchema() : new IntegerSchema();
			if (properties.isOpenapi31())
				source.setTypes(Set.of("integer"));
			assertFalse(provider.jsonMapper().isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
			Parameter constrained = new Parameter().schema(source);
			Parameter unconstrained = new Parameter().schema(source);
			AbstractRequestService service = requestService(properties, provider);
			MethodParameter methodParameter = methodParameter();

			service.applyBeanValidatorAnnotations(methodParameter, constrained,
					List.of(methodParameter.getParameterAnnotations()), false,
					properties.isOpenapi31() ? "3.1.0" : "3.0.1");
			service.applyBeanValidatorAnnotations(methodParameter, unconstrained, List.of(), false,
					properties.isOpenapi31() ? "3.1.0" : "3.0.1");

			assertFalse(provider.jsonMapper().isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
			assertNotSame(source, constrained.getSchema());
			assertEquals(source.getClass(), constrained.getSchema().getClass());
			assertEquals(BigDecimal.ONE, constrained.getSchema().getMinimum());
			assertNull(source.getMinimum());
			assertNull(unconstrained.getSchema().getMinimum());
			if (properties.isOpenapi31())
				assertEquals(Set.of("integer"), constrained.getSchema().getTypes());
			else
				assertEquals("integer", constrained.getSchema().getType());
		}
	}

	@Test
	void cloningPreservesOverriddenMapperConfiguration() throws Exception {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		ObjectMapper customMapper = new CustomObjectMapper(ObjectMapperProvider.createJson(properties));
		customMapper.addMixIn(JsonSchema.class, IgnoreDescription.class);
		// The provider override and its mixin must still govern the clone.
		ObjectMapperProvider provider = mock(ObjectMapperProvider.class);
		when(provider.jsonMapper()).thenReturn(customMapper);
		when(provider.isOpenapi31()).thenReturn(true);
		JsonSchema source = new JsonSchema();
		source.setTypes(Set.of("integer"));
		source.setDescription("ignored by the custom mapper");
		Parameter parameter = new Parameter().schema(source);
		MethodParameter methodParameter = methodParameter();

		requestService(properties, provider).applyBeanValidatorAnnotations(methodParameter, parameter,
				List.of(methodParameter.getParameterAnnotations()), false, "3.1.0");

		assertNotSame(source, parameter.getSchema());
		assertEquals(BigDecimal.ONE, parameter.getSchema().getMinimum());
		assertEquals(Set.of("integer"), parameter.getSchema().getTypes());
		assertNull(parameter.getSchema().getDescription());
		assertEquals("ignored by the custom mapper", source.getDescription());
		assertNull(source.getMinimum());
	}

	private AbstractRequestService requestService(SpringDocConfigProperties properties, ObjectMapperProvider provider) {
		GenericParameterService parameters = mock(GenericParameterService.class);
		PropertyResolverUtils resolver = mock(PropertyResolverUtils.class);
		when(parameters.getObjectMapperProvider()).thenReturn(provider);
		when(parameters.getPropertyResolverUtils()).thenReturn(resolver);
		when(resolver.getSpringDocConfigProperties()).thenReturn(properties);
		return new AbstractRequestService(parameters, null, mock(SpringDocCustomizers.class), null, null) {
		};
	}

	private MethodParameter methodParameter() throws Exception {
		return new MethodParameter(getClass().getDeclaredMethod("constrained", Integer.class), 0);
	}

	private void constrained(@Min(1) Integer value) {
	}

	// Normal serialization needs createInstance/withCaches, but does not require copy().
	private static class CustomSerializerProvider extends DefaultSerializerProvider {

		CustomSerializerProvider() {
		}

		CustomSerializerProvider(CustomSerializerProvider source, SerializationConfig config,
				SerializerFactory factory) {
			super(source, config, factory);
		}

		CustomSerializerProvider(CustomSerializerProvider source, CacheProvider caches) {
			super(source, caches);
		}

		@Override
		public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory factory) {
			return new CustomSerializerProvider(this, config, factory);
		}

		@Override
		public DefaultSerializerProvider withCaches(CacheProvider caches) {
			return new CustomSerializerProvider(this, caches);
		}

	}

	// A provider may supply an ObjectMapper subclass that does not implement copy().
	private static class CustomObjectMapper extends ObjectMapper {

		CustomObjectMapper(ObjectMapper source) {
			super(source);
		}

	}

	@JsonIgnoreProperties("description")
	private abstract static class IgnoreDescription {

	}

}
