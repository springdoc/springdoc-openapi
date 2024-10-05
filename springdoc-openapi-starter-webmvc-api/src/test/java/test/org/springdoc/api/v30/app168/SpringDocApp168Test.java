/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app168;

import java.util.Optional;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import org.springdoc.core.converters.PolymorphicModelConverter;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(properties = Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ENABLED + "=false")
public class SpringDocApp168Test extends AbstractSpringDocV30Test {

	static {
		Optional<ModelConverter> modelConverterOptional =
				ModelConverters.getInstance().getConverters()
						.stream().filter(modelConverter -> modelConverter instanceof PolymorphicModelConverter).findAny();
		modelConverterOptional.ifPresent(ModelConverters.getInstance()::removeConverter);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
