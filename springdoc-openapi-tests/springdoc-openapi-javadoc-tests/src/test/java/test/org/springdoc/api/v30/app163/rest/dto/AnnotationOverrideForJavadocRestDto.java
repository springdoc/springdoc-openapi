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

package test.org.springdoc.api.v30.app163.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This is the {@code AnnotationOverrideForJavadocRestDto} class javadoc.
 */
@Schema(
		title = "annotation-override-dto",
		description = "Description for the tag."
)
public class AnnotationOverrideForJavadocRestDto {
	/**
	 * This is the private {@code #guid} field's javadoc.
	 */
	@Schema(description = "Description for the #guid field")
	private String guid;

	/**
	 * This is the private {@code #inner} field's javadoc.
	 * <p>
	 * This javadoc description is ignored by the REST documentation:
	 * the {@code $ref} can't have a description as any sibling elements of a $ref are ignored.
	 */
	private AnnotationOverrideForJavadocStaticInnerRestDto inner;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public AnnotationOverrideForJavadocStaticInnerRestDto getInner() {
		return inner;
	}

	public void setInner(AnnotationOverrideForJavadocStaticInnerRestDto inner) {
		this.inner = inner;
	}

	/**
	 * This is the {@code AnnotationOverrideForJavadocStaticInnerRestDto} class javadoc.
	 */
	public static class AnnotationOverrideForJavadocStaticInnerRestDto {
		/**
		 * This is the private {@code #content} field's javadoc.
		 */
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
