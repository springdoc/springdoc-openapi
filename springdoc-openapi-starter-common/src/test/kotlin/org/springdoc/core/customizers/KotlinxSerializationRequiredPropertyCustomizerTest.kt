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

package org.springdoc.core.customizers

import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.models.media.Schema as SchemaModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.ObjectMapperProvider

/**
 * Tests the [KotlinxSerializationRequiredPropertyCustomizer].
 *
 * @author bnasslahsen
 */
class KotlinxSerializationRequiredPropertyCustomizerTest {

	@Serializable
	data class Person(
		val name: String,
		val age: Int,
		val nickname: String? = null,
		val title: String?,
		@Required val country: String = "FR",
		@Transient val cache: String = "",
		@Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED) val note: String,
		@Schema(requiredMode = Schema.RequiredMode.REQUIRED) val language: String = "fr",
		val address: Address,
	)

	@Serializable
	data class Address(val city: String, val zip: String? = null)

	data class PlainPerson(val name: String, val nickname: String? = null)

	@ParameterizedTest
	@ValueSource(booleans = [false, true])
	fun requiredPropertiesOfSerializableClass(openapi31: Boolean) {
		val schemas = readAll(Person::class.java, openapi31)

		assertThat(schemas["Person"]!!.required).containsExactlyInAnyOrder("name", "age", "title", "country", "language", "address")
		assertThat(schemas["Address"]!!.required).containsExactly("city")
	}

	@ParameterizedTest
	@ValueSource(booleans = [false, true])
	fun noRequiredPropertiesWithoutCustomizer(openapi31: Boolean) {
		val schemas = readAll(Person::class.java, openapi31, false)

		assertThat(schemas["Person"]!!.required).containsExactly("language")
		assertThat(schemas["Address"]!!.required).isNull()
	}

	@ParameterizedTest
	@ValueSource(booleans = [false, true])
	fun nonSerializableClassUnchanged(openapi31: Boolean) {
		assertThat(readAll(PlainPerson::class.java, openapi31)["PlainPerson"]!!.required)
			.isEqualTo(readAll(PlainPerson::class.java, openapi31, false)["PlainPerson"]!!.required)
	}

	private fun readAll(type: Class<*>, openapi31: Boolean, withCustomizer: Boolean = true): Map<String, SchemaModel<*>> {
		val modelConverters = ModelConverters(openapi31)
		if (withCustomizer)
			modelConverters.addConverter(KotlinxSerializationRequiredPropertyCustomizer(ObjectMapperProvider(SpringDocConfigProperties())))
		@Suppress("UNCHECKED_CAST")
		return modelConverters.readAll(type) as Map<String, SchemaModel<*>>
	}
}
