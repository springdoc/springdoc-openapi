/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.converters.ConverterUtils;
import org.springdoc.core.converters.PolymorphicModelConverter;
import org.springdoc.core.converters.SchemaPropertyDeprecatingConverter;
import org.springdoc.core.extractor.MethodParameterPojoExtractor;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.GenericResponseService;

import org.springframework.core.MethodParameter;

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
	 * @param clazz the clazz
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeFromSchemaMap(Class<?> clazz) {
		AdditionalModelsConverter.removeFromSchemaMap(clazz);
		return this;
	}

	/**
	 * Remove from schema class spring doc utils.
	 *
	 * @param clazz the clazz
	 * @return the spring doc utils
	 */
	public SpringDocUtils removeFromSchemaClass(Class<?> clazz) {
		AdditionalModelsConverter.removeFromClassMap(clazz);
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

	/**
	 * Is valid path boolean.
	 *
	 * @param path the path
	 * @return the boolean
	 */
	public static boolean isValidPath(String path) {
		if (StringUtils.isNotBlank(path) && !path.equals("/"))
			return true;
		return false;
	}

	/**
	 * Add parent type spring doc utils.
	 *
	 * @param parentTypes the parent types
	 * @return the spring doc utils
	 */
	public SpringDocUtils addParentType(String ...parentTypes) {
		PolymorphicModelConverter.addParentType(parentTypes);
		return this;
	}

	/**
	 * Gets parameter annotations.
	 *
	 * @param methodParameter the method parameter
	 * @return the parameter annotations
	 */
	@NotNull
	public static Annotation[] getParameterAnnotations(MethodParameter methodParameter) {
		// Get the parameter annotations directly as an array
		Annotation[] annotations = methodParameter.getParameterAnnotations();
		// Return early if no annotations are found, avoiding unnecessary processing
		if (ArrayUtils.isEmpty(annotations)) {
			return new Annotation[0];
		}
		// Create a list that can contain both parameter and meta-annotations
		List<Annotation> resultAnnotations = new ArrayList<>(annotations.length);

		// Add all direct annotations
		resultAnnotations.addAll(List.of(annotations));

		// Process each direct annotation to collect its meta-annotations
		for (Annotation annotation : annotations) {
			// Fetch meta-annotations
			Annotation[] metaAnnotations = annotation.annotationType().getAnnotations();

			// Only add meta-annotations if they exist
			if (ArrayUtils.isNotEmpty(metaAnnotations)) {
				resultAnnotations.addAll(List.of(metaAnnotations));
			}
		}
		// Convert the list to an array and return
		return resultAnnotations.toArray(new Annotation[0]);
	}
}

