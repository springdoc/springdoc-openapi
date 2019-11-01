package test.org.springdoc.api.app54;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

public class MealParty {
	@JsonView(Views.Public.class)
	private String name;

	@JsonView(Views.MealPartyAdmin.class)
	private List<String> members = new ArrayList<>();
}