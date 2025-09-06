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

package test.org.springdoc.webmvc.scalar.app17;

import java.util.Map;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;

@TestPropertySource(properties = {
		"server.forward-headers-strategy=framework",
		"scalar.path="+ SpringDocApp17Test.SCALAR_PATH

})
public class SpringDocApp17Test extends AbstractCommonTest {

	static final String PREFIX = "/documentation";
	static final String X_FORWARD_PREFIX = "/path/prefix";
	static final String SCALAR_PATH = PREFIX + SCALAR_DEFAULT_PATH;
	
	@Test
	void checkContent() throws Exception {
		checkContent(SCALAR_PATH);
	}

	@Test
	void checkContentForwardedPrefix() throws Exception {
		Map<String, String> headers = Map.of("X-Forwarded-Prefix", X_FORWARD_PREFIX);
		checkContent(SCALAR_PATH, headers,"results/app17-1" );
	}

	@Test
	void checkContentForwardedPrefixHostProto() throws Exception {
		Map<String, String> headers = Map.of("X-Forwarded-Prefix", X_FORWARD_PREFIX,
				"X-Forwarded-Proto", "https", "X-Forwarded-Host", "proxy-host" );
		checkContent(SCALAR_PATH, headers,"results/app17-2" );
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
