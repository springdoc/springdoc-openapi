package test.org.springdoc.api.v31

import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class AbstractKotlinDataRestTest {

	@Autowired
	val mockMvc: MockMvc? = null

	private val logger = LoggerFactory.getLogger(AbstractKotlinDataRestTest::class.java)

	@Test
	fun testApp() {
		var result: String? = null
		try {
			val response = mockMvc!!.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
				.andExpect(MockMvcResultMatchers.status().isOk).andReturn()

			result = response.response.contentAsString
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
		fun getContent(fileName: String): String {
			val path = Paths.get(
				AbstractKotlinDataRestTest::class.java.classLoader.getResource(fileName)!!.toURI()
			)
			return String(Files.readAllBytes(path), StandardCharsets.UTF_8)
		}
	}
}
