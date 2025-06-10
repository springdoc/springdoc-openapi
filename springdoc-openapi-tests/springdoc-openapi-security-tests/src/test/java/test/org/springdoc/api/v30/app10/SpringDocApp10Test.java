/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app10;

import java.util.Optional;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springdoc.core.converters.SchemaPropertyDeprecatingConverter;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = { "springdoc.show-oauth2-endpoints=true", "springdoc.model-converters.deprecating-converter.enabled=false" })
public class SpringDocApp10Test extends AbstractSpringDocTest {

	@BeforeAll
	public static void init() {
		Optional<ModelConverter> deprecatingConverterOptional =
				ModelConverters.getInstance().getConverters()
						.stream().filter(modelConverter -> modelConverter instanceof SchemaPropertyDeprecatingConverter).findAny();
		deprecatingConverterOptional.ifPresent(ModelConverters.getInstance()::removeConverter);
	}

	@AfterAll
	public static void clean() {
		Optional<ModelConverter> deprecatingConverterOptional =
				ModelConverters.getInstance().getConverters()
						.stream().filter(modelConverter -> modelConverter instanceof SchemaPropertyDeprecatingConverter).findAny();
		deprecatingConverterOptional.ifPresent(ModelConverters.getInstance()::addConverter);
	}

	@SpringBootApplication
	static class SpringDocTestApp {
	}

}
