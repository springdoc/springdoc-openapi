package test.org.springdoc.api.app70.customizer;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class PropertyCustomizer implements org.springdoc.core.customizers.PropertyCustomizer {
	@Override
	public Schema<?> customize(Schema<?> property, AnnotatedType type) {
		Annotation[] ctxAnnotations = type.getCtxAnnotations();
		if(ctxAnnotations == null) {
			return property;
		}

		Optional<CustomizedProperty> propertyAnnotation = Stream.of(ctxAnnotations)
				.filter(CustomizedProperty.class::isInstance)
				.findFirst()
				.map(CustomizedProperty.class::cast);

		propertyAnnotation
				.ifPresent(annotation -> property.description(property.getDescription() + ", " + annotation.addition()));

		return property;
	}
}
