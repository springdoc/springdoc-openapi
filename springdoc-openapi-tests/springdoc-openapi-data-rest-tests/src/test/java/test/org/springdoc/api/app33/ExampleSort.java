package test.org.springdoc.api.app33;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Sort;

import java.util.List;

public class ExampleSort extends Sort {

	@Parameter(description = "Anything")
	@JsonProperty
	private int something;

	protected ExampleSort(List<Order> orders) {
		super(orders);
	}
}
