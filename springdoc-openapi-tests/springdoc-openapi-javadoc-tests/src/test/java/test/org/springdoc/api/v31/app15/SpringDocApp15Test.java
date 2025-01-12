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

package test.org.springdoc.api.v31.app15;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

/**
 * The type Spring doc app 15 test.
 */
@TestPropertySource(properties = {
		"springdoc.operation-descriptions.myOperation=My Desc",
		"springdoc.openapidefinition.info.title=My title",
		"springdoc.openapidefinition.info.desc=My description",
		"springdoc.openapidefinition.info.version=My version",
		"springdoc.openapidefinition.info.terms=My terms",
		"springdoc.openapidefinition.info.license.name=My license name",
		"springdoc.openapidefinition.info.license.url=My license url",
		"springdoc.openapidefinition.info.contact.name=My contact name",
		"springdoc.openapidefinition.info.contact.email=My contact email",
		"springdoc.openapidefinition.info.contact.url=My contact url"
})
class SpringDocApp15Test extends AbstractSpringDocTest {

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	@OpenAPIDefinition(info = @Info(
			title = "${springdoc.openapidefinition.info.title}",
			description = "${springdoc.openapidefinition.info.desc}",
			version = "${springdoc.openapidefinition.info.version}",
			termsOfService = "${springdoc.openapidefinition.info.terms}",
			license = @License(
					name = "${springdoc.openapidefinition.info.license.name}",
					url = "${springdoc.openapidefinition.info.license.url}"
			),
			contact = @Contact(
					name = "${springdoc.openapidefinition.info.contact.name}",
					email = "${springdoc.openapidefinition.info.contact.email}",
					url = "${springdoc.openapidefinition.info.contact.url}"
			)
	))
	static class SpringDocTestApp {}
}
