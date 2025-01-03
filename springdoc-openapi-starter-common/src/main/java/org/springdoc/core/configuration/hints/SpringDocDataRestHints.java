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

package org.springdoc.core.configuration.hints;

import java.util.Arrays;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springdoc.core.extractor.DelegatingMethodParameter;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.data.rest.core.mapping.TypedResourceDescription;

/**
 * The type Spring doc DataRest hints.
 *
 * @author bnasslahsen
 */
public class SpringDocDataRestHints implements RuntimeHintsRegistrar {

	/**
	 * The Spring data rest type names.
	 */
//spring-data-rest
	static String[] springDataRestTypeNames = { "org.springframework.data.rest.webmvc.config.DelegatingHandlerMapping",
			"org.springframework.data.rest.webmvc.support.DelegatingHandlerMapping",
			"org.springframework.data.rest.webmvc.RepositoryPropertyReferenceController",
			"org.springframework.data.rest.webmvc.RepositorySearchController",
			"org.springframework.data.rest.webmvc.RepositorySchemaController",
			"org.springframework.data.rest.webmvc.RepositoryEntityController",
			"org.springdoc.webflux.core.fn.SpringdocRouteBuilder",
			"org.springdoc.webflux.core.visitor.RouterFunctionVisitor"
	};

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		//springdoc
		hints.reflection().registerField(
				FieldUtils.getField(TypedResourceDescription.class, "type", true));
		hints.reflection().registerField(
				FieldUtils.getDeclaredField(DelegatingMethodParameter.class, "additionalParameterAnnotations", true));

		//spring-data-rest
		Arrays.stream(springDataRestTypeNames).forEach(springDataRestTypeName ->
				hints.reflection()
						.registerTypeIfPresent(classLoader, springDataRestTypeName,
								hint -> hint.withMembers(MemberCategory.DECLARED_FIELDS,
										MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
										MemberCategory.INVOKE_DECLARED_METHODS
								))
		);
	}

}

