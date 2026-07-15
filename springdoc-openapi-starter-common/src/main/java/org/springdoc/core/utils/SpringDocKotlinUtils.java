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

package org.springdoc.core.utils;

import java.lang.reflect.Field;
import java.util.Optional;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KParameter;
import kotlin.reflect.KProperty;
import kotlin.reflect.KProperty1;
import kotlin.reflect.full.KClasses;
import kotlin.reflect.jvm.ReflectJvmMapping;

import org.springframework.core.KotlinDetector;

/**
 * The type Spring doc kotlin utils.
 *
 * @author bnasslahsen
 */
public class SpringDocKotlinUtils {

	public SpringDocKotlinUtils() {
	}

	/**
	 * Is kotlin declaring class boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	static boolean isKotlinDeclaringClass(Field f) {
		return KotlinDetector.isKotlinPresent()
				&& KotlinDetector.isKotlinReflectPresent()
				&& KotlinDetector.isKotlinType(f.getDeclaringClass());
	}

	/**
	 * Kotlin marked nullable fallback boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	private static Optional<Boolean> kotlinMarkedNullableFallback(Field f) {
		try {
			KClass<?> kClass = JvmClassMappingKt.getKotlinClass(f.getDeclaringClass());
			for (KProperty1<?, ?> pObj : KClasses.getMemberProperties(kClass)) {
				if (pObj.getName().equals(f.getName())) {
					return Optional.of(pObj.getReturnType().isMarkedNullable());
				}
			}
		} catch (Exception ignored) {
			// best-effort only
		}
		return Optional.empty();
	}

	/**
	 * Kotlin constructor param is optional boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	static Optional<Boolean> kotlinConstructorParamIsOptional(Field f) {
		try {
			KClass<?> kClass = JvmClassMappingKt.getKotlinClass(f.getDeclaringClass());
			KFunction<?> primary = KClasses.getPrimaryConstructor(kClass);
			if (primary != null) {
				for (KParameter p : primary.getParameters()) {
					if (f.getName().equals(p.getName())) {
						return Optional.of(p.isOptional());
					}
				}
			}
		} catch (Exception ignored) {
			// best-effort only
		}
		return Optional.empty();
	}

	/**
	 * Kotlin nullability boolean.
	 *
	 * @param field the field
	 * @return the boolean
	 */
	static Optional<Boolean> kotlinNullability(Field field) {
		if (!isKotlinDeclaringClass(field)) return Optional.empty();

		KProperty<?> prop = ReflectJvmMapping.getKotlinProperty(field);
		if (prop != null) {
			return Optional.of(prop.getReturnType().isMarkedNullable());
		}

		return kotlinMarkedNullableFallback(field);
	}
}
