/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springdoc.core.data;

import tools.jackson.core.Version;
import tools.jackson.databind.module.SimpleModule;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.CollectionModelMixin;
import org.springframework.hateoas.mediatype.hal.LinkMixin;
import org.springframework.hateoas.mediatype.hal.RepresentationModelMixin;

/**
 * Jackson 2 module implementation
 *
 * @author bnasslahsen
 */
public class SpringDocJackson2HalModule extends SimpleModule {

	private static final long serialVersionUID = 7806951456457932384L;

	/**
	 * Instantiates a new Spring doc jackson 2 hal module.
	 */
	public SpringDocJackson2HalModule() {

		super("json-hal-module", new Version(1, 0, 0, null, "org.springframework.hateoas", "spring-hateoas"));

		setMixInAnnotation(Link.class, LinkMixin.class);
		setMixInAnnotation(RepresentationModel.class, RepresentationModelMixin.class);
		setMixInAnnotation(CollectionModel.class, CollectionModelMixin.class);
	}


	


}