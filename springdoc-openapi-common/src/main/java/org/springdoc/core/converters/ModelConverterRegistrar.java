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

package org.springdoc.core.converters;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Wrapper for model converters to only register converters once
 */
public class ModelConverterRegistrar {

	/**
	 * @param modelConverters spring registered model converter beans which have to be
	 *                        registered in {@link ModelConverters} instance
	 */
	public ModelConverterRegistrar(List<ModelConverter> modelConverters) {
		for (ModelConverter modelConverter : modelConverters) {
			Optional<ModelConverter> registeredConverterOptional = getRegisteredConverterSameAs(modelConverter);

			if (!registeredConverterOptional.isPresent()) {
				ModelConverters.getInstance().addConverter(modelConverter);
			} else {
				registeredConverterOptional.ifPresent(alreadyRegisteredModelConverter -> {
					ModelConverters.getInstance().removeConverter(alreadyRegisteredModelConverter);
					ModelConverters.getInstance().addConverter(modelConverter);
				});
			}
		}
	}

	private Optional<ModelConverter> getRegisteredConverterSameAs(ModelConverter modelConverter) {
		try {
			Field convertersField = ModelConverters.class.getDeclaredField("converters");
			ModelConverters modelConvertersInstance = ModelConverters.getInstance();
			convertersField.setAccessible(true);
			List<ModelConverter> modelConverters = (List<ModelConverter>) convertersField.get(modelConvertersInstance);
			return modelConverters.stream()
					.filter(registeredModelConverter -> isSameConverter(registeredModelConverter, modelConverter))
					.findFirst();
		} catch (NoSuchFieldException | IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}

	private boolean isSameConverter(ModelConverter modelConverter1, ModelConverter modelConverter2) {
		// for now we are comparing using the converter types which may not be what we want
		Class<? extends ModelConverter> modelConverter1Class = modelConverter1.getClass();
		Class<? extends ModelConverter> modelConverter2Class = modelConverter2.getClass();

		return modelConverter1Class.equals(modelConverter2Class);
	}
}
