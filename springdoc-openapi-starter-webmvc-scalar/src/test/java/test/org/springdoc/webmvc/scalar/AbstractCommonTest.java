package test.org.springdoc.webmvc.scalar;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springdoc.scalar.ScalarConstants.SCALAR_JS_FILENAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public abstract class AbstractCommonTest {

	@Autowired
	protected MockMvc mockMvc;

	protected String className = getClass().getSimpleName();

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


	protected void checkContent(String requestPath) throws Exception {
		String expected = getResultFile();
		String scalarJsPath = getScalarJsPath(requestPath);

		mockMvc.perform(get(requestPath))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE))
				.andExpect(content().string(containsString(scalarJsPath)))
				.andExpect(content().string(equalTo(expected)));
		mockMvc.perform(get(scalarJsPath))
				.andExpect(status().isOk());
	}

	protected void checkContent(String requestPath, Map<String, String> headers, String resultFile) throws Exception {
		String expected = getContent(resultFile);
		String scalarJsPath = getScalarJsPath(requestPath);

		// build the request with headers
		MockHttpServletRequestBuilder requestBuilder = get(requestPath);
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				requestBuilder.header(entry.getKey(), entry.getValue());
			}
		}

		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE))
				.andExpect(content().string(containsString(scalarJsPath)))
				.andExpect(content().string(equalTo(expected)));

		// build the scalar.js request with headers
		MockHttpServletRequestBuilder scalarRequestBuilder = get(scalarJsPath);
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				scalarRequestBuilder.header(entry.getKey(), entry.getValue());
			}
		}

		mockMvc.perform(scalarRequestBuilder)
				.andExpect(status().isOk());
	}

	
	protected void checkContent(String requestPath, String contextPath) throws Exception {
		String expected = getResultFile();
		String scalarJsPath = getScalarJsPath(requestPath);

		mockMvc.perform(get(requestPath).contextPath(contextPath))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE))
				.andExpect(content().string(containsString(scalarJsPath)))
				.andExpect(content().string(equalTo(expected)));
		mockMvc.perform(get(scalarJsPath).contextPath(contextPath))
				.andExpect(status().isOk());
	}
	
	protected void checkContent(String requestPath, String contextPath, String servletPath) throws Exception {
		String expected = getResultFile();
		String scalarJsPath = getScalarJsPath(requestPath);
		contextPath = sanitizePath(contextPath);
		servletPath = sanitizePath(servletPath);
		
		mockMvc.perform(get(requestPath).contextPath(contextPath).servletPath(servletPath))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE))
				.andExpect(content().string(containsString(scalarJsPath)))
				.andExpect(content().string(equalTo(expected)));
		mockMvc.perform(get(scalarJsPath).contextPath(contextPath).servletPath(servletPath))
				.andExpect(status().isOk());
	}

	protected String getResultFile() {
		String testNumber = className.replaceAll("[^0-9]", "");
		return getContent("results/app" + testNumber);
	}

	private String sanitizePath(String path) {
		if (path == null) {
			return null;
		}
		return path.endsWith("/")
				? path.substring(0, path.length() - 1)
				: path;
	}

	protected static String getScalarJsPath(String requestPath) {
		String scalarJsPath;
		if(!requestPath.endsWith(DEFAULT_PATH_SEPARATOR))
			scalarJsPath = requestPath + DEFAULT_PATH_SEPARATOR + SCALAR_JS_FILENAME;
		else
			scalarJsPath =  requestPath + SCALAR_JS_FILENAME;
		return scalarJsPath;
	}
}
