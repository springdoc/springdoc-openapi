package org.springdoc.core.configuration;

import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * The type Spring doc kotlin module configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KotlinModule.class)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.enable-kotlin:true}")
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
class SpringDocJacksonKotlinModuleConfiguration {

	/**
	 * Instantiates a new Spring doc kotlin module configuration.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	public SpringDocJacksonKotlinModuleConfiguration(ObjectMapperProvider objectMapperProvider) {
		objectMapperProvider.jsonMapper()
				.registerModule(new KotlinModule.Builder().build())
				.registerModule(new SpringDocRequiredModule());

	}
}
