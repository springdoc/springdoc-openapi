/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import groovy.lang.MetaClass;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springdoc.core.converters.PropertyCustomizingConverter;
import org.springdoc.core.converters.RequestTypeToIgnoreConverter;
import org.springdoc.core.converters.ResponseSupportConverter;
import org.springdoc.core.customizers.OpenApiBuilderCustomiser;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.PropertyCustomizer;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import static org.springdoc.core.Constants.SPRINGDOC_CACHE_DISABLED;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES;
import static org.springdoc.core.SpringDocUtils.getConfig;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocConfiguration {

	static {
		getConfig().replaceWithSchema(ObjectNode.class, new ObjectSchema());
	}

	@Bean
	LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
		return new LocalVariableTableParameterNameDiscoverer();
	}

	@Bean
	AdditionalModelsConverter pageableSupportConverter() {
		return new AdditionalModelsConverter();
	}

	@Bean
	PropertyCustomizingConverter propertyCustomizingConverter(Optional<List<PropertyCustomizer>> customizers) {
		return new PropertyCustomizingConverter(customizers);
	}

	@Bean
	ResponseSupportConverter responseSupportConverter() {
		return new ResponseSupportConverter();
	}

	@ConditionalOnClass(value = { MetaClass.class })
	class GroovyProvider {
		@Bean
		Object ignoreGroovyMetaClass () {
			SpringDocUtils.getConfig().addRequestWrapperToIgnore(MetaClass.class);
			return null;
		}
		@Bean
		RequestTypeToIgnoreConverter requestTypeToIgnoreConverter() {
			return new RequestTypeToIgnoreConverter();
		}
	}

	@Bean
	OpenAPIBuilder openAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context,
			SecurityParser securityParser,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OpenApiBuilderCustomiser>> openApiBuilderCustomisers) {
		return new OpenAPIBuilder(openAPI, context, securityParser, springDocConfigProperties, openApiBuilderCustomisers);
	}

	@Bean
	ModelConverterRegistrar modelConverterRegistrar(Optional<List<ModelConverter>> modelConverters) {
		return new ModelConverterRegistrar(modelConverters.orElse(Collections.emptyList()));
	}

	@Bean
	@ConditionalOnWebApplication
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
	RequestBodyBuilder requestBodyBuilder(GenericParameterBuilder parameterBuilder) {
		return new RequestBodyBuilder(parameterBuilder);
	}

	@Bean
	SecurityParser securityParser(PropertyResolverUtils propertyResolverUtils) {
		return new SecurityParser(propertyResolverUtils);
	}

	@Bean
	ReturnTypeParser genericReturnTypeParser() {
		return new ReturnTypeParser() {};
	}

	@Bean
	GenericParameterBuilder parameterBuilder(PropertyResolverUtils propertyResolverUtils) {
		return new GenericParameterBuilder(propertyResolverUtils);
	}

	@Bean
	@ConditionalOnProperty(SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES)
	OpenApiCustomiser propertiesResolverForSchema(PropertyResolverUtils propertyResolverUtils, OpenAPIBuilder openAPIBuilder) {
		return openApi -> {
			Components components = openApi.getComponents();
			Map<String, Schema> schemas = components.getSchemas();
			schemas.values().forEach(schema -> openAPIBuilder.resolveProperties(schema, propertyResolverUtils));
		};
	}

	static class ConditionOnCacheOrGroupedOpenApi extends AnyNestedCondition {

		ConditionOnCacheOrGroupedOpenApi() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@Bean
		@ConditionalOnBean(GroupedOpenApi.class)
		BeanFactoryPostProcessor beanFactoryPostProcessor1() {
			return getBeanFactoryPostProcessor();
		}

		@Bean
		@ConditionalOnProperty(name = SPRINGDOC_CACHE_DISABLED)
		@ConditionalOnMissingBean(GroupedOpenApi.class)
		BeanFactoryPostProcessor beanFactoryPostProcessor2() {
			return getBeanFactoryPostProcessor();
		}

		private BeanFactoryPostProcessor getBeanFactoryPostProcessor() {
			return beanFactory -> {
				for (String beanName : beanFactory.getBeanNamesForType(OpenAPIBuilder.class)) {
					beanFactory.getBeanDefinition(beanName).setScope("prototype");
				}
				for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class)) {
					beanFactory.getBeanDefinition(beanName).setScope("prototype");
				}
			};
		}
	}
}
