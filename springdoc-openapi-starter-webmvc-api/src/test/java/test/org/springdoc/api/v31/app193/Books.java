package test.org.springdoc.api.v31.app193;


import java.util.ArrayList;
import java.util.Arrays;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a list of Books.")
public class Books extends ArrayList<Book> implements Knowledge {

	public Books() {
		super();
	}

	public Books(Book... books) {
		this();

		if (books != null) {
			Arrays.stream(books).forEach(this::add);
		}
	}
}
