/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v30.app183;

import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

public class SpringDocApp183Test extends AbstractSpringDocV30Test {

	@SpringBootApplication
	static class SpringDocTestApp {

		record ObjectA(String aa, String aaa) {}

		record ObjectB(Integer bb, Integer bbb) {}

		@Component
		class BToAConvertor implements Converter<ObjectB, ObjectA> {
			@Override
			public ObjectA convert(ObjectB source) {
				return new ObjectA(source.bb+"", source.bbb+"");
			}
		}

		@RestController
		class Controller {
			@PostMapping("/test")
			public String test(@RequestBody ObjectA request) {
				return "OK!";
			}
		}
	}

}
