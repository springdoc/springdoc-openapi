package org.springdoc.core;

import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

@SuppressWarnings("rawtypes")
public class RequestBodyInfo {

	private RequestBody requestBody;
	private Schema mergedSchema;
	private int nbParams;

	public RequestBody getRequestBody() {
		return requestBody;
	}

	public void incrementNbParam() {
		nbParams++;
	}

	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}

	public Schema getMergedSchema() {
		if (mergedSchema == null && nbParams > 1) {
			mergedSchema = new ObjectSchema();
		}
		return mergedSchema;
	}

	public Schema initMergedSchema() {
		if (mergedSchema == null) {
			mergedSchema = new ObjectSchema();
		}
		return mergedSchema;
	}

	public void setMergedSchema(Schema mergedSchema) {
		this.mergedSchema = mergedSchema;
	}

	public int getNbParams() {
		return nbParams;
	}

	public void setNbParams(int nbParams) {
		this.nbParams = nbParams;
	}

}
