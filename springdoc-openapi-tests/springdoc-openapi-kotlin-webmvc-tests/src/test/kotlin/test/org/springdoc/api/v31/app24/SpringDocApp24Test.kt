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

package test.org.springdoc.api.v31.app24

import io.swagger.v3.core.converter.ModelConverters
import org.springdoc.core.customizers.KotlinNullablePropertyCustomizer
import org.springdoc.core.utils.Constants
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.TestPropertySource
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

@TestPropertySource(properties = [Constants.SPRINGDOC_KOTLIN_NULLABLE_PROPERTY_CUSTOMIZER_ENABLED + "=false"])
class SpringDocApp24Test : AbstractKotlinSpringDocMVCTest() {
    companion object {
        init {
            ModelConverters
                .getInstance(true)
                .getConverters()
                .filterIsInstance<KotlinNullablePropertyCustomizer>()
                .forEach { ModelConverters.getInstance(true).removeConverter(it) }
        }
    }

    @SpringBootApplication
    @ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v31.app24"])
    class DemoApplication
}
