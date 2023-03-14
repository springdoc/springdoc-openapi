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

package org.springdoc.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.WebConversionServiceProvider;

import org.springframework.core.MethodParameter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link GenericParameterService}.
 */
class GenericParameterServiceTest {
	@TempDir
	private File tempDir;

	@Mock
	private PropertyResolverUtils propertyResolverUtils;

	@Mock
	private DelegatingMethodParameterCustomizer delegatingMethodParameterCustomizer;

	@Mock
	private WebConversionServiceProvider webConversionServiceProvider;

	@Mock
	private ObjectMapperProvider objectMapperProvider;

	@Mock
	private JavadocProvider javadocProvider;

	private GenericParameterService genericParameterService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		this.genericParameterService = new GenericParameterService(propertyResolverUtils, Optional.of(delegatingMethodParameterCustomizer), Optional.of(webConversionServiceProvider), objectMapperProvider, Optional.of(javadocProvider));
	}

	/**
	 * Tests for {@link GenericParameterService#getParamJavadoc(JavadocProvider, MethodParameter)}.
	 */
	@Nested
	class getParamJavadoc {
		@Mock
		private DelegatingMethodParameter methodParameter;

		@BeforeEach
		void setup() {
			MockitoAnnotations.openMocks(this);
		}

		@Test
		@EnabledForJreRange(min = JRE.JAVA_17)
		void hasDescriptionOfRecordObject() throws IOException, ClassNotFoundException, NoSuchMethodException {
			Class cls = createRecordObject();
			Method method = cls.getMethod("name");

			when(methodParameter.getParameterName()).thenReturn("name");
			when(methodParameter.isParameterObject()).thenReturn(true);
			when(methodParameter.getExecutable()).thenReturn(method);

			Map<String, String> recordParamMap = new HashMap<>();
			recordParamMap.put("id", "the id");
			recordParamMap.put("name", "the name");
			when(javadocProvider.getRecordClassParamJavadoc(cls)).thenReturn(recordParamMap);

			when(javadocProvider.getFieldJavadoc(any())).thenReturn(null);

			String actual = genericParameterService.getParamJavadoc(javadocProvider, methodParameter);
			assertEquals("the name", actual);

			verify(methodParameter).getParameterName();
			verify(methodParameter).isParameterObject();
			verify(methodParameter).getExecutable();
			verify(javadocProvider).getRecordClassParamJavadoc(cls);
			verify(javadocProvider).getFieldJavadoc(any());
		}

		@Test
		void hasDescriptionOfClassObject() throws IOException, ClassNotFoundException, NoSuchMethodException {
			Class cls = ClassObject.class;
			Method method = cls.getMethod("getName");

			when(methodParameter.getParameterName()).thenReturn("name");
			when(methodParameter.isParameterObject()).thenReturn(true);
			when(methodParameter.getExecutable()).thenReturn(method);

			when(javadocProvider.getFieldJavadoc(any())).thenReturn("the name");

			String actual = genericParameterService.getParamJavadoc(javadocProvider, methodParameter);
			assertEquals("the name", actual);

			verify(methodParameter).getParameterName();
			verify(methodParameter).isParameterObject();
			verify(methodParameter).getExecutable();
			verify(javadocProvider).getFieldJavadoc(any());
		}

		private Class<?> createRecordObject() throws IOException, ClassNotFoundException {
			File recordObject = new File(tempDir, "RecordObject.java");
			try (PrintWriter writer = new PrintWriter(new FileWriter(recordObject))) {
				writer.println("public record RecordObject(String id, String name){");
				writer.println("}");
			}
			String[] args = {
					recordObject.getAbsolutePath()
			};
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int r = compiler.run(null, null, null, args);
			if (r != 0) {
				throw new IllegalStateException("Compilation failed");
			}
			URL[] urls = { tempDir.toURI().toURL() };
			ClassLoader loader = URLClassLoader.newInstance(urls);

			return loader.loadClass("RecordObject");
		}

		private class ClassObject {
			/**
			 * the id
			 */
			private String id;

			/**
			 * the name
			 */
			private String name;

			public ClassObject(String id, String name) {
				this.id = id;
				this.name = name;
			}

			public String getId() {
				return id;
			}

			public String getName() {
				return name;
			}
		}
	}
}
