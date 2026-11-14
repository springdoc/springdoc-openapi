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

package org.springdoc.core.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springdoc.core.configuration.SpringDocKotlinConfiguration.KotlinReflectDependingConfiguration
import org.springdoc.core.customizers.KotlinxSerializationRequiredPropertyCustomizer
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.ObjectMapperProvider
import org.springdoc.core.utils.Constants
import org.springframework.boot.test.context.FilteredClassLoader
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import tools.jackson.module.kotlin.KotlinModule

/**
 * Tests that the [KotlinxSerializationRequiredPropertyCustomizer] is only registered when
 * `jackson-module-kotlin` is absent, which is simulated with a [FilteredClassLoader].
 *
 * @author bnasslahsen
 */
class SpringDocKotlinxSerializationConfigurationTest {

	private val contextRunner = ApplicationContextRunner()
		.withBean(ObjectMapperProvider::class.java, { ObjectMapperProvider(SpringDocConfigProperties()) })
		.withUserConfiguration(KotlinReflectDependingConfiguration::class.java)

	@Test
	fun registeredWithoutJacksonModuleKotlin() {
		contextRunner.withClassLoader(FilteredClassLoader(KotlinModule::class.java))
			.run { context -> assertThat(context).hasSingleBean(KotlinxSerializationRequiredPropertyCustomizer::class.java) }
	}

	@Test
	fun notRegisteredWithJacksonModuleKotlin() {
		contextRunner
			.run { context -> assertThat(context).doesNotHaveBean(KotlinxSerializationRequiredPropertyCustomizer::class.java) }
	}

	@Test
	fun notRegisteredWhenDisabled() {
		contextRunner.withClassLoader(FilteredClassLoader(KotlinModule::class.java))
			.withPropertyValues(Constants.SPRINGDOC_KOTLINX_SERIALIZATION_REQUIRED_PROPERTY_CUSTOMIZER_ENABLED + "=false")
			.run { context -> assertThat(context).doesNotHaveBean(KotlinxSerializationRequiredPropertyCustomizer::class.java) }
	}

	@Test
	fun notRegisteredWithoutKotlinxSerialization() {
		contextRunner.withClassLoader(FilteredClassLoader(KotlinModule::class.java, kotlinx.serialization.Serializable::class.java))
			.run { context -> assertThat(context).doesNotHaveBean(KotlinxSerializationRequiredPropertyCustomizer::class.java) }
	}
}
