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
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.api.ErrorMessage;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.converters.FileSupportConverter;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springdoc.core.converters.PolymorphicModelConverter;
import org.springdoc.core.converters.PropertyCustomizingConverter;
import org.springdoc.core.converters.ResponseSupportConverter;
import org.springdoc.core.converters.SchemaPropertyDeprecatingConverter;
import org.springdoc.core.customizers.ActuatorOpenApiCustomizer;
import org.springdoc.core.customizers.ActuatorOperationCustomizer;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springdoc.core.Constants.SPRINGDOC_DEPRECATING_CONVERTER_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;
import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Spring doc configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocConfiguration {

	/**
	 * The constant BINDRESULT_CLASS.
	 */
	private static final String BINDRESULT_CLASS = "org.springframework.boot.context.properties.bind.BindResult";

	static {
		getConfig().replaceWithSchema(ObjectNode.class, new ObjectSchema())
				.addResponseWrapperToIgnore(DeferredResult.class);
	}

	/**
	 * Local spring doc parameter name discoverer local variable table parameter name discoverer.
	 *
	 * @return the local variable table parameter name discoverer
	 */
	@Bean
	LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
		return new LocalVariableTableParameterNameDiscoverer();
	}

	/**
	 * Additional models converter additional models converter.
	 *
	 * @return the additional models converter
	 */
	@Bean
	@Lazy(false)
	AdditionalModelsConverter additionalModelsConverter() {
		return new AdditionalModelsConverter();
	}

	/**
	 * Property customizing converter property customizing converter.
	 *
	 * @param customizers the customizers
	 * @return the property customizing converter
	 */
	@Bean
	@Lazy(false)
	PropertyCustomizingConverter propertyCustomizingConverter(Optional<List<PropertyCustomizer>> customizers) {
		return new PropertyCustomizingConverter(customizers);
	}

	/**
	 * File support converter file support converter.
	 *
	 * @return the file support converter
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	FileSupportConverter fileSupportConverter() {
		return new FileSupportConverter();
	}

	/**
	 * Response support converter response support converter.
	 *
	 * @return the response support converter
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	ResponseSupportConverter responseSupportConverter() {
		return new ResponseSupportConverter();
	}

	/**
	 * Schema property deprecating converter schema property deprecating converter.
	 *
	 * @return the schema property deprecating converter
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_DEPRECATING_CONVERTER_ENABLED, matchIfMissing = true)
	@Lazy(false)
	SchemaPropertyDeprecatingConverter schemaPropertyDeprecatingConverter() {
		return new SchemaPropertyDeprecatingConverter();
	}

	/**
	 * Polymorphic model converter polymorphic model converter.
	 *
	 * @return the polymorphic model converter
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	PolymorphicModelConverter polymorphicModelConverter() {
		return new PolymorphicModelConverter();
	}

	/**
	 * Open api builder open api builder.
	 *
	 * @param openAPI the open api
	 * @param context the context
	 * @param securityParser the security parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param openApiBuilderCustomisers the open api builder customisers
	 * @return the open api builder
	 */
	@Bean
	@ConditionalOnMissingBean
	OpenAPIService openAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context,
			SecurityService securityParser,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers) {
		return new OpenAPIService(openAPI, context, securityParser, springDocConfigProperties, openApiBuilderCustomisers);
	}

	/**
	 * Model converter registrar model converter registrar.
	 *
	 * @param modelConverters the model converters
	 * @return the model converter registrar
	 */
	@Bean
	@Lazy(false)
	ModelConverterRegistrar modelConverterRegistrar(Optional<List<ModelConverter>> modelConverters) {
		return new ModelConverterRegistrar(modelConverters.orElse(Collections.emptyList()));
	}

	/**
	 * Operation builder operation builder.
	 *
	 * @param parameterBuilder the parameter builder
	 * @param requestBodyService the request body builder
	 * @param securityParser the security parser
	 * @param propertyResolverUtils the property resolver utils
	 * @return the operation builder
	 */
	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnMissingBean
	OperationService operationBuilder(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			SecurityService securityParser, PropertyResolverUtils propertyResolverUtils) {
		return new OperationService(parameterBuilder, requestBodyService,
				securityParser, propertyResolverUtils);
	}

	/**
	 * Property resolver utils property resolver utils.
	 *
	 * @param factory the factory
	 * @return the property resolver utils
	 */
	@Bean
	PropertyResolverUtils propertyResolverUtils(ConfigurableBeanFactory factory) {
		return new PropertyResolverUtils(factory);
	}

	/**
	 * Request body builder request body builder.
	 *
	 * @param parameterBuilder the parameter builder
	 * @return the request body builder
	 */
	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnMissingBean
	RequestBodyService requestBodyBuilder(GenericParameterService parameterBuilder) {
		return new RequestBodyService(parameterBuilder);
	}

	/**
	 * Security parser security parser.
	 *
	 * @param propertyResolverUtils the property resolver utils
	 * @return the security parser
	 */
	@Bean
	@ConditionalOnMissingBean
	SecurityService securityParser(PropertyResolverUtils propertyResolverUtils) {
		return new SecurityService(propertyResolverUtils);
	}

	/**
	 * Generic return type parser return type parser.
	 *
	 * @return the return type parser
	 */
	@Bean
	@Lazy(false)
	ReturnTypeParser genericReturnTypeParser() {
		return new ReturnTypeParser() {};
	}

	/**
	 * Parameter builder generic parameter builder.
	 *
	 * @param propertyResolverUtils the property resolver utils
	 * @param optionalDelegatingMethodParameterCustomizer the optional delegating method parameter customizer
	 * @param optionalWebConversionServiceProvider the optional web conversion service provider
	 * @return the generic parameter builder
	 */
	@Bean
	@ConditionalOnMissingBean
	GenericParameterService parameterBuilder(PropertyResolverUtils propertyResolverUtils,
			Optional<DelegatingMethodParameterCustomizer> optionalDelegatingMethodParameterCustomizer,
			Optional<WebConversionServiceProvider> optionalWebConversionServiceProvider) {
		return new GenericParameterService(propertyResolverUtils, optionalDelegatingMethodParameterCustomizer,
				optionalWebConversionServiceProvider);
	}

	/**
	 * Properties resolver for schema open api customiser.
	 *
	 * @param propertyResolverUtils the property resolver utils
	 * @param openAPIService the open api builder
	 * @return the open api customiser
	 */
	@Bean
	@ConditionalOnProperty(SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES)
	@Lazy(false)
	OpenApiCustomiser propertiesResolverForSchema(PropertyResolverUtils propertyResolverUtils, OpenAPIService openAPIService) {
		return openApi -> {
			Components components = openApi.getComponents();
			Map<String, Schema> schemas = components.getSchemas();
			schemas.values().forEach(schema -> openAPIService.resolveProperties(schema, propertyResolverUtils));
		};
	}

	/**
	 * Springdoc bean factory post processor bean factory post processor.
	 *
	 * @return the bean factory post processor
	 */
	@Bean
	@Conditional(CacheOrGroupedOpenApiCondition.class)
	@ConditionalOnClass(name = BINDRESULT_CLASS)
	@Lazy(false)
	static BeanFactoryPostProcessor springdocBeanFactoryPostProcessor() {
		return new SpringdocBeanFactoryConfigurer();
	}

	/**
	 * Springdoc bean factory post processor 2 bean factory post processor.
	 *
	 * @return the bean factory post processor
	 */
// For spring-boot-1 compatibility
	@Bean
	@Conditional(CacheOrGroupedOpenApiCondition.class)
	@ConditionalOnMissingClass(value = BINDRESULT_CLASS)
	@Lazy(false)
	static BeanFactoryPostProcessor springdocBeanFactoryPostProcessor2() {
		return SpringdocBeanFactoryConfigurer::initBeanFactoryPostProcessor;
	}

	/**
	 * The type Open api resource advice.
	 * @author bnasslashen
	 */
	@RestControllerAdvice
	@Hidden
	class OpenApiResourceAdvice {
		/**
		 * Handle no handler found response entity.
		 *
		 * @param e the e
		 * @return the response entity
		 */
		@ExceptionHandler(OpenApiResourceNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		public ResponseEntity<ErrorMessage> handleNoHandlerFound(OpenApiResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(e.getMessage()));
		}
	}

	/**
	 * The type Spring doc web mvc actuator configuration.
	 * @author bnasslashen
	 */
	@ConditionalOnClass(WebEndpointProperties.class)
	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	static class SpringDocActuatorConfiguration {

		/**
		 * Springdoc bean factory post processor 3 bean factory post processor.
		 *
		 * @param groupedOpenApis the grouped open apis
		 * @return the bean factory post processor
		 */
		@Bean
		@Lazy(false)
		@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
		@Conditional(MultipleOpenApiSupportCondition.class)
		static BeanFactoryPostProcessor springdocBeanFactoryPostProcessor3(List<GroupedOpenApi> groupedOpenApis) {
			return new SpringdocActuatorBeanFactoryConfigurer(groupedOpenApis);
		}

		/**
		 * Actuator customizer operation customizer.
		 *
		 * @return the operation customizer
		 */
		@Bean
		@Lazy(false)
		OperationCustomizer actuatorCustomizer() {
			return new ActuatorOperationCustomizer();
		}

		/**
		 * Actuator customizer OpenAPI customiser.
		 *
		 * @param webEndpointProperties the web endpoint properties
		 * @return the OpenAPI customiser
		 */
		@Bean
		@Lazy(false)
		OpenApiCustomiser actuatorOpenApiCustomiser(WebEndpointProperties webEndpointProperties) {
			return new ActuatorOpenApiCustomizer(webEndpointProperties);
		}

	}

	/**
	 * The type Web conversion service configuration.
	 * @author bnasslashen
	 */
	@ConditionalOnClass(WebConversionService.class)
	static class WebConversionServiceConfiguration {

		/**
		 * Web conversion service provider web conversion service provider.
		 *
		 * @param webConversionServiceOptional the web conversion service optional
		 * @return the web conversion service provider
		 */
		@Bean
		@Lazy(false)
		WebConversionServiceProvider webConversionServiceProvider(Optional<WebConversionService> webConversionServiceOptional) {
			return new WebConversionServiceProvider(webConversionServiceOptional);
		}
	}

}
