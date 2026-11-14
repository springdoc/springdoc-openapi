/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v30.app285;

import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Jackson 3 {@code ArrayNode} and {@code StringNode} are documented as an array and a
 * string, like {@code ObjectNode} is documented as an object.
 *
 * @see <a href="https://github.com/springdoc/springdoc-openapi/issues/3431">#3431</a>
 */
public class SpringDocApp285Test extends AbstractSpringDocV30Test {

	@SpringBootApplication
	static class SpringDocTestApp {

	}

}
