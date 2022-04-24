/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.parsers;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Optional;

import kotlin.coroutines.Continuation;

import org.springframework.core.MethodParameter;

/**
 * The type Kotlin coroutines return type parser.
 * @author bnasslahsen
 */
public class KotlinCoroutinesReturnTypeParser implements ReturnTypeParser {

	@Override
	public Type getReturnType(MethodParameter methodParameter) {
		Method method = methodParameter.getMethod();
		Type returnType = Object.class;
		assert method != null;
		Optional<Parameter> continuationParameter = Arrays.stream(method.getParameters())
				.filter(parameter -> parameter.getType().getCanonicalName().equals(Continuation.class.getCanonicalName()))
				.findFirst();
		if (continuationParameter.isPresent()) {
			Type continuationType = continuationParameter.get().getParameterizedType();
			if (continuationType instanceof ParameterizedType) {
				Type actualTypeArguments = ((ParameterizedType) continuationType).getActualTypeArguments()[0];
				if (actualTypeArguments instanceof WildcardType)
					returnType = ((WildcardType) actualTypeArguments).getLowerBounds()[0];
			}
		}
		return returnType;
	}
}
