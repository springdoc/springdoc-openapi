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

package org.springdoc.core.configuration;

import com.github.therapi.runtimejavadoc.CommentFormatter;
import org.springdoc.core.customizers.JavadocPropertyCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.SpringDocJavadocProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * The type Spring doc security configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.enable-javadoc:true}")
@ConditionalOnClass(CommentFormatter.class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocJavadocConfiguration {

	/**
	 * Spring doc javadoc provider spring doc javadoc provider.
	 *
	 * @return the spring doc javadoc provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SpringDocJavadocProvider springDocJavadocProvider() {
		return new SpringDocJavadocProvider();
	}

	/**
	 * Javadoc property customizer javadoc property customizer.
	 *
	 * @param javadocProvider      the javadoc provider
	 * @param objectMapperProvider the object mapper provider
	 * @return the javadoc property customizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	JavadocPropertyCustomizer javadocPropertyCustomizer(JavadocProvider javadocProvider, ObjectMapperProvider objectMapperProvider) {
		return new JavadocPropertyCustomizer(javadocProvider, objectMapperProvider);
	}
}