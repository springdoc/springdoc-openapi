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

package org.springdoc.openapi.javadoc;

import org.springdoc.core.JavadocProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

/**
 * The type Spring doc security configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocJavadocConfiguration {

	/**
	 * Spring doc javadoc provider spring doc javadoc provider.
	 *
	 * @return the spring doc javadoc provider
	 */
	@Bean
	@ConditionalOnMissingBean
	SpringDocJavadocProvider springDocJavadocProvider( ) {
		return new SpringDocJavadocProvider();
	}

	/**
	 * Javadoc property customizer javadoc property customizer.
	 *
	 * @param javadocProvider the javadoc provider
	 * @return the javadoc property customizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	JavadocPropertyCustomizer javadocPropertyCustomizer( JavadocProvider javadocProvider){
		return new JavadocPropertyCustomizer(javadocProvider);
	}
}
