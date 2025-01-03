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

package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * The type Schema property deprecating converter.
 *
 * @author bnasslahsen
 */
public class SchemaPropertyDeprecatingConverter implements ModelConverter {

	/**
	 * The constant DEPRECATED_ANNOTATIONS.
	 */
	private static final List<Class<? extends Annotation>> DEPRECATED_ANNOTATIONS = Collections.synchronizedList(new ArrayList<>());

	static {
		DEPRECATED_ANNOTATIONS.add(Deprecated.class);
	}

	/**
	 * Contains deprecated annotation boolean.
	 *
	 * @param annotations the annotations
	 * @return the boolean
	 */
	public static boolean containsDeprecatedAnnotation(Annotation[] annotations) {
		return annotations != null && Stream.of(annotations).map(Annotation::annotationType).anyMatch(DEPRECATED_ANNOTATIONS::contains);
	}

	/**
	 * Add deprecated type.
	 *
	 * @param cls the cls
	 */
	public static void addDeprecatedType(Class<? extends Annotation> cls) {
		DEPRECATED_ANNOTATIONS.add(cls);
	}

	/**
	 * Is deprecated boolean.
	 *
	 * @param method the annotated element
	 * @return the boolean
	 */
	public static boolean isDeprecated(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();
		boolean deprecatedMethod = DEPRECATED_ANNOTATIONS.stream().anyMatch(annoClass -> AnnotatedElementUtils.findMergedAnnotation(method, annoClass) != null);
		boolean deprecatedClass = DEPRECATED_ANNOTATIONS.stream().anyMatch(annoClass -> AnnotatedElementUtils.findMergedAnnotation(declaringClass, annoClass) != null);
		return deprecatedClass || deprecatedMethod;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext()) {
			Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
			if (type.isSchemaProperty() && containsDeprecatedAnnotation(type.getCtxAnnotations()))
				resolvedSchema.setDeprecated(true);
			return resolvedSchema;
		}
		return null;
	}
}
