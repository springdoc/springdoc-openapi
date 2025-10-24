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

package test.org.springdoc.api.v31.app14;

import java.util.Optional;

import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springdoc.core.customizers.DataRestDelegatingMethodParameterCustomizer;
import org.springdoc.core.providers.RepositoryRestConfigurationProvider;
import org.springdoc.core.providers.DataWebPropertiesProvider;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.data.rest.autoconfigure.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = { "spring.data.web.pageable.default-page-size=25",
		"spring.data.web.pageable.page-parameter=pages",
		"spring.data.web.pageable.size-parameter=sizes",
		"spring.data.web.pageable.one-indexed-parameters=true",
		"spring.data.web.pageable.prefix=prefix_",
		"spring.data.web.sort.sort-parameter=sorts" })
@EnableAutoConfiguration(exclude = {
		RepositoryRestMvcAutoConfiguration.class, SpringDocDataRestConfiguration.class
})
public class SpringDocApp14Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {
		// We only need to test spring-web with Pageable, without the use of spring-data-rest-starter
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		DataRestDelegatingMethodParameterCustomizer dataRestDelegatingMethodParameterCustomizer(Optional<DataWebPropertiesProvider> optionalDataWebPropertiesProvider, Optional<RepositoryRestConfigurationProvider> optionalRepositoryRestConfiguration) {
			return new DataRestDelegatingMethodParameterCustomizer(optionalDataWebPropertiesProvider, optionalRepositoryRestConfiguration);
		}
	}

}