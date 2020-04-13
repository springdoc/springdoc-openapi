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
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springdoc.core.converters.PropertyCustomizingConverter;
import org.springdoc.core.converters.ResponseSupportConverter;
import org.springdoc.core.customizers.OpenApiBuilderCustomiser;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.PropertyCustomizer;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_PREFIX;
import static org.springdoc.core.Constants.SPRINGDOC_SCHEMA_RESOLVE_PROPERTIES;
import static org.springdoc.core.SpringDocUtils.getConfig;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocConfiguration {

	static {
		getConfig().replaceWithSchema(ObjectNode.class, new ObjectSchema());
	}

	private final String BINDRESULT_CLASS = "org.springframework.boot.context.properties.bind.BindResult";

	@Bean
	LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
		return new LocalVariableTableParameterNameDiscoverer();
	}

	@Bean
	@Lazy(false)
	AdditionalModelsConverter pageableSupportConverter() {
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
	ResponseSupportConverter responseSupportConverter() {
		return new ResponseSupportConverter();
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
	@ConditionalOnClass(name= BINDRESULT_CLASS)
	@Lazy(false)
	BeanFactoryPostProcessor springdocBeanFactoryPostProcessor(Environment environment) {
		return beanFactory -> {
			final BindResult<SpringDocConfigProperties> result = Binder.get(environment)
					.bind(SPRINGDOC_PREFIX, SpringDocConfigProperties.class);
			if (result.isBound()) {
				SpringDocConfigProperties springDocGroupConfig = result.get();
				List<GroupedOpenApi> groupedOpenApis = springDocGroupConfig.getGroupConfigs().stream()
						.map(elt -> {
							GroupedOpenApi.Builder builder = GroupedOpenApi.builder();
							if (!CollectionUtils.isEmpty(elt.getPackagesToScan()))
								builder.packagesToScan(elt.getPackagesToScan().toArray(new String[0]));
							if (!CollectionUtils.isEmpty(elt.getPathsToMatch()))
								builder.pathsToMatch(elt.getPathsToMatch().toArray(new String[0]));
							return builder.setGroup(elt.getGroup()).build();
						})
						.collect(Collectors.toList());
				groupedOpenApis.forEach(elt -> beanFactory.registerSingleton(elt.getGroup(), elt));
			}
			initBeanFactoryPostProcessor(beanFactory);
		};
	}

	// For spring-boot-1 compatibility
	@Bean
	@Conditional(CacheOrGroupedOpenApiCondition.class)
	@ConditionalOnMissingClass(value = BINDRESULT_CLASS)
	@Lazy(false)
	BeanFactoryPostProcessor springdocBeanFactoryPostProcessor2(Environment environment) {
		return beanFactory -> initBeanFactoryPostProcessor(beanFactory);
	}

	private void initBeanFactoryPostProcessor(ConfigurableListableBeanFactory beanFactory) {
		for (String beanName : beanFactory.getBeanNamesForType(OpenAPIBuilder.class))
			beanFactory.getBeanDefinition(beanName).setScope(SCOPE_PROTOTYPE);
		for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class))
			beanFactory.getBeanDefinition(beanName).setScope(SCOPE_PROTOTYPE);
	}
}
