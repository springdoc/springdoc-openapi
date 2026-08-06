package org.springdoc.core.utils;

import io.swagger.v3.oas.models.media.JsonSchema;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpringDocUtilsTest {

    @Test
    void singleTypeForJsonSchemaJsonCloning() {
        SpringDocConfigProperties props = new SpringDocConfigProperties();
        props.getApiDocs().setVersion(SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_1);
        ObjectMapperProvider provider = new ObjectMapperProvider(props);

        JsonSchema jsonSchema = new JsonSchema();
        jsonSchema.setTypes(Set.of("integer"));

        JsonSchema cloned = SpringDocUtils.cloneViaJson(jsonSchema, JsonSchema.class, provider.jsonMapper());

        // The object is cloned properly, we do not get the type cast fallback
        assertNotSame(jsonSchema, cloned);
        assertNotNull(cloned);
        assertEquals(Set.of("integer"), cloned.getTypes());
    }

    @Test
    void nullTypeBecomesNullTypesForJsonSchemaJsonCloning() {
        SpringDocConfigProperties props = new SpringDocConfigProperties();
        props.getApiDocs().setVersion(SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_1);
        ObjectMapperProvider provider = new ObjectMapperProvider(props);

        JsonSchema jsonSchema = new JsonSchema();

        JsonSchema cloned = SpringDocUtils.cloneViaJson(jsonSchema, JsonSchema.class, provider.jsonMapper());

        // The object is cloned properly, we do not get the type cast fallback
        assertNotSame(jsonSchema, cloned);
        assertNotNull(cloned);
        assertNull(cloned.getTypes());
    }

    @Test
    void typeArrayIsRetainedForJsonSchemaJsonCloning() {
        SpringDocConfigProperties props = new SpringDocConfigProperties();
        props.getApiDocs().setVersion(SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_1);
        ObjectMapperProvider provider = new ObjectMapperProvider(props);

        JsonSchema jsonSchema = new JsonSchema();
        jsonSchema.setTypes(Set.of("integer", "null"));

        JsonSchema cloned = SpringDocUtils.cloneViaJson(jsonSchema, JsonSchema.class, provider.jsonMapper());

        // The object is cloned properly, we do not get the type cast fallback
        assertNotSame(jsonSchema, cloned);
        assertNotNull(cloned);
        assertEquals(Set.of("integer", "null"), cloned.getTypes());
    }

}