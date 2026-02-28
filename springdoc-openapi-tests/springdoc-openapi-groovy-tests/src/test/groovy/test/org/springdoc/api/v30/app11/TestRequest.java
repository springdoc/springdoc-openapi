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

package test.org.springdoc.api.v30.app11;

/**
 *
 */

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

public class TestRequest {
	@Schema(description = "Joe was here with a tuna melt!")
	private String joeWasHere;

	@Schema(description = "This is an example of a map that does not work.!")
	private Map<String, String> testingTheMap;

	public String getJoeWasHere() {
		return joeWasHere;
	}

	public void setJoeWasHere(String joeWasHere) {
		this.joeWasHere = joeWasHere;
	}

	public Map<String, String> getTestingTheMap() {
		return testingTheMap;
	}

	public void setTestingTheMap(Map<String, String> testingTheMap) {
		this.testingTheMap = testingTheMap;
	}
}