package test.org.springdoc.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springdoc.core.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.file.Files
import java.nio.file.Paths

@RunWith(SpringRunner::class)
@WebFluxTest
@ActiveProfiles("test")
abstract class AbstractKotlinSpringDocTest {

    @Autowired
    private val webTestClient: WebTestClient? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @Test
    @Throws(Exception::class)
    fun testApp() {
        val getResult = webTestClient!!.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
                .expectStatus().isOk.expectBody().returnResult()

        val result = String(getResult.responseBody!!)
        val className = javaClass.simpleName
        val testNumber = className.replace("[^0-9]".toRegex(), "")

        val path = Paths.get(javaClass.classLoader.getResource("results/app$testNumber.json")!!.toURI())
        val fileBytes = Files.readAllBytes(path)
        val expected = String(fileBytes)

        assertEquals(objectMapper!!.readTree(expected), objectMapper.readTree(result))
    }

}
