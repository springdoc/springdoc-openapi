package org.springdoc.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(MockitoExtension.class)
public class AbstractSwaggerIndexTransformerTest {

    private SwaggerUiConfigProperties swaggerUiConfig;
    @Mock
    private SwaggerUiOAuthProperties swaggerUiOAuthProperties;
    @Mock
    private SwaggerUiConfigParameters swaggerUiConfigParameters;
    @Mock
    private ObjectMapperProvider objectMapperProvider;

    private AbstractSwaggerIndexTransformer underTest;

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
    private final InputStream is = new ByteArrayInputStream(swaggerInitJs.getBytes());

    private final String apiDocUrl = "http://test.springdoc.com/apidoc";

    @BeforeEach
    public void setup(){
        swaggerUiConfig = new SwaggerUiConfigProperties();
        swaggerUiConfig.setUrl(apiDocUrl);
        underTest = new AbstractSwaggerIndexTransformer(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, objectMapperProvider);

    }

    @Test
    void setApiDocUrlCorrectly() throws IOException {
        var html = underTest.defaultTransformations(is);
        assertThat(html, containsString(apiDocUrl));
    }
}
