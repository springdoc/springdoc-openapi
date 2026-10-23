/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package org.springdoc.core.customizers

import com.fasterxml.jackson.databind.JavaType
import org.springdoc.core.providers.ObjectMapperProvider
import kotlin.reflect.full.memberProperties

/**
 * Marks schema properties as nullable for Kotlin data class fields whose
 * return type is marked nullable (`Type?`).
 *
 * See: https://github.com/springdoc/springdoc-openapi/issues/906
 *
 * @author Jeffrey Blayney
 * @see AbstractNullablePropertyCustomizer
 */
class KotlinNullablePropertyCustomizer(
	objectMapperProvider: ObjectMapperProvider
) : AbstractNullablePropertyCustomizer(objectMapperProvider) {

	override fun findNullableProperties(javaType: JavaType): Set<String> {
		val kotlinClass = try {
			javaType.rawClass.kotlin
		} catch (_: Throwable) {
			return emptySet()
		}
		return kotlinClass.memberProperties
			.filter { it.returnType.isMarkedNullable }
			.mapTo(LinkedHashSet()) { it.name }
	}
}
