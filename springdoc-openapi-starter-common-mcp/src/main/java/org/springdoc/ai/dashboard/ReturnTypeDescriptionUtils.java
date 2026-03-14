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

package org.springdoc.ai.dashboard;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Utility methods for building human-readable return type descriptions from Java types.
 *
 * @author bnasslahsen
 */
final class ReturnTypeDescriptionUtils {

	private ReturnTypeDescriptionUtils() {
	}

	/**
	 * Appends a return type suffix to a description, handling trailing punctuation.
	 * @param description the original description
	 * @param returnTypeDescription the return type description (e.g. "Returns String")
	 * @return the combined description
	 */
	static String appendReturnType(String description, String returnTypeDescription) {
		if (description.endsWith(".") || description.endsWith("!") || description.endsWith("?")) {
			return description + " " + returnTypeDescription;
		}
		return description + ". " + returnTypeDescription;
	}

	/**
	 * Describes a Java type as a human-readable string.
	 * @param type the Java type
	 * @return the description string
	 */
	static String describeJavaType(Type type) {
		if (type instanceof ParameterizedType pt) {
			Class<?> rawType = (Class<?>) pt.getRawType();
			Type[] typeArgs = pt.getActualTypeArguments();
			if (Collection.class.isAssignableFrom(rawType) && typeArgs.length > 0) {
				return "an array of " + describeJavaType(typeArgs[0]);
			}
			if (Map.class.isAssignableFrom(rawType) && typeArgs.length >= 2) {
				return "a map of " + describeJavaType(typeArgs[0]) + " to " + describeJavaType(typeArgs[1]);
			}
			return rawType.getSimpleName() + "<" + describeJavaType(typeArgs[0]) + ">";
		}
		if (type instanceof Class<?> clazz) {
			if (clazz.isArray()) {
				return "an array of " + describeJavaType(clazz.getComponentType());
			}
			if (!clazz.isPrimitive() && !clazz.getName().startsWith("java.lang")) {
				String props = describeClassProperties(clazz);
				if (!props.isEmpty()) {
					return clazz.getSimpleName() + " (" + props + ")";
				}
			}
			return clazz.getSimpleName();
		}
		return type.getTypeName();
	}

	/**
	 * Describes the properties of a class (record components or declared fields).
	 * @param clazz the class to describe
	 * @return comma-separated property names, or empty string
	 */
	static String describeClassProperties(Class<?> clazz) {
		if (clazz.isRecord()) {
			RecordComponent[] components = clazz.getRecordComponents();
			if (components.length == 0) {
				return "";
			}
			StringJoiner joiner = new StringJoiner(", ");
			for (RecordComponent rc : components) {
				joiner.add(rc.getName());
			}
			return joiner.toString();
		}
		Field[] fields = clazz.getDeclaredFields();
		StringJoiner joiner = new StringJoiner(", ");
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				joiner.add(field.getName());
			}
		}
		return joiner.toString();
	}

}
