package test.org.springdoc.api.app5;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author gibah
 */

@Entity
public class Country {

	@Id
	@GeneratedValue
	private Long id;

	@Basic
	@Column(nullable = false)
	private String name;

	@Basic
	private String shortName;

	@Basic
	private String dialingCode;

	@Basic
	@Column(unique = true, nullable = false)
	private String codeISO3166;

	@Basic
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDialingCode() {
		return dialingCode;
	}

	public void setDialingCode(String dialingCode) {
		this.dialingCode = dialingCode;
	}

	public String getCodeISO3166() {
		return codeISO3166;
	}

	public void setCodeISO3166(String codeISO3166) {
		this.codeISO3166 = codeISO3166;
	}

	public Status getStatus() {
		return status;
	}

	public Country setStatus(Status status) {
		this.status = status;
		return this;
	}

	public enum Status {
		ACTIVE, INACTIVE
	}

}
