package org.springdoc.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public abstract class SpringDocPropertiesUtils {

    private SpringDocPropertiesUtils() { }

    public static void put(String name, List<String> value, Map<String, Object> params) {
        if (!CollectionUtils.isEmpty(value)) {
            params.put(name, value);
        }
    }

    public static void put(final String name, final Integer value, final Map<String, Object> params) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    public static void put(final String name, final Boolean value, final Map<String, Object> params) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    public static void put(final String name, final String value, final Map<String, Object> params) {
        if (!StringUtils.isEmpty(value)) {
            params.put(name, value);
        }
    }

}
