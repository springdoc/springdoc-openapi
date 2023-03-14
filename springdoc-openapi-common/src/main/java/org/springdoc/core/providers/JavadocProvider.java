/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2023 the original author or authors.
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
import java.util.Map;

/**
 * The interface Javadoc provider.
 * @author bnasslashen
 */
public interface JavadocProvider {

	/**
	 * Gets class description.
	 *
	 * @param cl the class
	 * @return the class description
	 */
	String getClassJavadoc(Class<?> cl);

	/**
	 * Gets param descripton of record class.
	 *
	 * @param cl the class
	 * @return map of field and param descriptions
	 */
	Map<String, String> getRecordClassParamJavadoc(Class<?> cl);

		/**
		 * Gets method description.
		 *
		 * @param method the method
		 * @return the method description
		 */
	String getMethodJavadocDescription(Method method);

	/**
	 * Gets method javadoc return.
	 *
	 * @param method the method
	 * @return the method javadoc return
	 */
	String getMethodJavadocReturn(Method method);

	/**
	 * Gets method throws declaration.
	 *
	 * @param method the method
	 * @return the method throws (name-description map)
	 */
	Map<String, String> getMethodJavadocThrows(Method method);

	/**
	 * Gets param javadoc.
	 *
	 * @param method the method
	 * @param name the name
	 * @return the param javadoc
	 */
	String getParamJavadoc(Method method, String name);

	/**
	 * Gets field javadoc.
	 *
	 * @param field the field
	 * @return the field javadoc
	 */
	String getFieldJavadoc(Field field);

	/**
	 * Returns the first sentence of a javadoc comment.
	 * @param text the javadoc comment's text
	 * @return the first sentence based on javadoc guidelines
	 */
	String getFirstSentence(String text);
}
