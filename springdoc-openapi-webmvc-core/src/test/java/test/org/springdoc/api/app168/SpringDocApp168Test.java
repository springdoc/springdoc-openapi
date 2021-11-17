package test.org.springdoc.api.app168;

import java.util.Optional;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import org.springdoc.core.Constants;
import org.springdoc.core.converters.PolymorphicModelConverter;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(properties = Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ENABLED + "=false")
public class SpringDocApp168Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

	static {
		Optional<ModelConverter> modelConverterOptional =
				ModelConverters.getInstance().getConverters()
						.stream().filter(modelConverter -> modelConverter instanceof PolymorphicModelConverter).findAny();
		modelConverterOptional.ifPresent(ModelConverters.getInstance()::removeConverter);
	}

}
