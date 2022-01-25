/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.FieldJavadoc;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.ParamJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;

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
		MethodJavadoc methodJavadoc = RuntimeJavadoc.getJavadoc(method);
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
		MethodJavadoc methodJavadoc = RuntimeJavadoc.getJavadoc(method);
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
		MethodJavadoc methodJavadoc = RuntimeJavadoc.getJavadoc(method);
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

}
