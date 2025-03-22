/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.core.configuration.hints;

import java.util.Arrays;

import com.fasterxml.jackson.databind.BeanDescription;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.jackson.ApiResponsesSerializer;
import io.swagger.v3.core.jackson.PathsSerializer;
import io.swagger.v3.core.jackson.mixin.Components31Mixin;
import io.swagger.v3.core.jackson.mixin.ComponentsMixin;
import io.swagger.v3.core.jackson.mixin.DateSchemaMixin;
import io.swagger.v3.core.jackson.mixin.Discriminator31Mixin;
import io.swagger.v3.core.jackson.mixin.ExampleMixin;
import io.swagger.v3.core.jackson.mixin.ExtensionsMixin;
import io.swagger.v3.core.jackson.mixin.Info31Mixin;
import io.swagger.v3.core.jackson.mixin.MediaTypeMixin;
import io.swagger.v3.core.jackson.mixin.OpenAPI31Mixin;
import io.swagger.v3.core.jackson.mixin.OpenAPIMixin;
import io.swagger.v3.core.jackson.mixin.OperationMixin;
import io.swagger.v3.core.jackson.mixin.Schema31Mixin;
import io.swagger.v3.core.jackson.mixin.SchemaMixin;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.servers.ServerVariables;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springdoc.core.properties.SpringDocConfigProperties.ModelConverters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * The type Spring doc hints.
 *
 * @author bnasslahsen
 */
public class SpringDocHints implements RuntimeHintsRegistrar {

	/**
	 * The Types to register.
	 */
	static Class[] typesToRegister = {
			//swagger-models
			io.swagger.v3.oas.models.security.SecurityScheme.Type.class,
			io.swagger.v3.oas.models.security.SecurityScheme.In.class,
			io.swagger.v3.oas.models.media.Encoding.class,
			io.swagger.v3.oas.models.info.Contact.class,
			io.swagger.v3.oas.models.info.License.class,
			io.swagger.v3.oas.models.security.OAuthFlow.class, io.swagger.v3.oas.models.security.OAuthFlows.class,
			io.swagger.v3.oas.models.security.SecurityScheme.class,
			io.swagger.v3.oas.models.tags.Tag.class,
			io.swagger.v3.oas.models.servers.ServerVariable.class,
			io.swagger.v3.oas.models.servers.Server.class,
			io.swagger.v3.oas.models.security.SecurityRequirement.class,
			io.swagger.v3.oas.models.info.Info.class,
			io.swagger.v3.oas.models.parameters.RequestBody.class,
			io.swagger.v3.oas.models.media.Schema.class,
			io.swagger.v3.oas.models.media.Content.class,
			io.swagger.v3.oas.models.media.ArraySchema.class,
			io.swagger.v3.oas.models.media.JsonSchema.class,
			io.swagger.v3.oas.models.responses.ApiResponse.class,
			io.swagger.v3.oas.models.responses.ApiResponses.class,
			io.swagger.v3.oas.models.ExternalDocumentation.class,
			io.swagger.v3.oas.models.links.LinkParameter.class,
			io.swagger.v3.oas.models.links.Link.class,
			io.swagger.v3.oas.models.parameters.Parameter.class,
			io.swagger.v3.oas.models.Operation.class,
			io.swagger.v3.oas.models.headers.Header.class,
			ModelConverter.class,
			io.swagger.v3.core.converter.ModelConverterContextImpl.class,
			ModelConverters.class,
			SpecFilter.class,
			MediaType.class,
			ApiResponsesSerializer.class,
			PathsSerializer.class,
			ComponentsMixin.class,
			ExtensionsMixin.class,
			OpenAPIMixin.class,
			OperationMixin.class,
			SchemaMixin.class,
			Paths.class,
			XML.class,
			UUIDSchema.class,
			PathItem.class,
			ServerVariables.class,
			OpenAPI.class,
			Components.class,
			StringSchema.class,
			DateTimeSchema.class,
			Discriminator.class,
			BooleanSchema.class,
			FileSchema.class,
			IntegerSchema.class,
			MapSchema.class,
			ObjectSchema.class,
			Scopes.class,
			DateSchema.class,
			ComposedSchema.class,
			BinarySchema.class,
			ByteArraySchema.class,
			EmailSchema.class,
			Example.class,
			EncodingProperty.class,
			NumberSchema.class,
			PasswordSchema.class,
			CookieParameter.class,
			HeaderParameter.class,
			PathParameter.class,
			QueryParameter.class,
			DateSchemaMixin.class,
			ExampleMixin.class,
			MediaTypeMixin.class,
			//oas 3.1
			Schema31Mixin.class,
			Schema31Mixin.TypeSerializer.class,
			Components31Mixin.class,
			OpenAPI31Mixin.class,
			Discriminator31Mixin.class,
			Info31Mixin.class,
			Schema31Mixin.TypeSerializer.class,
			JsonSchema.class,
			//springdoc classes
			org.springdoc.core.annotations.ParameterObject.class,
			org.springdoc.core.converters.models.Pageable.class,
			org.springdoc.core.extractor.DelegatingMethodParameter.class,
			// spring
			org.springframework.core.MethodParameter.class,
			// jackson
			BeanDescription.class,
	};

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.proxies()
				.registerJdkProxy(org.springframework.web.context.request.NativeWebRequest.class);
		hints.reflection()
				.registerType(java.lang.Module.class,
						hint -> hint.withMembers(MemberCategory.DECLARED_FIELDS,
								MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
								MemberCategory.INVOKE_DECLARED_METHODS))
				.registerType(java.lang.ModuleLayer.class, MemberCategory.INVOKE_DECLARED_METHODS)
				.registerType(java.lang.module.Configuration.class, MemberCategory.INVOKE_DECLARED_METHODS)
				.registerType(java.lang.module.ResolvedModule.class, MemberCategory.INVOKE_DECLARED_METHODS)
				.registerType(java.lang.invoke.MethodHandles.class, MemberCategory.DECLARED_CLASSES)
				.registerType(java.lang.invoke.MethodHandles.Lookup.class);
		//swagger-models
		Arrays.stream(typesToRegister).forEach(aClass ->
				hints.reflection().registerType(aClass,
						hint -> hint.withMembers(
								MemberCategory.DECLARED_FIELDS,
								MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
								MemberCategory.INVOKE_DECLARED_METHODS
						)));

		//springdoc
		hints.reflection().registerField(FieldUtils.getDeclaredField(io.swagger.v3.core.converter.ModelConverters.class, "converters", true));
		hints.reflection().registerType(org.springdoc.core.utils.Constants.class, hint -> hint.withMembers(MemberCategory.DECLARED_FIELDS));
		hints.resources().registerPattern(SwaggerUiConfigProperties.SPRINGDOC_CONFIG_PROPERTIES)
				.registerResourceBundle("sun.util.resources.LocaleNames");
	}

}

