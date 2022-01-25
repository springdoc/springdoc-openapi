/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.app2.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Body 1.
 */
public class Body1 {

	/**
	 * Additional data to pass to server
	 */
	private String additionalMetadata = null;

	/**
	 * file to upload
	 */
	private File file = null;

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 * @param o the o
	 * @return the string
	 */
	private static String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	/**
	 * Additional data to pass to server
	 *
	 * @return additionalMetadata additional metadata
	 */
	@JsonProperty("additionalMetadata")
	public String getAdditionalMetadata() {
		return additionalMetadata;
	}

	/**
	 * Sets additional metadata.
	 *
	 * @param additionalMetadata the additional metadata
	 */
	public void setAdditionalMetadata(String additionalMetadata) {
		this.additionalMetadata = additionalMetadata;
	}

	/**
	 * Additional metadata body 1.
	 *
	 * @param additionalMetadata the additional metadata
	 * @return the body 1
	 */
	public Body1 additionalMetadata(String additionalMetadata) {
		this.additionalMetadata = additionalMetadata;
		return this;
	}

	/**
	 * file to upload
	 *
	 * @return file file
	 */
	@JsonProperty("file")
	public File getFile() {
		return file;
	}

	/**
	 * Sets file.
	 *
	 * @param file the file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * File body 1.
	 *
	 * @param file the file
	 * @return the body 1
	 */
	public Body1 file(File file) {
		this.file = file;
		return this;
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Body1 {\n");

		sb.append("    additionalMetadata: ").append(toIndentedString(additionalMetadata)).append("\n");
		sb.append("    file: ").append(toIndentedString(file)).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
