package test.org.springdoc.api.app28;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.models.Pageable;

public class ExamplePageableReplacement extends Pageable {

	@Parameter(description = "Anything")
	private int something;

	/**
	 * Instantiates a new Pageable.
	 *  @param page the page
	 * @param size the size
	 * @param sort the sort
	 */
	public ExamplePageableReplacement(int page, int size, List<String> sort) {
		super(page, size, sort);
	}

	public int getSomething() {
		return something;
	}

	public void setSomething(int something) {
		this.something = something;
	}
}
