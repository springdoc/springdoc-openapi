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

package org.springdoc.webflux.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The annotation may be used to define an operation method as an OpenAPI Operation, and/or to define additional
 * properties for the Operation.
 * <p>The following fields can also alternatively be defined at method level (as repeatable annotations in case of arrays),
 *
 **/
@Target({ ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RouterOperation {
	/**
	 * The path mapping URIs (e.g. {@code "/profile"}).
	 * Path mapping URIs may contain placeholders (e.g. <code>"/${profile_path}"</code>).
	 */
	String path();

	/**
	 * The HTTP request methods to map to, narrowing the primary mapping:
	 * GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE.
	 */
	RequestMethod[] method();

	/**
	 * Narrows the primary mapping by media types that can be consumed by the
	 * mapped handler. Consists of one or more media types one of which must
	 * match to the request {@code Content-Type} header. Examples:
	 * <pre class="code">
	 * consumes = "text/plain"
	 * consumes = {"text/plain", "application/*"}
	 * consumes = MediaType.TEXT_PLAIN_VALUE
	 * </pre>
	 */
	String[] consumes() default {};

	/**
	 * Narrows the primary mapping by media types that can be produced by the
	 * mapped handler. Consists of one or more media types one of which must
	 * be chosen via content negotiation against the "acceptable" media types
	 * of the request. Typically those are extracted from the {@code "Accept"}
	 * header but may be derived from query parameters, or other. Examples:
	 * <pre class="code">
	 * produces = "text/plain"
	 * produces = {"text/plain", "application/*"}
	 * produces = MediaType.TEXT_PLAIN_VALUE
	 * produces = "text/plain;charset=UTF-8"
	 * </pre>
	 */
	String[] produces() default {};

	/**
	 * The class of the Handler bean.
	 * @return the class of the Bean
	 **/
	Class<?> beanClass() default Void.class;

	/**
	 * The method of the handler Bean.
	 * @return The method of the handler Bean.
	 **/
	String beanMethod() default "";

	/**
	 * The parameters of the handler method.
	 * @return The parameters of the handler method.
	 **/
	Class<?>[] parameterTypes() default {};

	/**
	 * The swagger operation description
	 * Alias for {@link Operation}.
	 * @return The operation
	 **/
	Operation operation() default @Operation();


}