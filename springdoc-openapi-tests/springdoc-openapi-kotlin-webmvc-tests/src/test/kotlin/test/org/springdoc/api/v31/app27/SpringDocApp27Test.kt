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

package test.org.springdoc.api.v31.app27

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

/**
 * A nullable Kotlin property is marked nullable even when its JSON name differs from
 * its Kotlin name.
 *
 * @see <a href="https://github.com/springdoc/springdoc-openapi/issues/3399">#3399</a>
 */
class SpringDocApp27Test : AbstractKotlinSpringDocMVCTest() {

	@SpringBootApplication
	@ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v31.app27"])
	class DemoApplication
}
