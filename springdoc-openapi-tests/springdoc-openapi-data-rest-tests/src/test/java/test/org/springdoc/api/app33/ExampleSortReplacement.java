package test.org.springdoc.api.app33;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.models.Sort;

public class ExampleSortReplacement extends Sort {

	@Parameter(description = "Anything")
	private int something;

	/**
	 * Instantiates a new Sort.
	 * @param sort the sort
	 */
	public ExampleSortReplacement(List<String> sort) {
		super(sort);
	}

	public int getSomething() {
		return something;
	}

	public void setSomething(int something) {
		this.something = something;
	}
}
