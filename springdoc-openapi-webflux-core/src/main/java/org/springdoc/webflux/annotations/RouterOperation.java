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

import org.springframework.web.bind.annotation.RequestMapping;
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
	 * Alias for {@link RequestMapping#path}.
	 */
	String path();

	/**
	 * Alias for {@link RequestMapping#method}.
	 */
	RequestMethod[] method();

	/**
	 * Alias for {@link RequestMapping#consumes}.
	 */
	String[] consumes() default {};

	/**
	 * Alias for {@link RequestMapping#produces}.
	 */
	String[] produces() default {};

	/**
	 * The class of the Handler bean.
	 *
	 * @return the class of the Bean
	 **/
	Class<?> beanClass() default Void.class;

	/**
	 * The method of the handler Bean.
	 *
	 * @return The method of the handler Bean.
	 **/
	String beanMethod() default "";

	/**
	 * The parameters of the handler method.
	 *
	 * @return The parameters of the handler method.
	 **/
	Class<?>[] parameterTypes() default {};

}