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

package org.springdoc.core;

import java.util.Arrays;

import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

import org.springframework.http.MediaType;

@SuppressWarnings("rawtypes")
class RequestBodyInfo {

	private RequestBody requestBody;

	private Schema mergedSchema;

	private int nbParams;

	public RequestBodyInfo(MethodAttributes methodAttributes) {
		String[] allConsumes = methodAttributes.getAllConsumes();
		if (Arrays.asList(allConsumes).contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
			this.initMergedSchema();
		}
	}

	public RequestBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}

	public void incrementNbParam() {
		nbParams++;
	}

	public Schema getMergedSchema() {
		if (mergedSchema == null && nbParams > 1) {
			mergedSchema = new ObjectSchema();
		}
		return mergedSchema;
	}

	public void setMergedSchema(Schema mergedSchema) {
		this.mergedSchema = mergedSchema;
	}

	private void initMergedSchema() {
		if (mergedSchema == null) {
			mergedSchema = new ObjectSchema();
		}
	}

	public int getNbParams() {
		return nbParams;
	}

	public void setNbParams(int nbParams) {
		this.nbParams = nbParams;
	}

}
