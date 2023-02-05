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

package org.springdoc.core;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.converters.ConverterUtils;
import org.springdoc.core.converters.SchemaPropertyDeprecatingConverter;

/**
 * The type Spring doc utils.
 *
 * @author bnasslahsen
 */
public class SpringDocUtils {

	/**
	 * The constant springDocConfig.
	 */
	private static final SpringDocUtils springDocConfig = new SpringDocUtils();

	/**
	 * Instantiates a new Spring doc utils.
	 */
	private SpringDocUtils() {
	}

	/**
	 * Gets config.
	 *
	 * @return the config
	 */
	public static SpringDocUtils getConfig() {
		return springDocConfig;
	}

	/**
	 * Add deprecated type spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils addDeprecatedType(Class<? extends Annotation> cls) {
		SchemaPropertyDeprecatingConverter.addDeprecatedType(cls);
		return this;
	}

	/**
	 * Add rest controllers spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addRestControllers(Class<?>... classes) {
		AbstractOpenApiResource.addRestControllers(classes);
		return this;
	}

	/**
	 * Add hidden rest controllers spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addHiddenRestControllers(Class<?>... classes) {
		AbstractOpenApiResource.addHiddenRestControllers(classes);
		return this;
	}

	/**
	 * Add hidden rest controllers spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addHiddenRestControllers(String... classes) {
		AbstractOpenApiResource.addHiddenRestControllers(classes);
		return this;
	}

	/**
	 * Replace with class spring doc utils.
	 *
	 * @param source the source
	 * @param target the target
	 * @return the spring doc utils
	 */
	public SpringDocUtils replaceWithClass(Class source, Class target) {
		AdditionalModelsConverter.replaceWithClass(source, target);
		return this;
	}

	/**
	 * Replace with schema spring doc utils.
	 *
	 * @param source the source
	 * @param target the target
	 * @return the spring doc utils
	 */
	public SpringDocUtils replaceWithSchema(Class source, Schema target) {
		AdditionalModelsConverter.replaceWithSchema(source, target);
		return this;
	}

	/**
	 * Add request wrapper to ignore spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addRequestWrapperToIgnore(Class<?>... classes) {
		AbstractRequestService.addRequestWrapperToIgnore(classes);
		return this;
	}

	/**
	 * Remove request wrapper to ignore spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeRequestWrapperToIgnore(Class<?>... classes) {
		AbstractRequestService.removeRequestWrapperToIgnore(classes);
		return this;
	}

	/**
	 * Add file type spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addFileType(Class<?>... classes) {
		GenericParameterService.addFileType(classes);
		return this;
	}

	/**
	 * Add response wrapper to ignore spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils addResponseWrapperToIgnore(Class<?> cls) {
		ConverterUtils.addResponseWrapperToIgnore(cls);
		return this;
	}

	/**
	 * Remove response wrapper to ignore spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeResponseWrapperToIgnore(Class<?> cls) {
		ConverterUtils.removeResponseWrapperToIgnore(cls);
		return this;
	}

	/**
	 * Add response type to ignore spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils addResponseTypeToIgnore(Class<?> cls) {
		ConverterUtils.addResponseTypeToIgnore(cls);
		return this;
	}

	/**
	 * Remove response type to ignore spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeResponseTypeToIgnore(Class<?> cls) {
		ConverterUtils.removeResponseTypeToIgnore(cls);
		return this;
	}

	/**
	 * Add annotations to ignore spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addAnnotationsToIgnore(Class<?>... classes) {
		SpringDocAnnotationsUtils.addAnnotationsToIgnore(classes);
		return this;
	}

	/**
	 * Remove annotations to ignore spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeAnnotationsToIgnore(Class<?>... classes) {
		SpringDocAnnotationsUtils.removeAnnotationsToIgnore(classes);
		return this;
	}

	/**
	 * Add flux wrapper to ignore spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils addFluxWrapperToIgnore(Class<?> cls) {
		ConverterUtils.addFluxWrapperToIgnore(cls);
		return this;
	}

	/**
	 * Remove flux wrapper to ignore spring doc utils.
	 *
	 * @param cls the cls
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeFluxWrapperToIgnore(Class<?> cls) {
		ConverterUtils.removeFluxWrapperToIgnore(cls);
		return this;
	}

	/**
	 * Add simple types for parameter object spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils addSimpleTypesForParameterObject(Class<?>... classes) {
		MethodParameterPojoExtractor.addSimpleTypes(classes);
		return this;
	}

	/**
	 * Remove simple types for parameter object spring doc utils.
	 *
	 * @param classes the classes
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeSimpleTypesForParameterObject(Class<?>... classes) {
		MethodParameterPojoExtractor.removeSimpleTypes(classes);
		return this;
	}

	/**
	 * Add simple type predicate for parameter object spring doc utils.
	 *
	 * @param predicate the predicate
	 * @return the spring doc utils
	 */
	public SpringDocUtils addSimpleTypePredicateForParameterObject(Predicate<Class<?>> predicate) {
		MethodParameterPojoExtractor.addSimpleTypePredicate(predicate);
		return this;
	}

	/**
	 * Disable replacement spring doc utils.
	 *
	 * @param source the source
	 * @return the spring doc utils
	 */
	public SpringDocUtils disableReplacement(Class source) {
		AdditionalModelsConverter.disableReplacement(source);
		return this;
	}

	/**
	 * Replace the ParameterObject with the target class.
	 *
	 * @param source the source
	 * @param target the target
	 * @return the spring doc utils
	 */
	public SpringDocUtils replaceParameterObjectWithClass(Class source, Class target) {
		AdditionalModelsConverter.replaceParameterObjectWithClass(source, target);
		return this;
	}

	/**
	 * Sets response entity exception handler class.
	 *
	 * @param clazz the clazz
	 * @return the response entity exception handler class
	 */
	public SpringDocUtils setResponseEntityExceptionHandlerClass(Class<?> clazz) {
		GenericResponseService.setResponseEntityExceptionHandlerClass(clazz);
		return this;
	}

	/**
	 * Sets model and view class.
	 *
	 * @param clazz the clazz
	 * @return the model and view class
	 */
	public SpringDocUtils setModelAndViewClass(Class<?> clazz) {
		AbstractOpenApiResource.setModelAndViewClass(clazz);
		return this;
	}

	/**
	 * Remove from schema map spring doc utils.
	 *
	 * @param clazzs the clazzs
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeFromSchemaMap(Class<?> clazzs) {
		AdditionalModelsConverter.removeFromSchemaMap(clazzs);
		return this;
	}

	/**
	 * Remove from schema class spring doc utils.
	 *
	 * @param clazzs the clazzs
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeFromSchemaClass(Class<?> clazzs) {
		AdditionalModelsConverter.removeFromClassMap(clazzs);
		return this;
	}

	/**
	 * Add java type to ignore spring doc utils.
	 *
	 * @param clazz the clazz
	 * @return the spring doc utils
	 */
	public SpringDocUtils addJavaTypeToIgnore(Class<?> clazz) {
		ConverterUtils.addJavaTypeToIgnore(clazz);
		return this;
	}

	/**
	 * Remove java type to ignore spring doc utils.
	 *
	 * @param clazz the clazz
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeJavaTypeToIgnore(Class<?> clazz) {
		ConverterUtils.removeJavaTypeToIgnore(clazz);
		return this;
	}
}

