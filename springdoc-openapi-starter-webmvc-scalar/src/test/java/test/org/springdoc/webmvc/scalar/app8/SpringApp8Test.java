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

package test.org.springdoc.webmvc.scalar.app8;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
		"spring.mvc.pathmatch.matching-strategy=ant_path_matcher",
		"scalar.path="+ SpringApp8Test.SCALAR_PATH,
		"spring.mvc.servlet.path="+ SpringApp8Test.SERVLET_PATH
})
public class SpringApp8Test extends AbstractCommonTest {

	static final String CONTEXT_PATH = "/context-path";
	static final String SERVLET_PATH = "/servlet-path";
	static final String SCALAR_PATH = "/test/scalar";
	static final String REQUEST_PATH = CONTEXT_PATH + SERVLET_PATH + SCALAR_PATH;

	@Test
	void checkContent() throws Exception {
		checkContent(REQUEST_PATH, CONTEXT_PATH, SERVLET_PATH);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}