/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v31.app39;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that HateoasProperties-dependent bean methods are isolated in nested
 * configuration classes guarded by {@code @ConditionalOnClass} /
 * {@code @ConditionalOnMissingClass}, so that when HateoasProperties is absent
 * from the classpath, Spring never loads the class that references it.
 *
 * @author bnasslahsen
 */
class SpringDocApp39Test {

	private static final String HATEOAS_PROPERTIES_CLASS = "org.springframework.boot.hateoas.autoconfigure.HateoasProperties";

	@Test
	void dataRestConfigOuterClassDoesNotReferenceHateoasProperties() {
		assertOuterBeanMethodsDoNotReferenceHateoasProperties(SpringDocDataRestConfiguration.class);
	}

	@Test
	void dataRestConfigHasGuardedNestedClasses() {
		assertHasGuardedNestedClasses(SpringDocDataRestConfiguration.class);
	}

	/**
	 * Asserts that no {@code @Bean} method declared directly on the outer
	 * configuration class has {@code HateoasProperties} in its generic
	 * parameter types. Before the fix, the outer class had methods like
	 * {@code halProvider(Optional<HateoasProperties>...)} which caused
	 * {@code TypeNotPresentException} when HateoasProperties was absent.
	 */
	private void assertOuterBeanMethodsDoNotReferenceHateoasProperties(Class<?> configClass) {
		for (Method method : configClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Bean.class)) {
				for (Type type : method.getGenericParameterTypes()) {
					assertThat(type.getTypeName())
							.as("@Bean method %s.%s() should not reference HateoasProperties directly",
									configClass.getSimpleName(), method.getName())
							.doesNotContain("HateoasProperties");
				}
			}
		}
	}

	/**
	 * Asserts that the configuration class has at least one nested class
	 * guarded by {@code @ConditionalOnClass(name = "...HateoasProperties")}
	 * and one by {@code @ConditionalOnMissingClass("...HateoasProperties")}.
	 */
	private void assertHasGuardedNestedClasses(Class<?> configClass) {
		boolean hasConditionalOnClass = false;
		boolean hasConditionalOnMissingClass = false;

		for (Class<?> inner : configClass.getDeclaredClasses()) {
			ConditionalOnClass onClass = inner.getAnnotation(ConditionalOnClass.class);
			if (onClass != null && Arrays.asList(onClass.name()).contains(HATEOAS_PROPERTIES_CLASS)) {
				hasConditionalOnClass = true;
			}
			ConditionalOnMissingClass onMissingClass = inner.getAnnotation(ConditionalOnMissingClass.class);
			if (onMissingClass != null && Arrays.asList(onMissingClass.value()).contains(HATEOAS_PROPERTIES_CLASS)) {
				hasConditionalOnMissingClass = true;
			}
		}

		assertThat(hasConditionalOnClass)
				.as("%s should have a nested class with @ConditionalOnClass for HateoasProperties",
						configClass.getSimpleName())
				.isTrue();
		assertThat(hasConditionalOnMissingClass)
				.as("%s should have a nested class with @ConditionalOnMissingClass for HateoasProperties",
						configClass.getSimpleName())
				.isTrue();
	}

}
