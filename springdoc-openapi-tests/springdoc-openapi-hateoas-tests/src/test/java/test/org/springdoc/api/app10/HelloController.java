/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.app10;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("rawtypes")
@RestController
public class HelloController {

	@GetMapping("/page-simple")
	public Page<String> pageSimple() {
		return pageImpl("test");
	}

	@GetMapping("/paged-model-simple")
	public PagedModel<String> pagedModelSimple() {
		return pagedModel("test");
	}

	@GetMapping("/page-complex")
	public Page<Dummy<List<String>>> pageComplex() {
		return pageImpl(new Dummy<>(List.of("test")));
	}

	@GetMapping("/paged-model-complex")
	public PagedModel<Dummy<List<String>>> pagedModelComplex() {
		return pagedModel(new Dummy<>(List.of("test")));
	}

	@GetMapping("/page-raw")
	public Page pageRaw() {
		return pageSimple();
	}

	@GetMapping("/paged-model-raw")
	public PagedModel pagedModelRaw() {
		return pagedModelSimple();
	}

	private <T> PagedModel<T> pagedModel(T value) {
		return new PagedModel<>(pageImpl(value));
	}

	private <T> Page<T> pageImpl(T value) {
		return new PageImpl<>(List.of(value));
	}

}
