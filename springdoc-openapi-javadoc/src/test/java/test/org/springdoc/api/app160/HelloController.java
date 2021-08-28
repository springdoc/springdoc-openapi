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

package test.org.springdoc.api.app160;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
public class HelloController {

	/**
	 * Do something interesting error response.
	 *
	 * @return the error response
	 */
	@PostMapping("test")
	public ErrorResponse doSomethingInteresting() {
		return null;
	}

	/**
	 * The type Swagger message.
	 */
	@PropertySource("classpath:swagger-message-160.properties" )
	@Configuration
	public class SwaggerMessage{}

	/**
	 * The type Error response.
	 */
	@Schema(description = "${ErrorResponse}")
	public class ErrorResponse {

		/**
		 * The Error code.
		 */
		@Schema(description = "${ErrorCode}", required = true)
		@JsonProperty
		private Integer errorCode;

		/**
		 * The Error message.
		 */
		@Schema(description = "${ErrorMessage}")
		@JsonProperty
		private String errorMessage;
	}

	/**
	 * Bundle api grouped open api.
	 *
	 * @return the grouped open api
	 */
	@Bean
	public GroupedOpenApi bundleApi() {
		return GroupedOpenApi.builder()
				.group("test")
				.pathsToMatch("/**")
				.build();
	}

	/**
	 * Translator resource bundle message source.
	 *
	 * @return the resource bundle message source
	 */
	@Bean
	public ResourceBundleMessageSource translator() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("swagger-message-160");
		source.setUseCodeAsDefaultMessage(true);
		source.setDefaultEncoding("utf-8");
		return source;
	}

}
