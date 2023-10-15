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

package test.org.springdoc.api.app172;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springdoc.core.customizers.JavadocPropertyCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.SpringDocJavadocProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link JavadocPropertyCustomizer}.
 */
class JavadocPropertyCustomizerTest {
	@TempDir
	private File tempDir;

	private JavadocProvider javadocProvider;

	@Mock
	private ObjectMapperProvider objectMapperProvider;

	private JavadocPropertyCustomizer javadocPropertyCustomizer;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		this.javadocProvider = new SpringDocJavadocProvider();
		this.javadocPropertyCustomizer = new JavadocPropertyCustomizer(javadocProvider, objectMapperProvider);
	}


	/**
	 * Tests for {@link JavadocPropertyCustomizer#setJavadocDescription(Class, List, List, Schema)}.
	 */
	@Nested
	class setJavadocDescription {
		@Test
		@EnabledForJreRange(min = JRE.JAVA_17)
		void ifRecordObjectShouldGetField() throws IOException, ClassNotFoundException, IntrospectionException {
			File recordObject = new File(tempDir, "RecordObject.java");
			try (PrintWriter writer = new PrintWriter(new FileWriter(recordObject))) {
				writer.println("/**");
				writer.println(" * Record Object");
				writer.println(" *");
				writer.println(" * @param id the id");
				writer.println(" * @param name the name");
				writer.println(" */");
				writer.println("public record RecordObject(String id, String name){");
				writer.println("}");
			}
			File recordObjectJavadocJson = new File(tempDir, "RecordObject__Javadoc.json");
			try (PrintWriter writer = new PrintWriter(new FileWriter(recordObjectJavadocJson))) {
				writer.print("{");
				writer.print("\"doc\":\"Record Object\\n\\n @param id the id\\n @param name the name\",");
				writer.print("\"fields\":[],");
				writer.print("\"methods\":[],");
				writer.print("\"constructors\":[]");
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

			Class<?> cls = loader.loadClass("RecordObject");

			List<Field> fields = Arrays.asList(cls.getFields());

			Schema existingSchema = new ObjectSchema().name("RecordObject")
					.addProperty("id", new StringSchema().name("id"))
					.addProperty("name", new StringSchema().name("name"));

			List<PropertyDescriptor> propertyDescriptors = Arrays.asList(Introspector.getBeanInfo(cls).getPropertyDescriptors());
			javadocPropertyCustomizer.setJavadocDescription(cls, fields, propertyDescriptors, existingSchema,false);

			assertEquals("Record Object", existingSchema.getDescription());
			Map<String, Schema> properties = existingSchema.getProperties();
			assertEquals(2, properties.size());
			assertEquals("the id", properties.get("id").getDescription());
			assertEquals("the name", properties.get("name").getDescription());
		}
	}
}
