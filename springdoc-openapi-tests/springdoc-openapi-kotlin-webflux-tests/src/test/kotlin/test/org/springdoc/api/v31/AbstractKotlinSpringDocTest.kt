/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v31

import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.slf4j.LoggerFactory
import org.springdoc.core.utils.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@WebFluxTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "3600000")
abstract class AbstractKotlinSpringDocTest {

	@Autowired
	private val webTestClient: WebTestClient? = null

	private val logger = LoggerFactory.getLogger(AbstractKotlinSpringDocTest::class.java)

	@Test
	@Throws(Exception::class)
	fun testApp() {
		var result: String? = null
		try {
			val getResult =
				webTestClient!!.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
					.expectStatus().isOk.expectBody().returnResult()

			result = String(getResult.responseBody!!)
			val className = javaClass.simpleName
			val testNumber = className.replace("[^0-9]".toRegex(), "")

			val expected = getContent("results/3.1.0/app$testNumber.json")
			JSONAssert.assertEquals(expected, result, true)
		} catch (e: AssertionError) {
			logger.error(result)
			throw e
		}
	}

	companion object {
		@Throws(Exception::class)
		fun getContent(fileName: String): String {
			try {
				val path = Paths.get(
					AbstractKotlinSpringDocTest::class.java.classLoader.getResource(
						fileName
					)!!.toURI()
				)
				val fileBytes = Files.readAllBytes(path)
				return String(fileBytes, StandardCharsets.UTF_8)
			} catch (e: Exception) {
				throw RuntimeException("Failed to read file: $fileName", e)
			}

		}
	}
}