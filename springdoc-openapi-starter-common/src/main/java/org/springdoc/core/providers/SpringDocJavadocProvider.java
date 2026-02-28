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

package org.springdoc.core.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.FieldJavadoc;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.ParamJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;
import com.github.therapi.runtimejavadoc.ThrowsJavadoc;
import org.apache.commons.lang3.StringUtils;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toMap;

/**
 * The type Spring doc javadoc provider.
 *
 * @author bnasslahsen
 */
public class SpringDocJavadocProvider implements JavadocProvider {

	/**
	 * The comment formatter.
	 */
	private final CommentFormatter formatter = new CommentFormatter();

	/**
	 * The Class javadoc cache.
	 */
	private final Map<Class<?>, ClassJavadoc> classJavadocCache = new HashMap<>();


	/**
	 * Gets class description.
	 *
	 * @param cl the class
	 * @return the class description
	 */
	@Override
	public String getClassJavadoc(Class<?> cl) {
		ClassJavadoc classJavadoc = getJavadoc(cl);
		return formatter.format(classJavadoc.getComment());
	}

	/**
	 * Gets param descripton of record class.
	 *
	 * @param cl the class
	 * @return map of field and param descriptions
	 */
	@Override
	public Map<String, String> getRecordClassParamJavadoc(Class<?> cl) {
		ClassJavadoc classJavadoc = getJavadoc(cl);
		return classJavadoc.getRecordComponents().stream()
				.collect(Collectors.toMap(ParamJavadoc::getName, recordClass -> formatter.format(recordClass.getComment())));
	}

	/**
	 * Gets method javadoc description.
	 *
	 * @param method the method
	 * @return the method javadoc description
	 */
	@Override
	public String getMethodJavadocDescription(Method method) {
		MethodJavadoc methodJavadoc = getJavadoc(method);
		return formatter.format(methodJavadoc.getComment());
	}

	/**
	 * Gets method javadoc return.
	 *
	 * @param method the method
	 * @return the method javadoc return
	 */
	@Override
	public String getMethodJavadocReturn(Method method) {
		MethodJavadoc methodJavadoc = getJavadoc(method);
		return formatter.format(methodJavadoc.getReturns());
	}

	/**
	 * Gets method throws declaration.
	 *
	 * @param method the method
	 * @return the method throws (name-description map)
	 */
	public Map<String, String> getMethodJavadocThrows(Method method) {
		return getJavadoc(method)
				.getThrows()
				.stream()
				.collect(toMap(ThrowsJavadoc::getName, javadoc -> formatter.format(javadoc.getComment())));
	}

	/**
	 * Gets param javadoc.
	 *
	 * @param method the method
	 * @param name   the name
	 * @return the param javadoc
	 */
	@Override
	public String getParamJavadoc(Method method, String name) {
		MethodJavadoc methodJavadoc = getJavadoc(method);
		List<ParamJavadoc> paramsDoc = methodJavadoc.getParams();
		return paramsDoc.stream().filter(paramJavadoc1 -> name.equals(paramJavadoc1.getName())).findAny()
				.map(paramJavadoc1 -> formatter.format(paramJavadoc1.getComment())).orElse(null);
	}

	/**
	 * Gets field javadoc.
	 *
	 * @param field the field
	 * @return the field javadoc
	 */
	@Override
	public String getFieldJavadoc(Field field) {
		FieldJavadoc fieldJavadoc = getJavadoc(field);
		return formatter.format(fieldJavadoc.getComment());
	}

	@Override
	public String getFirstSentence(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		int pOpenIndex = text.indexOf("<p>");
		int pCloseIndex = text.indexOf("</p>");
		int dotIndex = text.indexOf(".");
		if (pOpenIndex != -1) {
			if (pOpenIndex == 0 && pCloseIndex != -1) {
				if (dotIndex != -1) {
					return text.substring(3, min(pCloseIndex, dotIndex));
				}
				return text.substring(3, pCloseIndex);
			}
			if (dotIndex != -1) {
				return text.substring(0, min(pOpenIndex, dotIndex));
			}
			return text.substring(0, pOpenIndex);
		}
		if (dotIndex != -1
				&& text.length() != dotIndex + 1
				&& Character.isWhitespace(text.charAt(dotIndex + 1))) {
			return text.substring(0, dotIndex + 1);
		}
		return text;
	}

	private ClassJavadoc getJavadoc(Class<?> cl) {
		ClassJavadoc classJavadoc = classJavadocCache.get(cl);
		if (classJavadoc != null) {
			return classJavadoc;
		}
		classJavadoc = RuntimeJavadoc.getJavadoc(cl);
		classJavadocCache.put(cl, classJavadoc);
		return classJavadoc;
	}

	private MethodJavadoc getJavadoc(Method method) {
		ClassJavadoc classJavadoc = getJavadoc(method.getDeclaringClass());
		List<String> paramTypes = Arrays.stream(method.getParameterTypes())
				.map(Class::getCanonicalName)
				.toList();
		return classJavadoc.getMethods()
				.stream()
				.filter(it -> Objects.equals(method.getName(), it.getName()) && Objects.equals(paramTypes, it.getParamTypes()))
				.findFirst().orElseGet(() -> MethodJavadoc.createEmpty(method));
	}

	private FieldJavadoc getJavadoc(Field field) {
		ClassJavadoc classJavadoc = getJavadoc(field.getDeclaringClass());
		return classJavadoc.getFields()
				.stream()
				.filter(it -> Objects.equals(field.getName(), it.getName()))
				.findFirst().orElseGet(() -> FieldJavadoc.createEmpty(field.getName()));
	}

	@Override
	public void clearCache() {
		classJavadocCache.clear();
	}
}
