package test.org.springdoc.api.app28;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ExamplePageable implements Pageable {

	@Parameter(description = "Anything")
	@JsonProperty
	private int something;

	@Override
	public int getPageNumber() {
		return 0;
	}

	@Override
	public int getPageSize() {
		return 0;
	}

	@Override
	public long getOffset() {
		return 0;
	}

	@Override
	public Sort getSort() {
		return null;
	}

	@Override
	public Pageable next() {
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		return null;
	}

	@Override
	public Pageable first() {
		return null;
	}

	@Override
	public Pageable withPage(int pageNumber) {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}
}
