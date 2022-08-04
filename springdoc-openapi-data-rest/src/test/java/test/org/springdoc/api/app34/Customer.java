package test.org.springdoc.api.app34;


import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author Oliver Gierke
 */
@Entity
public class Customer {

	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

	private String firstname;
	private String lastname;

	public Long getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	@OneToMany(mappedBy = "customer")
	private Collection<Account> accounts;

	public Collection<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}
}
