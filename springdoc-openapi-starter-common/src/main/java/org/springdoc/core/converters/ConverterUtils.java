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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

/**
 * The type Converter utils.
 *
 * @author bnasslahsen
 */
public class ConverterUtils {

	/**
	 * The constant RESULT_WRAPPERS_TO_IGNORE.
	 */
	private static final List<Class<?>> RESULT_WRAPPERS_TO_IGNORE = new CopyOnWriteArrayList<>();

	/**
	 * The constant RESPONSE_TYPES_TO_IGNORE.
	 */
	private static final List<Class<?>> RESPONSE_TYPES_TO_IGNORE = new CopyOnWriteArrayList<>();

	/**
	 * The constant FLUX_WRAPPERS_TO_IGNORE.
	 */
	private static final List<Class<?>> FLUX_WRAPPERS_TO_IGNORE = new CopyOnWriteArrayList<>();

	/**
	 * The constant JAVA_TYPE_TO_IGNORE.
	 */
	private static final List<Class<?>> JAVA_TYPE_TO_IGNORE = new CopyOnWriteArrayList<>();

	static {
		RESULT_WRAPPERS_TO_IGNORE.add(Callable.class);
		RESULT_WRAPPERS_TO_IGNORE.add(ResponseEntity.class);
		RESULT_WRAPPERS_TO_IGNORE.add(HttpEntity.class);
		RESULT_WRAPPERS_TO_IGNORE.add(CompletionStage.class);
	}

	/**
	 * Instantiates a new Converter utils.
	 */
	private ConverterUtils() {
	}

	/**
	 * Add response wrapper to ignore.
	 *
	 * @param cls the cls
	 */
	public static void addResponseWrapperToIgnore(Class<?> cls) {
		RESULT_WRAPPERS_TO_IGNORE.add(cls);
	}

	/**
	 * Add response type to ignore.
	 *
	 * @param cls the cls
	 */
	public static void addResponseTypeToIgnore(Class<?> cls) {
		RESPONSE_TYPES_TO_IGNORE.add(cls);
	}

	/**
	 * Is response type wrapper boolean.
	 *
	 * @param rawClass the raw class
	 * @return the boolean
	 */
	public static boolean isResponseTypeWrapper(Class<?> rawClass) {
		for (Class<?> clazz : RESULT_WRAPPERS_TO_IGNORE) {
			if (clazz.isAssignableFrom(rawClass))
				return true;
		}
		return false;
	}

	/**
	 * Is response type to ignore boolean.
	 *
	 * @param rawClass the raw class
	 * @return the boolean
	 */
	public static boolean isResponseTypeToIgnore(Class<?> rawClass) {
		for (Class<?> clazz : RESPONSE_TYPES_TO_IGNORE) {
			if (clazz.isAssignableFrom(rawClass))
				return true;
		}
		return false;
	}

	/**
	 * Remove response wrapper to ignore.
	 *
	 * @param classes the classes
	 */
	public static void removeResponseWrapperToIgnore(Class<?> classes) {
		RESULT_WRAPPERS_TO_IGNORE.remove(classes);
	}

	/**
	 * Remove response type to ignore.
	 *
	 * @param classes the classes
	 */
	public static void removeResponseTypeToIgnore(Class<?> classes) {
		RESPONSE_TYPES_TO_IGNORE.remove(classes);
	}

	/**
	 * Is flux type wrapper boolean.
	 *
	 * @param rawClass the raw class
	 * @return the boolean
	 */
	public static boolean isFluxTypeWrapper(Class<?> rawClass) {
		for (Class<?> clazz : FLUX_WRAPPERS_TO_IGNORE) {
			if (clazz.isAssignableFrom(rawClass))
				return true;
		}
		return false;
	}

	/**
	 * Remove flux wrapper to ignore.
	 *
	 * @param classes the classes
	 */
	public static void removeFluxWrapperToIgnore(Class<?> classes) {
		FLUX_WRAPPERS_TO_IGNORE.remove(classes);
	}

	/**
	 * Add flux wrapper to ignore.
	 *
	 * @param cls the cls
	 */
	public static void addFluxWrapperToIgnore(Class<?> cls) {
		FLUX_WRAPPERS_TO_IGNORE.add(cls);
	}

	/**
	 * Add java type to ignore.
	 *
	 * @param cls the cls
	 */
	public static void addJavaTypeToIgnore(Class<?> cls) {
		JAVA_TYPE_TO_IGNORE.add(cls);
	}

	/**
	 * Remove java type to ignore.
	 *
	 * @param classes the classes
	 */
	public static void removeJavaTypeToIgnore(Class<?> classes) {
		JAVA_TYPE_TO_IGNORE.remove(classes);
	}

	/**
	 * Is java type to ignore boolean.
	 *
	 * @param rawClass the raw class
	 * @return the boolean
	 */
	public static boolean isJavaTypeToIgnore(Class<?> rawClass) {
		for (Class<?> clazz : JAVA_TYPE_TO_IGNORE) {
			if (clazz.isAssignableFrom(rawClass))
				return true;
		}
		return false;
	}

}
