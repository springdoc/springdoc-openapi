/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
package test.org.springdoc.api.v31.app120;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.core.annotation.AliasFor;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The interface Account id.
 */
@Target({ PARAMETER, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Parameter(description = "non alias description")
public @interface AccountId {

	/**
	 * Name string.
	 *
	 * @return the string
	 */
	@AliasFor(annotation = Parameter.class, value = "name")
	String name() default "";

	/**
	 * Example string.
	 *
	 * @return the string
	 */
	@AliasFor(annotation = Parameter.class, value = "example")
	String example() default "123456";

	/**
	 * In parameter in.
	 *
	 * @return the parameter in
	 */
	@AliasFor(annotation = Parameter.class, value = "in")
	ParameterIn in() default ParameterIn.DEFAULT;

	/**
	 * Schema schema.
	 *
	 * @return the schema
	 */
	@AliasFor(annotation = Parameter.class, value = "schema")
	Schema schema() default @Schema();
}
