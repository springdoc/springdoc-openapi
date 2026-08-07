/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.autoconfigure;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An auto-configuration that is only registered when {@link SpringDocConfiguration} already
 * contributed its beans has to declare that order, otherwise the {@code @ConditionalOnBean} match
 * depends on where the class name happens to sort in Spring Boot's auto-configuration sort. A
 * third-party auto-configuration that sorts earlier and orders itself around a springdoc
 * auto-configuration is then enough to move that class ahead of {@link SpringDocConfiguration},
 * which silently drops it (gh-3313).
 *
 * @author kdelay
 */
class SpringDocAutoConfigurationOrderTest {

	private static final String IMPORTS_RESOURCE = "META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

	private final MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

	@Test
	void auto_configurations_conditional_on_spring_doc_configuration_declare_their_order() throws IOException {
		List<String> gatedWithoutDeclaredOrder = new ArrayList<>();
		for (String candidate : autoConfigurationImports()) {
			AnnotationMetadata metadata = annotationMetadata(candidate);
			if (isConditionalOnSpringDocConfiguration(metadata) && !declaresOrderAfterSpringDocConfiguration(metadata)) {
				gatedWithoutDeclaredOrder.add(candidate);
			}
		}
		assertThat(gatedWithoutDeclaredOrder).isEmpty();
	}

	private Set<String> autoConfigurationImports() throws IOException {
		Set<String> candidates = new LinkedHashSet<>();
		Enumeration<URL> resources = getClass().getClassLoader().getResources(IMPORTS_RESOURCE);
		while (resources.hasMoreElements()) {
			try (InputStream inputStream = resources.nextElement().openStream()) {
				String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
				for (String line : content.split("\n")) {
					String candidate = line.trim();
					if (StringUtils.hasText(candidate) && candidate.startsWith("org.springdoc.")) {
						candidates.add(candidate);
					}
				}
			}
		}
		assertThat(candidates).isNotEmpty();
		return candidates;
	}

	/**
	 * Reads the annotations without loading the class, because several of these
	 * auto-configurations reference optional types that are absent from this module's classpath.
	 * @param candidate the auto-configuration class name as written in the imports file
	 * @return the annotation metadata
	 */
	private AnnotationMetadata annotationMetadata(String candidate) throws IOException {
		try {
			return this.metadataReaderFactory.getMetadataReader(candidate).getAnnotationMetadata();
		}
		catch (IOException ex) {
			// nested classes are written with a dot separator in the imports file
			int lastDot = candidate.lastIndexOf('.');
			String nested = candidate.substring(0, lastDot) + '$' + candidate.substring(lastDot + 1);
			return this.metadataReaderFactory.getMetadataReader(nested).getAnnotationMetadata();
		}
	}

	private boolean isConditionalOnSpringDocConfiguration(AnnotationMetadata metadata) {
		return referencesSpringDocConfiguration(metadata.getAnnotationAttributes(ConditionalOnBean.class.getName(), true),
				"value", "name");
	}

	private boolean declaresOrderAfterSpringDocConfiguration(AnnotationMetadata metadata) {
		return referencesSpringDocConfiguration(metadata.getAnnotationAttributes(AutoConfigureAfter.class.getName(), true),
				"value", "name")
				|| referencesSpringDocConfiguration(metadata.getAnnotationAttributes(AutoConfiguration.class.getName(), true),
						"after", "afterName");
	}

	private boolean referencesSpringDocConfiguration(Map<String, Object> attributes, String... attributeNames) {
		if (attributes == null) {
			return false;
		}
		for (String attributeName : attributeNames) {
			Object value = attributes.get(attributeName);
			if (value instanceof String[] values
					&& Arrays.asList(values).contains(SpringDocConfiguration.class.getName())) {
				return true;
			}
			if (value instanceof Collection<?> values && values.contains(SpringDocConfiguration.class.getName())) {
				return true;
			}
		}
		return false;
	}

}
