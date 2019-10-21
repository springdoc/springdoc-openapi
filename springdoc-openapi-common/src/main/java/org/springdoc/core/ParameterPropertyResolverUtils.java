package org.springdoc.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ParameterPropertyResolverUtils {

    @Autowired
    private Environment env;

    public String resolve(String parameterProperty) {
        if(parameterProperty.startsWith("${") && parameterProperty.endsWith("}")){
            String propertyName = parameterProperty.substring(2, parameterProperty.length() - 1);
            return env.containsProperty(propertyName) ? env.getProperty(propertyName) : parameterProperty;
        }
        return parameterProperty;
    }
}
