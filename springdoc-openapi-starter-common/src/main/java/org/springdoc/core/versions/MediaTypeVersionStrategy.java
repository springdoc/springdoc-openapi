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
package org.springdoc.core.versions;

import org.springframework.http.MediaType;

/**
 * The type Media type version strategy.
 *
 * @author bnasslahsen
 */
public final class MediaTypeVersionStrategy extends SpringDocVersionStrategy {

	/**
	 * The Media type.
	 */
	private final MediaType mediaType;

	/**
	 * The Parameter name.
	 */
	private final String parameterName;

	/**
	 * Instantiates a new Media type version strategy.
	 *
	 * @param mediaType     the media type
	 * @param parameterName the parameter name
	 */
	public MediaTypeVersionStrategy(MediaType mediaType, String parameterName, String defaultVersion) {
		super(defaultVersion);
		this.mediaType = mediaType;
		this.parameterName = parameterName;
		if(defaultVersion != null)
			versionDefaultMap.put(parameterName, defaultVersion);
	}

	/**
	 * Gets media type.
	 *
	 * @return the media type
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * Gets parameter name.
	 *
	 * @return the parameter name
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * Build produces string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] buildProduces() {
		String type = mediaType.getType();
		String subtype = mediaType.getSubtype();
		String produces = type + "/" + subtype + ";" + parameterName + "=" + version;
		return new String[] { produces };
	}

}
