package test.org.springdoc.api.app162.rest.dto;

/**
 * This is the {@code JavadocOnlyRestDto} class javadoc.
 */
public class JavadocOnlyRestDto {
    /**
     * This is the private {@code #guid} field's javadoc.
     */
    private String guid;
    /**
     * This is the private {@code #inner} field's javadoc.
     *
     * This javadoc description is ignored by the REST documentation:
     * the {@code $ref} can't have a description as any sibling elements of a $ref are ignored.
     */
    private JavadocOnlyStaticInnerRestDto inner;

    public JavadocOnlyRestDto() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public JavadocOnlyStaticInnerRestDto getInner() {
        return inner;
    }

    public void setInner(JavadocOnlyStaticInnerRestDto inner) {
        this.inner = inner;
    }

    /**
     * This is the {@code JavadocOnlyStaticInnerRestDto} class javadoc.
     */
    public static class JavadocOnlyStaticInnerRestDto {
        /**
         * This is the private {@code #content} field's javadoc.
         */
        private String content;

        public JavadocOnlyStaticInnerRestDto() {
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
