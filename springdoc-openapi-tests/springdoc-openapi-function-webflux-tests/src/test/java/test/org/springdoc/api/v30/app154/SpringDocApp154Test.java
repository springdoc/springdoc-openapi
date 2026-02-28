/*
 *
 *  *
 *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app154;

import java.util.function.Function;
import java.util.function.Supplier;

import reactor.core.publisher.Flux;
import test.org.springdoc.api.v30.AbstractSpringDocFunctionTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.cloud.function.web.mvc.ReactorAutoConfiguration;
import org.springframework.cloud.function.web.source.FunctionExporterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Import({ ReactorAutoConfiguration.class, FunctionExporterAutoConfiguration.class, ContextFunctionCatalogAutoConfiguration.class })
@TestPropertySource(properties = "spring.cloud.function.web.path=/toto")
public class SpringDocApp154Test extends AbstractSpringDocFunctionTest {

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v30.app154" })
	static class SpringDocTestApp {
		@Bean
		public Function<String, String> reverseString() {
			return value -> new StringBuilder(value).reverse().toString();
		}

		@Bean
		public Function<String, String> uppercase() {
			return String::toUpperCase;
		}

		@Bean
		public Function<Flux<String>, Flux<String>> lowercase() {
			return flux -> flux.map(String::toLowerCase);
		}

		@Bean(name = "titi")
		public Supplier<PersonDTO> hello() {
			return PersonDTO::new;
		}

		@Bean
		public Supplier<Flux<String>> words() {
			return () -> Flux.fromArray(new String[] { "foo", "bar" });
		}
	}

}
