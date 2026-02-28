/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.webflux.scalar.app9;


import org.junit.jupiter.api.Test;
import test.org.springdoc.webflux.scalar.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;
import static test.org.springdoc.webflux.scalar.app9.WebConfig.BASE_PATH;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "server.port=9520" })
class SpringDocApp9Test extends AbstractCommonTest {

	@Test
	void checkContent() {
		checkContent(BASE_PATH + SCALAR_DEFAULT_PATH);
	}
	
	@SpringBootApplication
	static class SpringDocTestApp {}

}