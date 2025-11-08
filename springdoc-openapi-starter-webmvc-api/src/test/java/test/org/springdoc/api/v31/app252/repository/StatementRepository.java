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

package test.org.springdoc.api.v31.app252.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

public class StatementRepository {

	private final MultiValueMap<String, Statement> statementsByAccount;

	public StatementRepository(MultiValueMap<String, Statement> statements) {
		this.statementsByAccount = new LinkedMultiValueMap<>(statements);
	}

	public List<Statement> getStatementsForAccount(String id) {
		List<Statement> list = statementsByAccount.get(id);
		if (list == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return list;
	}

	public List<Statement> getStatementsForAccountAndType(String id, Statement.Type type) {
		List<Statement> statements = statementsByAccount.get(id);
		if (statements == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		List<Statement> filteredStatements = statements.stream().filter(statement ->
				statement.type() == type
		).collect(Collectors.toUnmodifiableList());

		return filteredStatements;
	}
}
