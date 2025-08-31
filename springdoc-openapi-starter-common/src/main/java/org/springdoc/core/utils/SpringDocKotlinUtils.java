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

package org.springdoc.core.utils;

import java.lang.reflect.Field;

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
	private static Boolean kotlinMarkedNullableFallback(Field f) {
		try {
			KClass<?> kClass = JvmClassMappingKt.getKotlinClass(f.getDeclaringClass());
			for (Object pObj : KClasses.getMemberProperties((KClass<Object>) kClass)) {
				KProperty1<?, ?> p = (KProperty1<?, ?>) pObj;
				if (p.getName().equals(f.getName())) {
					return p.getReturnType().isMarkedNullable();
				}
			}
		} catch (Throwable ignored) {}
		return null;
	}

	/**
	 * Kotlin constructor param is optional boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	static Boolean kotlinConstructorParamIsOptional(Field f) {
		try {
			KClass<?> kClass = JvmClassMappingKt.getKotlinClass(f.getDeclaringClass());
			KFunction<?> primary = KClasses.getPrimaryConstructor(kClass);
			if (primary != null) {
				for (KParameter p : primary.getParameters()) {
					if (f.getName().equals(p.getName())) {
						return p.isOptional();
					}
				}
			}
		} catch (Throwable ignored) {}
		return null;
	}

	/**
	 * Kotlin nullability boolean.
	 *
	 * @param field the field
	 * @return the boolean
	 */
	static Boolean kotlinNullability(Field field) {
		if (!isKotlinDeclaringClass(field)) return null;

		KProperty<?> prop = ReflectJvmMapping.getKotlinProperty(field);
		if (prop != null) {
			return prop.getReturnType().isMarkedNullable();
		}

		return kotlinMarkedNullableFallback(field);
	}
}
