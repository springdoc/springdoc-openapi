package test.org.springdoc.api.app3;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebFluxTest
@ActiveProfiles("test")
public class SpringDocApp3Test {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testApp3() throws Exception {
		EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
				.expectStatus()
				.isOk().expectBody().returnResult();

		String result = new String(getResult.getResponseBody());

		Path path = Paths.get(getClass().getClassLoader().getResource("results/app3.json").toURI());
		byte[] fileBytes = Files.readAllBytes(path);
		String expected = new String(fileBytes);

		assertEquals(objectMapper.readTree(expected), objectMapper.readTree(result));
	}

}
