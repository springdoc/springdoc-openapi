package test.org.springdoc.webflux.scalar;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springdoc.scalar.ScalarConstants.SCALAR_JS_FILENAME;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@AutoConfigureWebTestClient(timeout = "3600000")
@ActiveProfiles("test")
@SpringBootTest
public abstract class AbstractCommonTest {

	@Autowired
	protected WebTestClient webTestClient;

	protected String className = getClass().getSimpleName();
	
	@PostConstruct
	public void init() {
		this.webTestClient = webTestClient.mutate()
				.baseUrl("http://localhost")
				.build();
	}
	
	protected void checkContent(String requestPath, Map<String, String> headers, String resultFile) {
		String expected     = getContent(resultFile);
		String scalarJsPath = getScalarJsPath(requestPath);
		webTestClient.get()
				.uri(requestPath)
				.headers(applyHeaders(headers))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().value(HttpHeaders.CONTENT_TYPE, startsWith(MediaType.TEXT_HTML_VALUE))
				.expectBody(String.class)
				.value(containsString(scalarJsPath))
				.value(equalTo(expected));
		webTestClient.get()
				.uri(scalarJsPath)
				.headers(applyHeaders(headers))
				.exchange()
				.expectStatus().isOk();
	}
	
	protected void checkContent(String requestPath)  {
		String expected = getResultFile();
		String scalarJsPath = getScalarJsPath(requestPath);
		webTestClient.get().uri(requestPath)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().value(HttpHeaders.CONTENT_TYPE, startsWith(MediaType.TEXT_HTML_VALUE))
				.expectBody(String.class)
				.value(containsString(scalarJsPath))
				.value(equalTo(expected));
		webTestClient.get().uri(scalarJsPath)
				.exchange()
				.expectStatus().isOk();
	}

	protected String getResultFile() {
		String testNumber = className.replaceAll("[^0-9]", "");
		return getContent("results/app" + testNumber);
	}

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

	protected static String getScalarJsPath(String requestPath) {
		String scalarJsPath;
		if(!requestPath.endsWith(DEFAULT_PATH_SEPARATOR))
			scalarJsPath = requestPath + DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME;
		else
			scalarJsPath =  requestPath + SCALAR_JS_FILENAME;
		return scalarJsPath;
	}

	private Consumer<HttpHeaders> applyHeaders(Map<String, String> headers) {
		return httpHeaders -> {
			if (headers != null) {
				headers.forEach(httpHeaders::set);
			}
		};
	}
}
