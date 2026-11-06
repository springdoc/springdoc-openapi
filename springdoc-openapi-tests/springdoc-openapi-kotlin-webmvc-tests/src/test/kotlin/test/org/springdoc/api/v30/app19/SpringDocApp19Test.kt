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

package test.org.springdoc.api.v30.app19

import io.swagger.v3.core.converter.ModelConverters
import org.springdoc.core.customizers.KotlinNullablePropertyCustomizer
import org.springdoc.core.utils.Constants
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.TestPropertySource
import test.org.springdoc.api.v30.AbstractKotlinSpringDocMVCTest

@TestPropertySource(
    properties = [
        "springdoc.api-docs.version=openapi_3_0", Constants.SPRINGDOC_KOTLIN_NULLABLE_PROPERTY_CUSTOMIZER_ENABLED +
            "=false",
    ],
)
class SpringDocApp19Test : AbstractKotlinSpringDocMVCTest() {
    companion object {
        init {
            ModelConverters
                .getInstance(false)
                .getConverters()
                .filterIsInstance<KotlinNullablePropertyCustomizer>()
                .forEach { ModelConverters.getInstance(false).removeConverter(it) }
        }
    }

    @SpringBootApplication
    @ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v30.app19"])
    class DemoApplication
}
