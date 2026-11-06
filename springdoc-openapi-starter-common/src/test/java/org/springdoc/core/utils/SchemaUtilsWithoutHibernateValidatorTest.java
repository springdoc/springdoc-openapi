/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.springdoc.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;

import org.springframework.web.bind.annotation.RequestHeader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the schema validations work without Hibernate Validator on the classpath, as its
 * {@code @Range} constraint is optional.
 *
 * @author bnasslahsen
 * @see <a href="https://github.com/springdoc/springdoc-openapi/issues/3405">#3405</a>
 */
class SchemaUtilsWithoutHibernateValidatorTest {

	@Test
	void validationsDoNotRequireHibernateValidator() throws Throwable {
		ClassLoader classLoader = new HibernateValidatorHidingClassLoader();
		assertThrows(ClassNotFoundException.class,
				() -> classLoader.loadClass("org.hibernate.validator.constraints.Range"));
		Class<?> schemaUtils = classLoader.loadClass(SchemaUtils.class.getName());
		List<Annotation> annotations = List.of(parameterAnnotation("header"), parameterAnnotation("sized"));
		Schema<?> schema = new StringSchema();

		boolean hasConstraints = (boolean) invoke(schemaUtils.getMethod("hasValidationConstraints", List.class),
				annotations);
		invoke(schemaUtils.getMethod("applyValidationsToSchema", Schema.class, List.class, String.class),
				schema, annotations, OpenApiVersion.OPENAPI_3_1.getVersion());

		assertTrue(hasConstraints);
		assertEquals(5, schema.getMaxLength());
	}

	private static Object invoke(Method method, Object... args) throws Throwable {
		try {
			return method.invoke(null, args);
		}
		catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	private static Annotation parameterAnnotation(String methodName) throws NoSuchMethodException {
		return SchemaUtilsWithoutHibernateValidatorTest.class.getDeclaredMethod(methodName, String.class)
				.getParameterAnnotations()[0][0];
	}

	private void header(@RequestHeader("X-Trace") String trace) {
	}

	private void sized(@Size(max = 5) String value) {
	}

	/**
	 * Loads {@link SchemaUtils} itself, so that the classes it refers to are resolved through this
	 * class loader, which hides Hibernate Validator. Every other class comes from the parent.
	 */
	private static class HibernateValidatorHidingClassLoader extends ClassLoader {

		HibernateValidatorHidingClassLoader() {
			super(SchemaUtils.class.getClassLoader());
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			if (name.startsWith("org.hibernate.validator."))
				throw new ClassNotFoundException(name);
			if (!name.startsWith(SchemaUtils.class.getName()))
				return super.loadClass(name, resolve);
			synchronized (getClassLoadingLock(name)) {
				Class<?> loaded = findLoadedClass(name);
				if (loaded == null) {
					try (InputStream in = getParent().getResourceAsStream(name.replace('.', '/') + ".class")) {
						if (in == null)
							throw new ClassNotFoundException(name);
						byte[] bytes = in.readAllBytes();
						loaded = defineClass(name, bytes, 0, bytes.length);
					}
					catch (IOException e) {
						throw new ClassNotFoundException(name, e);
					}
				}
				return loaded;
			}
		}

	}

}
