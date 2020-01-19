package org.springdoc.core;

import java.util.Map;

public interface SecurityOAuth2Provider {

     Map getHandlerMethods();

     Map<String, Object> getFrameworkEndpoints();
}
