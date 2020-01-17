package org.springdoc.core;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Map;

public class SpringSecurityOAuth2Provider implements SecurityOAuth2Provider {

    private FrameworkEndpointHandlerMapping oauth2EndpointHandlerMapping;

    public SpringSecurityOAuth2Provider(FrameworkEndpointHandlerMapping oauth2EndpointHandlerMapping) {
        this.oauth2EndpointHandlerMapping = oauth2EndpointHandlerMapping;
    }

    public FrameworkEndpointHandlerMapping getOauth2EndpointHandlerMapping() {
        return oauth2EndpointHandlerMapping;
    }

    @Override
    public Map<RequestMappingInfo, HandlerMethod> getHandlerMethods() {
        return oauth2EndpointHandlerMapping.getHandlerMethods();
    }

    @Override
    public Map getFrameworkEndpoints(){
        return oauth2EndpointHandlerMapping.getApplicationContext().getBeansWithAnnotation(FrameworkEndpoint.class);
    }

}
