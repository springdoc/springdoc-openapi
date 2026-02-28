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

package test.org.springdoc.api.v30.app8;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unchecked")
public class AlbumController {

	@Autowired
	private AlbumModelAssembler albumModelAssembler;

	@Autowired
	private PagedResourcesAssembler pagedResourcesAssembler;

	@GetMapping("/api/albums")
	public PagedModel<Album> getAllAlbums() {
		Album album1 = new Album("album-title-1", "album-description-1", "album-release-date-1");
		Album album2 = new Album("album-title-2", "album-description-2", "album-release-date-2");
		Page<Album> albumPage = new PageImpl<>(Arrays.asList(album1, album2));

		return pagedResourcesAssembler.toModel(albumPage, albumModelAssembler);
	}

}
