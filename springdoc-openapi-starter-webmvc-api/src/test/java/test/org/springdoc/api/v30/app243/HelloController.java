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

package test.org.springdoc.api.v30.app243;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PostMapping("/parent")
	public void parentEndpoint(@RequestBody SuperClass parent) {

	}

}

@Schema(name = SuperClass.SCHEMA_NAME,
		discriminatorProperty = "type",
		oneOf = {
				FirstChildClass.class,
				SecondChildClass.class
		},
		discriminatorMapping = {
				@DiscriminatorMapping(value = FirstChildClass.SCHEMA_NAME, schema = FirstChildClass.class),
				@DiscriminatorMapping(value = SecondChildClass.SCHEMA_NAME, schema = SecondChildClass.class)
		}
)
sealed class SuperClass {

	@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
	public String getType() {
		return type;
	}

	public String type;

	public static final String SCHEMA_NAME = "SuperClass";
}

@Schema(name = FirstChildClass.SCHEMA_NAME)
final class FirstChildClass extends SuperClass {

	@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
	public String getType() {
		return type;
	}

	public String type;

	public static final String SCHEMA_NAME = "Image";
}

@Schema(name = SecondChildClass.SCHEMA_NAME)
final class SecondChildClass extends SuperClass {

	@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
	public String getType() {
		return type;
	}

	public String type;

	public static final String SCHEMA_NAME = "Mail";
}
