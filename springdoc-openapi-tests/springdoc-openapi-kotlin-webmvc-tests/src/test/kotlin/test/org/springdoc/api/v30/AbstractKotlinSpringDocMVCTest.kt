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

package test.org.springdoc.api.v30

import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = ["springdoc.api-docs.version=openapi_3_0"])
abstract class AbstractKotlinSpringDocMVCTest {

	@Autowired
	val mockMvc: MockMvc? = null

	private val logger =
		LoggerFactory.getLogger(AbstractKotlinSpringDocMVCTest::class.java)

	@Test
	fun testApp() {
		var result: String? = null
		try {
			val response = mockMvc!!.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
				.andExpect(MockMvcResultMatchers.status().isOk).andReturn()

			result = response.response.contentAsString
			val className = javaClass.simpleName
			val testNumber = className.replace("[^0-9]".toRegex(), "")

			val expected = getContent("results/3.0.1/app$testNumber.json")
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
					AbstractKotlinSpringDocMVCTest::class.java.classLoader.getResource(
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
