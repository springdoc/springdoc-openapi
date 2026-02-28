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

package test.org.springdoc.api.v30.app174;

import java.util.List;

/**
 * The type Test question.
 *
 * @author bnasslahsen  test question
 */
class TestQuestion extends Question {
	/**
	 * list of variants
	 */
	private final List<String> variants;

	/**
	 * correct answer
	 */
	private final int answer;

	public TestQuestion(String question, String type, List<String> variants, int answer) {
		super(question, type);
		this.variants = variants;
		this.answer = answer;
	}

	public List<String> getVariants() {
		return variants;
	}

	public int getAnswer() {
		return answer;
	}

	@Override
	public String toString() {
		return "TestQuestion{" +
				"question='" + getQuestion() + '\'' +
				", variants=" + variants +
				", answer=" + answer +
				'}';
	}
}
