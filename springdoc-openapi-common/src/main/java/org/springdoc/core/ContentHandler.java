package org.springdoc.core;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
public class ContentHandler {
    public ContentHandler() {
    }

    public void mergeContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema) {
        Content content = requestBody.getContent();
        buildContent(requestBody, methodAttributes, schema, content);
    }

    /**
     * Build content.
     *
     * @param requestBody the request body
     * @param methodAttributes the method attributes
     * @param schema the schema
     */
    public void buildContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema) {
        Content content = new Content();
        buildContent(requestBody, methodAttributes, schema, content);
    }

    /**
     * Build content.
     *
     * @param requestBody the request body
     * @param methodAttributes the method attributes
     * @param schema the schema
     * @param content the content
     */
    private void buildContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema, Content content) {
        for (String value : methodAttributes.getMethodConsumes()) {
            io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
            mediaTypeObject.setSchema(schema);
            MediaType mediaType = content.get(value);
            if (mediaType != null) {
                if (mediaType.getExample() != null)
                    mediaTypeObject.setExample(mediaType.getExample());
                if (mediaType.getExamples() != null)
                    mediaTypeObject.setExamples(mediaType.getExamples());
                if (mediaType.getEncoding() != null)
                    mediaTypeObject.setEncoding(mediaType.getEncoding());
            }
            content.addMediaType(value, mediaTypeObject);
        }
        requestBody.setContent(content);
    }
}
