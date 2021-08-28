package test.org.springdoc.api.app157;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;

/**
 * The type Stringy converter.
 */
public class StringyConverter implements ModelConverter {

	/**
	 * Resolve schema.
	 *
	 * @param type the type
	 * @param context the context
	 * @param chain the chain
	 * @return the schema
	 */
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
