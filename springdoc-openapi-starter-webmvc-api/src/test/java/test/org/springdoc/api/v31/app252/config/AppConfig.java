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

package test.org.springdoc.api.v31.app252.config;

import java.math.BigDecimal;
import java.util.Map;

import test.org.springdoc.api.v31.app252.repository.Account;
import test.org.springdoc.api.v31.app252.repository.AccountRepository;
import test.org.springdoc.api.v31.app252.repository.Statement;
import test.org.springdoc.api.v31.app252.repository.Statement.Type;
import test.org.springdoc.api.v31.app252.repository.StatementRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author bnasslahsen
 */
@Configuration
public class AppConfig {

	@Bean
	AccountRepository accountRepository() {
		return new AccountRepository(Map.of("1", new Account("1", "Checking")));
	}

	@Bean
	StatementRepository statementRepository() {
		MultiValueMap<String, Statement> statements = new LinkedMultiValueMap<>();
		statements.add("1", new Statement(250, Type.CREDIT));
		statements.add("1", new Statement(110, Type.DEBIT));
		return new StatementRepository(statements);
	}
}
