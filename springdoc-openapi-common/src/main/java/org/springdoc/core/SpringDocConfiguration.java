/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.converters.FileSupportConverter;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springdoc.core.converters.PolymorphicModelConverter;
import org.springdoc.core.converters.PropertyCustomizingConverter;
import org.springdoc.core.converters.ResponseSupportConverter;
import org.springdoc.core.converters.SchemaPropertyDeprecatingConverter;
import org.springdoc.core.customizers.OpenApiBuilderCustomiser;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.PropertyCustomizer;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES;
import static org.springdoc.core.SpringDocUtils.getConfig;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocConfiguration {

	private static final String BINDRESULT_CLASS = "org.springframework.boot.context.properties.bind.BindResult";

	static {
		getConfig().replaceWithSchema(ObjectNode.class, new ObjectSchema());
	}

	@Bean
	LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
		return new LocalVariableTableParameterNameDiscoverer();
	}

	@Bean
	@Lazy(false)
	AdditionalModelsConverter additionalModelsConverter() {
		return new AdditionalModelsConverter();
	}

	@Bean
	@Lazy(false)
	PropertyCustomizingConverter propertyCustomizingConverter(Optional<List<PropertyCustomizer>> customizers) {
		return new PropertyCustomizingConverter(customizers);
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	FileSupportConverter fileSupportConverter() {
		return new FileSupportConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	ResponseSupportConverter responseSupportConverter() {
		return new ResponseSupportConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SchemaPropertyDeprecatingConverter schemaPropertyDeprecatingConverter() {
		return new SchemaPropertyDeprecatingConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	PolymorphicModelConverter polymorphicModelConverter() {
		return new PolymorphicModelConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	OpenAPIBuilder openAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context,
			SecurityParser securityParser,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OpenApiBuilderCustomiser>> openApiBuilderCustomisers) {
		return new OpenAPIBuilder(openAPI, context, securityParser, springDocConfigProperties, openApiBuilderCustomisers);
	}

	@Bean
	@Lazy(false)
	ModelConverterRegistrar modelConverterRegistrar(Optional<List<ModelConverter>> modelConverters) {
		return new ModelConverterRegistrar(modelConverters.orElse(Collections.emptyList()));
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnMissingBean
	OperationBuilder operationBuilder(GenericParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			SecurityParser securityParser, PropertyResolverUtils propertyResolverUtils) {
		return new OperationBuilder(parameterBuilder, requestBodyBuilder,
				securityParser, propertyResolverUtils);
	}

	@Bean
	PropertyResolverUtils propertyResolverUtils(ConfigurableBeanFactory factory) {
		return new PropertyResolverUtils(factory);
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnMissingBean
	RequestBodyBuilder requestBodyBuilder(GenericParameterBuilder parameterBuilder) {
		return new RequestBodyBuilder(parameterBuilder);
	}

	@Bean
	@ConditionalOnMissingBean
	SecurityParser securityParser(PropertyResolverUtils propertyResolverUtils) {
		return new SecurityParser(propertyResolverUtils);
	}

	@Bean
	@Lazy(false)
	ReturnTypeParser genericReturnTypeParser() {
		return new ReturnTypeParser() {};
	}

	@Bean
	@ConditionalOnMissingBean
	GenericParameterBuilder parameterBuilder(PropertyResolverUtils propertyResolverUtils) {
		return new GenericParameterBuilder(propertyResolverUtils);
	}

	@Bean
	@ConditionalOnProperty(SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES)
	@Lazy(false)
	OpenApiCustomiser propertiesResolverForSchema(PropertyResolverUtils propertyResolverUtils, OpenAPIBuilder openAPIBuilder) {
		return openApi -> {
			Components components = openApi.getComponents();
			Map<String, Schema> schemas = components.getSchemas();
			schemas.values().forEach(schema -> openAPIBuilder.resolveProperties(schema, propertyResolverUtils));
		};
	}

	@Bean
	@Conditional(CacheOrGroupedOpenApiCondition.class)
	@ConditionalOnClass(name = BINDRESULT_CLASS)
	@Lazy(false)
	static BeanFactoryPostProcessor springdocBeanFactoryPostProcessor() {
		return new SpringdocBeanFactoryConfigurer();
	}

	// For spring-boot-1 compatibility
	@Bean
	@Conditional(CacheOrGroupedOpenApiCondition.class)
	@ConditionalOnMissingClass(value = BINDRESULT_CLASS)
	@Lazy(false)
	static BeanFactoryPostProcessor springdocBeanFactoryPostProcessor2() {
		return beanFactory -> SpringdocBeanFactoryConfigurer.initBeanFactoryPostProcessor(beanFactory);
	}

}
