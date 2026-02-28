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

package test.org.springdoc.api.v30.app125;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Deprecated entity.
 *
 * @author bnasslahsen
 */
class DeprecatedEntity {
	/**
	 * The My non deprecated field.
	 */
	@Schema(deprecated = false)
	private String myNonDeprecatedField;

	/**
	 * The Mydeprecated field.
	 */
	@Schema(deprecated = true)
	private String mydeprecatedField;

	/**
	 * Gets my non deprecated field.
	 *
	 * @return the my non deprecated field
	 */
	public String getMyNonDeprecatedField() {
		return myNonDeprecatedField;
	}

	/**
	 * Sets my non deprecated field.
	 *
	 * @param myNonDeprecatedField the my non deprecated field
	 * @return the my non deprecated field
	 */
	@Deprecated
	public DeprecatedEntity setMyNonDeprecatedField(String myNonDeprecatedField) {
		this.myNonDeprecatedField = myNonDeprecatedField;
		return this;
	}

	/**
	 * Gets mydeprecated field.
	 *
	 * @return the mydeprecated field
	 */
	public String getMydeprecatedField() {
		return mydeprecatedField;
	}

	/**
	 * Sets mydeprecated field.
	 *
	 * @param mydeprecatedField the mydeprecated field
	 */
	public void setMydeprecatedField(String mydeprecatedField) {
		this.mydeprecatedField = mydeprecatedField;
	}
}