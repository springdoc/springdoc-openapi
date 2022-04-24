/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

/**
 * The type Request body info.
 * @author bnasslahsen
 */
@SuppressWarnings("rawtypes")
public class RequestBodyInfo {

	/**
	 * The Request body.
	 */
	private RequestBody requestBody;

	/**
	 * The Merged schema.
	 */
	private Schema mergedSchema;

	/**
	 * Gets request body.
	 *
	 * @return the request body
	 */
	public RequestBody getRequestBody() {
		return requestBody;
	}

	/**
	 * Sets request body.
	 *
	 * @param requestBody the request body
	 */
	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * Gets merged schema.
	 *
	 * @return the merged schema
	 */
	public Schema getMergedSchema() {
		return mergedSchema;
	}

	/**
	 * Sets merged schema.
	 *
	 * @param mergedSchema the merged schema
	 */
	public void setMergedSchema(Schema mergedSchema) {
		this.mergedSchema = mergedSchema;
	}

	/**
	 * Add properties.
	 *
	 * @param paramName the param name
	 * @param schemaN the schema n
	 */
	public void addProperties(String paramName, Schema schemaN) {
		if (mergedSchema == null)
			mergedSchema = new ObjectSchema();
		mergedSchema.addProperty(paramName, schemaN);
	}

}
