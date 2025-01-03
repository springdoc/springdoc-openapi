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

package org.springdoc.core.converters;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.hateoas.Links;
import org.springframework.hateoas.mediatype.hal.RepresentationModelMixin;

/**
 * The type Representation model links oas mixin.
 *
 * @author bnasslahsen
 */
public abstract class RepresentationModelLinksOASMixin extends RepresentationModelMixin {
	@Override
	@Schema(ref = AnnotationsUtils.COMPONENTS_REF + "Links")
	public abstract Links getLinks();
}
