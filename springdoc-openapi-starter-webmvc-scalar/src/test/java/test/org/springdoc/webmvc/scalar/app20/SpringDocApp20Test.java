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

package test.org.springdoc.webmvc.scalar.app20;

import com.scalar.maven.webmvc.ScalarWebMvcAutoConfiguration;
import com.scalar.maven.webmvc.ScalarWebMvcController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The Springdoc Scalar starter and Scalar's own integration both declare a bean named
 * {@code scalarWebMvcController} mapped to {@code ${scalar.path}}: the standalone one must not
 * survive next to the Springdoc controller, otherwise the application fails on a duplicated bean
 * definition (#3084). This is the counterpart of the standalone Scalar support restored by #3387.
 *
 * @author awinishere
 */
@SpringBootTest
class SpringDocApp20Test {

	@Autowired
	private ApplicationContext context;

	@Test
	void scalarStandaloneAutoConfigurationIsNotApplied() {

		assertThat(context.getBeanNamesForType(ScalarWebMvcAutoConfiguration.class)).isEmpty();
		assertThat(context.getBeanNamesForType(ScalarWebMvcController.class)).isEmpty();
	}

	@Test
	void springdocScalarControllerReplacesIt() {

		assertThat(context.getBean("scalarWebMvcController"))
				.isInstanceOf(org.springdoc.webmvc.scalar.ScalarWebMvcController.class);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
