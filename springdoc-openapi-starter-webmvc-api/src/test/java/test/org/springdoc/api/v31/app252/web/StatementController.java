/*
 * Copyright 2002-2025 the original author or authors.
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

package test.org.springdoc.api.v31.app252.web;

import java.util.List;


import test.org.springdoc.api.v31.app252.repository.Statement;
import test.org.springdoc.api.v31.app252.repository.StatementRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts/{id}/statements")
public class StatementController {

	private final StatementRepository repository;

	public StatementController(StatementRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	List<Statement> getStatements(@PathVariable String id) {
		return repository.getStatementsForAccount(id);
	}

	@GetMapping(version = "1.2.0")
	List<Statement> getStatements1_2(@PathVariable String id, @RequestParam(required = false) Statement.Type type) {

		return repository.getStatementsForAccountAndType(id, type);
	}

}
