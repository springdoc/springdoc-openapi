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

package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import kotlin.jvm.JvmInline;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KParameter;
import kotlin.reflect.jvm.ReflectJvmMapping;
import kotlin.reflect.jvm.internal.KClassImpl;

import org.springframework.core.MethodParameter;

/**
 * @author bnasslahsen
 */
// KotlinInlineParameterResolver.java
public final class KotlinInlineParameterResolver {

	private KotlinInlineParameterResolver() {}

	public static Class<?> resolveInlineType(MethodParameter methodParameter, Type type) {
		Method method = methodParameter.getMethod();
		if (method == null) return null;

		KFunction<?> kFunction = ReflectJvmMapping.getKotlinFunction(method);
		if (kFunction == null) return null;

		int paramIndex = methodParameter.getParameterIndex();

		KParameter kParam = kFunction.getParameters().stream()
				.filter(p -> p.getKind() == KParameter.Kind.VALUE)
				.skip(paramIndex)
				.findFirst()
				.orElse(null);

		if (kParam == null) return null;

		Object classifier = kParam.getType().getClassifier();
		if (!(classifier instanceof KClass<?> kClass)) return null;

		for (Annotation a : kClass.getAnnotations()) {
			if (a.annotationType() == JvmInline.class
					&& kClass instanceof KClassImpl<?> impl) {
				return impl.getJClass();
			}
		}

		return null;
	}
}
