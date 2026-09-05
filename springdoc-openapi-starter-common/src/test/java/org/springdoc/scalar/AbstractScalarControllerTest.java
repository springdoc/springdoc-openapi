/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package org.springdoc.scalar;

import java.io.IOException;
import java.util.Set;

import com.scalar.maven.core.ScalarProperties;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig;

import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springdoc.scalar.ScalarConstants.SCALAR_DEFAULT_PATH;

/**
 * The Scalar properties bean is a singleton shared by every request, while the api-docs url
 * and the group sources it is rendered with are request-specific. Rendering must therefore
 * leave the shared bean untouched, otherwise concurrent requests interleave their state.
 *
 * @author bnasslahsen
 */
class AbstractScalarControllerTest {

	/**
	 * Minimal concrete controller exposing the protected rendering entry point.
	 */
	private static class TestScalarController extends AbstractScalarController {

		TestScalarController(ScalarProperties scalarProperties, SpringDocConfigProperties springDocConfigProperties) {
			super(scalarProperties, springDocConfigProperties);
		}

		ResponseEntity<String> render(String requestUrl) throws IOException {
			return getDocs(requestUrl, "/v3/api-docs", SCALAR_DEFAULT_PATH);
		}

	}

	/**
	 * Builds config properties declaring a single group, so that the sources branch is taken.
	 *
	 * @return the spring doc config properties
	 */
	private SpringDocConfigProperties groupedConfigProperties() {
		SpringDocConfigProperties springDocConfigProperties = new SpringDocConfigProperties();
		GroupConfig groupConfig = new GroupConfig();
		groupConfig.setGroup("stores");
		springDocConfigProperties.setGroupConfigs(Set.of(groupConfig));
		return springDocConfigProperties;
	}

	@Test
	void testRenderingDoesNotMutateTheSharedProperties() throws IOException {
		ScalarProperties scalarProperties = new ScalarProperties();
		scalarProperties.setPath(SCALAR_DEFAULT_PATH);
		String originalUrl = scalarProperties.getUrl();
		TestScalarController controller = new TestScalarController(scalarProperties, groupedConfigProperties());

		controller.render("http://first-host:8080" + SCALAR_DEFAULT_PATH);

		assertThat(scalarProperties.getUrl()).isEqualTo(originalUrl);
		assertThat(scalarProperties.getSources()).isNull();
	}

	@Test
	void testEachRequestIsRenderedWithItsOwnUrl() throws IOException {
		ScalarProperties scalarProperties = new ScalarProperties();
		scalarProperties.setPath(SCALAR_DEFAULT_PATH);
		TestScalarController controller = new TestScalarController(scalarProperties, groupedConfigProperties());

		String first = controller.render("http://first-host:8080" + SCALAR_DEFAULT_PATH).getBody();
		String second = controller.render("http://second-host:9090" + SCALAR_DEFAULT_PATH).getBody();

		assertThat(first).contains("http://first-host:8080/v3/api-docs/stores").doesNotContain("second-host");
		assertThat(second).contains("http://second-host:9090/v3/api-docs/stores").doesNotContain("first-host");
	}

}
