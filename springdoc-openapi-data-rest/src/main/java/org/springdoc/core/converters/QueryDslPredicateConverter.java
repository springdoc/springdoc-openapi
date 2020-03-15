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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JavaType;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.util.CastUtils;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;


public class QueryDslPredicateConverter implements ModelConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryDslPredicateConverter.class);

	private QuerydslBindingsFactory querydslBindingsFactory;

	public QueryDslPredicateConverter(QuerydslBindingsFactory querydslBindingsFactory) {
		this.querydslBindingsFactory = querydslBindingsFactory;
	}

	@Override
	public Schema resolve(AnnotatedType annotatedType, ModelConverterContext modelConverterContext, Iterator<ModelConverter> iterator) {
		JavaType javaType = Json.mapper().constructType(annotatedType.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (Predicate.class.isAssignableFrom(cls)) {
				Optional<QuerydslPredicate> predicate = Arrays.stream(annotatedType.getCtxAnnotations()).filter(QuerydslPredicate.class::isInstance).map(QuerydslPredicate.class::cast).findAny();
				if (predicate.isPresent()) {
					try {
						Class<?> tClass = getClassFromPredicate(predicate.get());
						return this.resolve(new AnnotatedType(tClass).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()).resolveAsRef(true), modelConverterContext, iterator);
					}
					catch (CannotCompileException e) {
						LOGGER.warn("Can not compile : {}", e.getMessage());
					}
				}
			}
		}
		return iterator.hasNext() ? iterator.next().resolve(annotatedType, modelConverterContext, iterator) : null;
	}

	private Class<?> getClassFromPredicate(QuerydslPredicate predicate) throws CannotCompileException {
		ClassTypeInformation<?> classTypeInformation = ClassTypeInformation.from(predicate.root());
		TypeInformation<?> domainType = classTypeInformation.getRequiredActualType();
		Optional<Class<? extends QuerydslBinderCustomizer<?>>> bindingsAnnotation = Optional.of(predicate)
				.map(QuerydslPredicate::bindings)
				.map(CastUtils::cast);
		QuerydslBindings bindings = bindingsAnnotation
				.map(it -> querydslBindingsFactory.createBindingsFor(domainType, it))
				.orElseGet(() -> querydslBindingsFactory.createBindingsFor(domainType));
		String generatedClassName = "com.springdoc.core." + predicate.bindings().getSimpleName() + "G";
		ClassPool classPool = ClassPool.getDefault();
		CtClass classPoolOrNull = classPool.getOrNull(generatedClassName);
		if (classPoolOrNull == null) {
			classPoolOrNull = classPool.makeClass(generatedClassName);
			Set<String> fieldsToAdd = Arrays.stream(predicate.root().getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
			//remove blacklisted fields
			Set<String> blacklist = getBindingFieldValues(bindings, "blackList");
			fieldsToAdd.removeIf(blacklist::contains);
			Set<String> whiteList = getBindingFieldValues(bindings, "whiteList");
			Set<String> aliases = getBindingFieldValues(bindings, "aliases");
			fieldsToAdd.addAll(aliases);
			fieldsToAdd.addAll(whiteList);
			for (String fieldName : fieldsToAdd) {
				CtField f = new CtField(CtClass.charType, fieldName, classPoolOrNull);
				f.setModifiers(Modifier.PUBLIC);
				classPoolOrNull.addField(f);
			}
		}
		return classPoolOrNull.toClass(this.getClass().getClassLoader(), this.getClass().getProtectionDomain());
	}

	private Set<String> getBindingFieldValues(QuerydslBindings instance, String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			if (Modifier.isPrivate(field.getModifiers())) {
				field.setAccessible(true);
			}
			return (Set<String>) field.get(instance);
		}
		catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.warn("NoSuchFieldException or IllegalAccessException occured : {}", e.getMessage());
		}
		return Collections.emptySet();
	}
}
