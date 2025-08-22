package test.org.springdoc.api.v31.app248.dto;

/**
 * @author bnasslahsen
 */

public class Address {
	private AddressLine line1;

	private AddressLine line2;

	private AddressCity city;

	private AddressState state;

	private AddressCountry country;

	public Address() {}

	public AddressLine getLine1() {
		return line1;
	}

	public void setLine1(AddressLine line1) {
		this.line1 = line1;
	}

	public AddressLine getLine2() {
		return line2;
	}

	public void setLine2(AddressLine line2) {
		this.line2 = line2;
	}

	public AddressCity getCity() {
		return city;
	}

	public void setCity(AddressCity city) {
		this.city = city;
	}

	public AddressState getState() {
		return state;
	}

	public void setState(AddressState state) {
		this.state = state;
	}

	public AddressCountry getCountry() {
		return country;
	}

	public void setCountry(AddressCountry country) {
		this.country = country;
	}
}