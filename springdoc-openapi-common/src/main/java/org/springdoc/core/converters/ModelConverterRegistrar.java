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

package org.springdoc.core.converters;

import java.util.List;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;

/**
 * Wrapper for model converters to only register converters once
 */
public class ModelConverterRegistrar {

	/**
	 * @param modelConverters spring registered model converter beans which have to be
	 *                        registered in {@link ModelConverters} instance
	 */
	public ModelConverterRegistrar(List<ModelConverter> modelConverters) {
		modelConverters.forEach(ModelConverters.getInstance()::addConverter);
	}
}
