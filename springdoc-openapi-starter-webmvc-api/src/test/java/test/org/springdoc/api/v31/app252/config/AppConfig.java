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
