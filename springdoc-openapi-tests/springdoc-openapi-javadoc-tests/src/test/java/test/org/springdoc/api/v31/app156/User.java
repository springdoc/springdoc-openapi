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

package test.org.springdoc.api.v31.app156;

import java.util.Set;

/**
 * The type User.
 */
class User {
	/**
	 * The Some text.
	 */
	private String someText;

	/**
	 * The Text set.
	 */
	private Set<String> textSet;

	/**
	 * The Some enums.
	 */
	private Set<SomeEnum> someEnums;

	/**
	 * Gets some text.
	 *
	 * @return the some text
	 */
	public String getSomeText() {
		return someText;
	}

	/**
	 * Sets some text.
	 *
	 * @param someText the some text
	 */
	public void setSomeText(String someText) {
		this.someText = someText;
	}

	/**
	 * Gets text set.
	 *
	 * @return the text set
	 */
	public Set<String> getTextSet() {
		return textSet;
	}

	/**
	 * Sets text set.
	 *
	 * @param textSet the text set
	 */
	public void setTextSet(Set<String> textSet) {
		this.textSet = textSet;
	}

	/**
	 * Gets some enums.
	 *
	 * @return the some enums
	 */
	public Set<SomeEnum> getSomeEnums() {
		return someEnums;
	}

	/**
	 * Sets some enums.
	 *
	 * @param someEnums the some enums
	 */
	public void setSomeEnums(Set<SomeEnum> someEnums) {
		this.someEnums = someEnums;
	}
}
