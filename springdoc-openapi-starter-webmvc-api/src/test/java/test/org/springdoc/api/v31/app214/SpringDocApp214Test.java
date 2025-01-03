/*
 *
 *  * Copyright 2019-2023 the original author or authors.
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

package test.org.springdoc.api.v31.app214;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.SpecPropertiesCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig;
import test.org.springdoc.api.AbstractCommonTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * A test for {@link SpecPropertiesCustomizer}
 */
@SpringBootTest
@TestPropertySource(properties = { "springdoc.group-configs[0].group=Group0",
		"springdoc.group-configs[0].packages-to-scan=com.my-package",
		"springdoc.group-configs[1].group=Group1",
		"springdoc.group-configs[1].packages-to-scan=com.my-package",
		"springdoc.group-configs[2].group=Group2",
		"springdoc.group-configs[2].packages-to-scan=com.my-package",
		"springdoc.group-configs[3].group=Group3",
		"springdoc.group-configs[3].packages-to-scan=com.my-package" })

public class SpringDocApp214Test extends AbstractCommonTest {

	@Autowired
	private SpringDocConfigProperties springDocConfigProperties;
	
	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	protected void testApp() throws Exception {
		assertEquals(4, springDocConfigProperties.getGroupConfigs().stream().map(GroupConfig::getGroup).toList().size());
	}

}