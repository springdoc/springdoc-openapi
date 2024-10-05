package test.org.springdoc.api.app163.rest.dto;

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
