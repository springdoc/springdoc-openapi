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

package test.org.springdoc.api.v30.app105.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class Body1 {

	@Schema(description = "Additional data to pass to server")
	/**
	 * Additional data to pass to server
	 **/
	private String additionalMetadata = null;

	@Schema(description = "file to upload")
	/**
	 * file to upload
	 **/
	private File file = null;

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
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
	 * @return additionalMetadata
	 **/
	@JsonProperty("additionalMetadata")
	public String getAdditionalMetadata() {
		return additionalMetadata;
	}

	public void setAdditionalMetadata(String additionalMetadata) {
		this.additionalMetadata = additionalMetadata;
	}

	public Body1 additionalMetadata(String additionalMetadata) {
		this.additionalMetadata = additionalMetadata;
		return this;
	}

	/**
	 * file to upload
	 *
	 * @return file
	 **/
	@JsonProperty("file")
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Body1 file(File file) {
		this.file = file;
		return this;
	}

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
