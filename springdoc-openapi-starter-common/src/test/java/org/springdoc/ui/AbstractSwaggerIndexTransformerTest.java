package org.springdoc.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@ExtendWith(MockitoExtension.class)
class AbstractSwaggerIndexTransformerTest {

	private final String swaggerInitJs = "window.onload = function() {\n" +
			"\n" +
			"  window.ui = SwaggerUIBundle({\n" +
			"    url: \"https://petstore.swagger.io/v2/swagger.json\",\n" +
			"    dom_id: '#swagger-ui',\n" +
			"    deepLinking: true,\n" +
			"    presets: [\n" +
			"      SwaggerUIBundle.presets.apis,\n" +
			"      SwaggerUIStandalonePreset\n" +
			"    ],\n" +
			"    plugins: [\n" +
			"      SwaggerUIBundle.plugins.DownloadUrl\n" +
			"    ],\n" +
			"    layout: \"StandaloneLayout\"\n" +
			"  });\n" +
			"\n" +
			"  //</editor-fold>\n" +
			"};";

	private final InputStream is = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));

	private final String apiDocUrl = "http://test.springdoc.com/apidoc";

	private SwaggerUiConfigProperties swaggerUiConfig;

	@Mock
	private SwaggerUiOAuthProperties swaggerUiOAuthProperties;

	private ObjectMapperProvider objectMapperProvider;

	private AbstractSwaggerIndexTransformer underTest;

	@BeforeEach
	public void setup() {
		swaggerUiConfig = new SwaggerUiConfigProperties();
		swaggerUiConfig.setUrl(apiDocUrl);
		objectMapperProvider = new ObjectMapperProvider(new SpringDocConfigProperties());
		underTest = new AbstractSwaggerIndexTransformer(swaggerUiConfig, swaggerUiOAuthProperties, objectMapperProvider);
	}

	@Test
	void setApiDocUrlCorrectly() throws IOException {
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), is);
		assertThat(html, containsString(apiDocUrl));
	}

	@Test
	void documentTitle_whenSet_addsDocumentTitleScript() throws IOException {
		swaggerUiConfig.setDocumentTitle("My Custom API Documentation");
		InputStream inputStream = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, containsString("document.title = 'My Custom API Documentation';"));
	}

	@Test
	void documentTitle_whenNotSet_doesNotAddScript() throws IOException {
		swaggerUiConfig.setDocumentTitle(null);
		InputStream inputStream = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, not(containsString("document.title")));
	}

	@Test
	void documentTitle_whenEmpty_doesNotAddScript() throws IOException {
		swaggerUiConfig.setDocumentTitle("");
		InputStream inputStream = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, not(containsString("document.title")));
	}

	@Test
	void documentTitle_escapesSpecialCharacters() throws IOException {
		swaggerUiConfig.setDocumentTitle("Test's API \\ Documentation");
		InputStream inputStream = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, containsString("document.title = 'Test\\'s API \\\\ Documentation';"));
	}

	@Test
	void documentTitle_escapesNewlines() throws IOException {
		swaggerUiConfig.setDocumentTitle("Test\nAPI\rDocs\tTitle");
		InputStream inputStream = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, containsString("document.title = 'Test\\nAPI\\rDocs\\tTitle';"));
	}

	@Test
	void documentTitle_escapesScriptTags() throws IOException {
		swaggerUiConfig.setDocumentTitle("</script><script>alert('xss')</script>");
		InputStream inputStream = new ByteArrayInputStream(swaggerInitJs.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, not(containsString("</script><script>")));
		assertThat(html, containsString("\\u003c/script\\u003e\\u003cscript\\u003ealert"));
	}

	@Test
	void documentTitle_whenMarkerMissing_returnsOriginalHtml() throws IOException {
		String htmlWithoutMarker = "window.onload = function() { window.ui = SwaggerUIBundle({}); };";
		swaggerUiConfig.setDocumentTitle("My Title");
		swaggerUiConfig.setUrl(null);
		InputStream inputStream = new ByteArrayInputStream(htmlWithoutMarker.getBytes(StandardCharsets.UTF_8));
		var html = underTest.defaultTransformations(new SwaggerUiConfigParameters(swaggerUiConfig), inputStream);
		assertThat(html, not(containsString("document.title")));
	}
}
