package org.springdoc.data.rest.utils;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.StringSchema;

import org.springframework.data.rest.webmvc.RestMediaTypes;

/**
 * The interface Spring doc data rest utils.
 * @author bnasslashen
 */
public interface SpringDocDataRestUtils {

	/**
	 * Build text uri content.
	 *
	 * @param content the content
	 */
	static void buildTextUriContent(Content content) {
		if (content.get(RestMediaTypes.TEXT_URI_LIST_VALUE) != null)
			content.put(RestMediaTypes.TEXT_URI_LIST_VALUE, new MediaType().schema(new StringSchema()));
	}
}
