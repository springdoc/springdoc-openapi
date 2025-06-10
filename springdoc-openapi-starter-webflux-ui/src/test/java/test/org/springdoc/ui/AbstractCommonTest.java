package test.org.springdoc.ui;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "3600000")
@ActiveProfiles("test")
@TestPropertySource(properties = "springdoc.api-docs.version=openapi_3_0")
public abstract class AbstractCommonTest {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonTest.class);

	@Autowired
	protected WebTestClient webTestClient;

	protected String getContent(String fileName) {
		try {
			Path path = Paths.get(AbstractCommonTest.class.getClassLoader().getResource(fileName).toURI());
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read file: " + fileName, e);
		}
	}
}
