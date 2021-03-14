package test.org.springdoc.api.app25.model;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	private String name;
	
	private String street;
	
	private String number;
	
	private String zipcode;
	
	private String city;
	
	private String country;
	
}
