/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app164;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class SampleResponseClass {

	private String idAsFirstParameter;

	private String nameAsSecondParamater;

	private String lastNameAsThirdParameter;

	private boolean booleanValueAsFourthParameter;

	@ArraySchema( arraySchema = @Schema( description = "${blahDescription.value}" ),
			schema = @Schema( description = "${blahDescription.value}" ) )
	private List<String> listBlah;

	public List<String> getListBlah() {
		return listBlah;
	}

	public void setListBlah(List<String> listBlah) {
		this.listBlah = listBlah;
	}

	public String getIdAsFirstParameter() {
		return idAsFirstParameter;
	}

	public void setIdAsFirstParameter(String idAsFirstParameter) {
		this.idAsFirstParameter = idAsFirstParameter;
	}

	public String getNameAsSecondParamater() {
		return nameAsSecondParamater;
	}

	public void setNameAsSecondParamater(String nameAsSecondParamater) {
		this.nameAsSecondParamater = nameAsSecondParamater;
	}

	public String getLastNameAsThirdParameter() {
		return lastNameAsThirdParameter;
	}

	public void setLastNameAsThirdParameter(String lastNameAsThirdParameter) {
		this.lastNameAsThirdParameter = lastNameAsThirdParameter;
	}

	public boolean isBooleanValueAsFourthParameter() {
		return booleanValueAsFourthParameter;
	}

	public void setBooleanValueAsFourthParameter(boolean aBooleanValueAsFourthParameter) {
		this.booleanValueAsFourthParameter = aBooleanValueAsFourthParameter;
	}

}
