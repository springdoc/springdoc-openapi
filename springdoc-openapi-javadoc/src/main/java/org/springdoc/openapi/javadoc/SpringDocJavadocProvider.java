/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.openapi.javadoc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.FieldJavadoc;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.ParamJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;
import org.springdoc.core.JavadocProvider;

/**
 * The type Spring doc javadoc provider.
 * @author bnasslahsen
 */
public class SpringDocJavadocProvider implements JavadocProvider {

	/**
	 * The comment formatter.
	 */
	private final CommentFormatter formatter = new CommentFormatter();

	/**
	 * Gets method javadoc description.
	 *
	 * @param method the method
	 * @return the method javadoc description
	 */
	@Override
	public String getMethodJavadocDescription(Method method) {
		MethodJavadoc methodJavadoc = findMethodJavadoc(method);
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
		MethodJavadoc methodJavadoc = findMethodJavadoc(method);
		return formatter.format(methodJavadoc.getReturns());
	}

	/**
	 * Gets param javadoc.
	 *
	 * @param method the method
	 * @param name the name
	 * @return the param javadoc
	 */
	@Override
	public String getParamJavadoc(Method method, String name) {
		MethodJavadoc methodJavadoc = findMethodJavadoc(method);
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
		FieldJavadoc fieldJavadoc = RuntimeJavadoc.getJavadoc(field);
		return formatter.format(fieldJavadoc.getComment());
	}

	/**
	 * Find method javadoc method javadoc.
	 *
	 * @param method the method
	 * @return the method javadoc
	 */
	private MethodJavadoc findMethodJavadoc(Method method) {
		ClassJavadoc classJavadoc = RuntimeJavadoc.getJavadoc(method.getDeclaringClass());
		List<MethodJavadoc> methodDocs = classJavadoc.getMethods();
		// filter by method name
		List<MethodJavadoc> methodDocByMethodName = methodDocs.stream().filter(methodJavadoc -> methodJavadoc.getName().equals(method.getName())).collect(Collectors.toList());
		if (methodDocByMethodName.size() == 1)
			return methodDocByMethodName.get(0);
		// filter by parameters
		if (methodDocByMethodName.size() > 1) {
			List<MethodJavadoc> methodDocByParamType = methodDocByMethodName.stream().filter(methodJavadoc -> paramsMatch(method.getParameterTypes(), methodJavadoc.getParamTypes())).collect(Collectors.toList());
			if (methodDocByParamType.size() == 1)
				return methodDocByParamType.get(0);
		}
		return MethodJavadoc.createEmpty(method);
	}

	/**
	 * Params match boolean.
	 *
	 * @param paramTypesClass the param types class
	 * @param paramTypes the param types
	 * @return the boolean
	 */
	private boolean paramsMatch(Class<?>[] paramTypesClass, List<String> paramTypes) {
		List<String> paramTypesJavadoc = new ArrayList<>();
		for (int i = 0; i < paramTypes.size(); i++) {
			if (paramTypes.get(i).contains("::")) {
				String[] paramTypeArray = paramTypes.get(i).split("::");
				String paramType = paramTypeArray[paramTypeArray.length - 1].trim().replace(")", "");
				paramTypesJavadoc.add(paramType);
			}
			else
				paramTypesJavadoc.add(paramTypes.get(i));
		}
		return getCanonicalNames(paramTypesClass).equals(paramTypesJavadoc);
	}

	/**
	 * Gets canonical names.
	 *
	 * @param paramTypes the param types
	 * @return the canonical names
	 */
	private List<String> getCanonicalNames(Class<?>[] paramTypes) {
		List<String> methodParamsTypes = new ArrayList<>();
		for (Class<?> aClass : paramTypes) {
			methodParamsTypes.add(aClass.getCanonicalName());
		}
		return methodParamsTypes;
	}

}
