package org.springdoc.core;

import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springframework.http.MediaType;

import java.util.Arrays;

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
