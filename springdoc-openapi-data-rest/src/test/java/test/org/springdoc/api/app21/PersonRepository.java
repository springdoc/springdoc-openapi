/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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
 *
 */

package test.org.springdoc.api.app21;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "people", path = "peopleme")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	@Operation(description = "this is my test")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "successful operation"),
					@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
					@ApiResponse(responseCode = "404", description = "Contact not found"),
					@ApiResponse(responseCode = "405", description = "Validation exception") }
	)
	List<Person> findByLastName(@Param("lastName") String name);

	@Operation(description = "this is another test", responses = {
			@ApiResponse(responseCode = "200", description = "another successful operation"),
			@ApiResponse(responseCode = "404", description = "another Contact not found") }
	)
	List<Person> findByFirstName(@Param("firstName")  @Parameter(description = "this is for first Name") String name);

}
