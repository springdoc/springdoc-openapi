/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.webmvc.scalar.app5;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractCommonTest;

import org.springframework.test.context.TestPropertySource;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;
import static test.org.springdoc.webmvc.scalar.app5.SpringDocApp5Test.API_DOCS_URL;

@TestPropertySource(properties = "scalar.url=" + API_DOCS_URL)
public class SpringDocApp5Test extends AbstractCommonTest {
	
	static final String API_DOCS_URL = "http://myserver:8080/v3/api-docs";

	@Test
	void checkContent() throws Exception {
		checkContent(SCALAR_DEFAULT_PATH);
		checkContent(SCALAR_DEFAULT_PATH);
	}
}