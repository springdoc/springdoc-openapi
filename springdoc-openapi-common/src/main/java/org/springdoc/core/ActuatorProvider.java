/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

import java.util.Map;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.api.AbstractOpenApiResource;

import org.springframework.util.AntPathMatcher;


/**
 * The interface Actuator provider.
 * @author bnasslahsen
 */
public interface ActuatorProvider {

	/**
	 * Gets methods.
	 *
	 * @return the methods
	 */
	Map getMethods();

	/**
	 * Gets tag.
	 *
	 * @return the tag
	 */
	default Tag getTag() {
		Tag actuatorTag = new Tag();
		actuatorTag.setName(Constants.SPRINGDOC_ACTUATOR_TAG);
		actuatorTag.setDescription(Constants.SPRINGDOC_ACTUATOR_DESCRIPTION);
		actuatorTag.setExternalDocs(
				new ExternalDocumentation()
						.url(Constants.SPRINGDOC_ACTUATOR_DOC_URL)
						.description(Constants.SPRINGDOC_ACTUATOR_DOC_DESCRIPTION)
		);
		return actuatorTag;
	}

	/**
	 * Is rest controller boolean.
	 *
	 * @param operationPath the operation path
	 * @param controllerClass the controller class
	 * @return the boolean
	 */
	default boolean isRestController(String operationPath, Class<?>  controllerClass) {
		return operationPath.startsWith(AntPathMatcher.DEFAULT_PATH_SEPARATOR)
				&&  !AbstractOpenApiResource.isHiddenRestControllers(controllerClass);
	}

	int getApplicationPort();

	int getActuatorPort();

	String getActuatorPath();

	boolean isUseManagementPort();

	String getBasePath();

	String getContextPath();

}
