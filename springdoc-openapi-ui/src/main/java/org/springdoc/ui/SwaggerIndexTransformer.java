package org.springdoc.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SwaggerIndexTransformer implements ResourceTransformer {

    private SwaggerUiOAuthProperties swaggerUiOAuthProperties;
    private ObjectMapper objectMapper;

    public SwaggerIndexTransformer(SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
        this.swaggerUiOAuthProperties = swaggerUiOAuthProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public Resource transform(HttpServletRequest request, Resource resource,
                              ResourceTransformerChain transformerChain) throws IOException {
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isIndexFound = antPathMatcher.match("**/swagger-ui/**/index.html", resource.getURL().toString());
        if (isIndexFound && !CollectionUtils.isEmpty(swaggerUiOAuthProperties.getConfigParameters())) {
            String html = readFullyAsString(resource.getInputStream());
            html = addInitOauth(html);
            return new TransformedResource(resource, html.getBytes());
        } else {
            return resource;
        }
    }

    private String addInitOauth(String html) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder("window.ui = ui\n");
        stringBuilder.append("ui.initOAuth(\n");
        String json = objectMapper.writeValueAsString(swaggerUiOAuthProperties.getConfigParameters());
        stringBuilder.append(json);
        stringBuilder.append(")");
        String result = html.replace("window.ui = ui", stringBuilder.toString());
        return result;
    }

    private String readFullyAsString(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString(StandardCharsets.UTF_8.name());
    }
}