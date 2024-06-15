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

package test.org.springdoc.api;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;


@WebFluxTest
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	@Test
	public void testApp() throws Exception {
		String className = getClass().getSimpleName();
		String testId = className.replaceAll("[^0-9]", "");
		testApp(testId, StringUtils.EMPTY);
	}

}
