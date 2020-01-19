package org.springdoc.api;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.Constants;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Map;


public class ActuatorProvider {

    private final WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

    public ActuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
        this.webMvcEndpointHandlerMapping = webMvcEndpointHandlerMapping;
    }

    public Map<RequestMappingInfo, HandlerMethod> getMethods() {
        return  webMvcEndpointHandlerMapping.getHandlerMethods();
    }

    public Tag getTag() {
        Tag actuatorTag = new Tag();
        actuatorTag.setName(Constants.SPRINGDOC_ACTUATOR_TAG);
        actuatorTag.setDescription(Constants.SPRINGDOC_ACTUATOR_DESCRIPTION);
        actuatorTag.setExternalDocs(
                new ExternalDocumentation()
                        .url(Constants.SPRINGDOC_ACTUATOR_DOC_URL)
                        .description(Constants.SPRINGDOC_ACTUATOR_DOC_DESCRIPTION)
        );
        return actuatorTag;
    }

    public boolean isRestController(String operationPath) {
        return operationPath.startsWith(AntPathMatcher.DEFAULT_PATH_SEPARATOR);
    }

}
