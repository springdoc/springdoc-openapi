package test.org.springdoc.api.app157;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

public class StringyConverter implements ModelConverter {

  @Override
  public Schema resolve(AnnotatedType type, ModelConverterContext context,
    Iterator<ModelConverter> chain) {

    JavaType javaType = Json.mapper().constructType(type.getType());

    if (javaType.getRawClass().equals(String.class)) {
      type.getParent().addRequiredItem("stringy");
    }
    return chain.next().resolve(type, context, chain);
  }
}
