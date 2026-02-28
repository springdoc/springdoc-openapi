/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app9.component.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import test.org.springdoc.api.v30.app9.component.model.DemoComponent;
import test.org.springdoc.api.v30.app9.component.model.DemoComponent.Builder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ComponentsService {

	private static final Map<String, DemoComponent> repo = Collections.singletonMap("1", 
			
			new Builder().id("1").payload("Hello World !").build());

	public Optional<DemoComponent> findById(String componentId) {
		return Optional.ofNullable(repo.get(componentId));
	}

	public Page<DemoComponent> findAll(Pageable pageable) {
		return new PageImpl<DemoComponent>(new ArrayList<>(repo.values()), pageable, repo.size());
	}

}
