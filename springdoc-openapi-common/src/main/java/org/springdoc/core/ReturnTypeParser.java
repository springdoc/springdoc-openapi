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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * The interface Return type parser.
 * @author bnasslahsen
 */
public interface ReturnTypeParser {

	/**
	 * This is a copy of GenericTypeResolver.resolveType which is not available on spring 4.
	 * This also keeps compatibility with spring-boot 1 applications.
	 * Resolve the given generic type against the given context class,
	 * substituting type variables as far as possible.
	 * @param genericType the (potentially) generic type
	 * @param contextClass a context class for the target type, for example a class in which the target type appears in a method signature (can be {@code null})
	 * @return the resolved type (possibly the given generic type as-is)
	 * @since 5.0
	 */
	static Type resolveType(Type genericType, @Nullable Class<?> contextClass) {
		if (contextClass != null) {
			if (genericType instanceof TypeVariable) {
				ResolvableType resolvedTypeVariable = resolveVariable(
						(TypeVariable<?>) genericType, ResolvableType.forClass(contextClass));
				if (resolvedTypeVariable != ResolvableType.NONE) {
					Class<?> resolved = resolvedTypeVariable.resolve();
					if (resolved != null) {
						return resolved;
					}
				}
			}
			else if (genericType instanceof ParameterizedType) {
				ResolvableType resolvedType = ResolvableType.forType(genericType);
				if (resolvedType.hasUnresolvableGenerics()) {
					ResolvableType resolvableType = getResolvedType(resolvedType, contextClass);
					if (resolvableType != null)
						return resolvableType.getType();
				}
				else if (resolvedType.hasGenerics()) {
					ResolvableType[] resolvableTypes = resolvedType.getGenerics();
					resolveType(resolvableTypes, contextClass);
					return ResolvableType.forClassWithGenerics(Objects.requireNonNull(resolvedType.getRawClass()), resolvableTypes).getType();
				}
			}
		}
		return genericType;
	}

	/**
	 * Resolve type.
	 *
	 * @param resolvableTypes the resolvable types
	 * @param contextClass the context class
	 */
	static void resolveType(ResolvableType[] resolvableTypes, Class<?> contextClass) {
		for (int i = 0; i < resolvableTypes.length; i++) {
			if (resolvableTypes[i].hasUnresolvableGenerics() && resolvableTypes[i].getType() instanceof ParameterizedType) {
				ResolvableType resolvableType = getResolvedType(resolvableTypes[i], contextClass);
				if (resolvableType != null)
					resolvableTypes[i] = resolvableType;
			}
			else if (resolvableTypes[i].hasGenerics()) {
				resolveType(resolvableTypes[i].getGenerics(), contextClass);
				if (resolvableTypes[i].getRawClass() != null)
					resolvableTypes[i] = ResolvableType.forClassWithGenerics(Objects.requireNonNull(resolvableTypes[i].getRawClass()), resolvableTypes[i].getGenerics());
			}
		}
	}

	/**
	 * Gets resolved type.
	 *
	 * @param resolvableType the resolvable type
	 * @param contextClass the context class
	 * @return the resolved type
	 */
	static ResolvableType getResolvedType(ResolvableType resolvableType, Class<?> contextClass) {
		ParameterizedType parameterizedType = (ParameterizedType) resolvableType.getType();
		Class<?>[] generics = new Class<?>[parameterizedType.getActualTypeArguments().length];
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		ResolvableType contextType = ResolvableType.forClass(contextClass);
		findTypeForGenerics(generics, typeArguments, contextType);
		Class<?> rawClass = resolvableType.getRawClass();
		if (rawClass != null)
			return ResolvableType.forClassWithGenerics(rawClass, generics);
		return null;
	}

	/**
	 * Find type for generics.
	 *
	 * @param generics the generics
	 * @param typeArguments the type arguments
	 * @param contextType the context type
	 */
	static void findTypeForGenerics(Class<?>[] generics, Type[] typeArguments, ResolvableType contextType) {
		for (int i = 0; i < typeArguments.length; i++) {
			Type typeArgument = typeArguments[i];
			if (typeArgument instanceof TypeVariable) {
				ResolvableType resolvedTypeArgument = resolveVariable(
						(TypeVariable<?>) typeArgument, contextType);
				if (resolvedTypeArgument != ResolvableType.NONE) {
					generics[i] = resolvedTypeArgument.resolve();
				}
				else {
					generics[i] = ResolvableType.forType(typeArgument).resolve();
				}
			}
			else {
				generics[i] = ResolvableType.forType(typeArgument).resolve();
			}
		}
	}

	/**
	 * Resolve variable resolvable type.
	 *
	 * @param typeVariable the type variable
	 * @param contextType the context type
	 * @return the resolvable type
	 */
	static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
		ResolvableType resolvedType;
		if (contextType.hasGenerics() && Objects.equals(contextType.getRawClass(), typeVariable.getGenericDeclaration())) {
			resolvedType = ResolvableType.forType(typeVariable, contextType);
			if (resolvedType.resolve() != null) {
				return resolvedType;
			}
		}

		ResolvableType superType = contextType.getSuperType();
		if (superType != ResolvableType.NONE) {
			resolvedType = resolveVariable(typeVariable, superType);
			if (resolvedType.resolve() != null) {
				return resolvedType;
			}
		}
		for (ResolvableType ifc : contextType.getInterfaces()) {
			resolvedType = resolveVariable(typeVariable, ifc);
			if (resolvedType.resolve() != null) {
				return resolvedType;
			}
		}
		return ResolvableType.NONE;
	}

	/**
	 * Gets type.
	 *
	 * @param methodParameter the method parameter
	 * @return the type
	 */
	static Type getType(MethodParameter methodParameter) {
		Type genericParameterType = methodParameter.getGenericParameterType();
		if (genericParameterType instanceof ParameterizedType || genericParameterType instanceof TypeVariable)
			return ReturnTypeParser.resolveType(genericParameterType, methodParameter.getContainingClass());
		return methodParameter.getParameterType();
	}

	/**
	 * Gets return type.
	 *
	 * @param methodParameter the method parameter
	 * @return the return type
	 */
	default Type getReturnType(MethodParameter methodParameter) {
		if (methodParameter.getGenericParameterType() instanceof ParameterizedType)
			return ReturnTypeParser.resolveType(methodParameter.getGenericParameterType(), methodParameter.getContainingClass());
		return methodParameter.getParameterType();
	}

}
