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

package test.org.springdoc.api.v30.app174;

import java.io.Serializable;
import java.util.List;

/**
 * The type Test.
 *
 * @author bnasslahsen
 */

class Test implements Serializable {

	private static final long serialVersionUID = 1L;  // Recommended for Serializable classes

	private List<Question> questions;

	// No-argument constructor
	public Test() {
	}

	// Constructor with arguments
	public Test(List<Question> questions) {
		this.questions = questions;
	}

	// Getter method for 'questions'
	public List<Question> getQuestions() {
		return questions;
	}

	// Setter method for 'questions'
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	// Optionally, you can override toString, hashCode, equals, etc.
	@Override
	public String toString() {
		return "Test{" +
				"questions=" + questions +
				'}';
	}
}