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

package org.springdoc.core.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public class ConverterUtils {
	
	private ConverterUtils() { }

	private static final List<Class<?>> RESULT_WRAPPERS_TO_IGNORE = new ArrayList<>();
	private static final List<Class<?>> RESPONSE_TYPES_TO_IGNORE = new ArrayList<>();

	static {
		RESULT_WRAPPERS_TO_IGNORE.add(Callable.class);
		RESULT_WRAPPERS_TO_IGNORE.add(ResponseEntity.class);
		RESULT_WRAPPERS_TO_IGNORE.add(HttpEntity.class);
		RESULT_WRAPPERS_TO_IGNORE.add(CompletionStage.class);
	}

	public static void addResponseWrapperToIgnore(Class<?> cls){
		RESULT_WRAPPERS_TO_IGNORE.add(cls);
	}

	public static void addResponseTypeToIgnore(Class<?> cls){
		RESPONSE_TYPES_TO_IGNORE.add(cls);
	}

	static boolean isResponseTypeWrapper(Class<?> rawClass) {
		return RESULT_WRAPPERS_TO_IGNORE.stream().anyMatch(clazz -> clazz.isAssignableFrom(rawClass));
	}

	public static boolean isResponseTypeToIgnore(Class<?> rawClass){
		return RESPONSE_TYPES_TO_IGNORE.stream().anyMatch(clazz -> clazz.isAssignableFrom(rawClass));
	}
}
