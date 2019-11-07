package test.org.springdoc.api.app54;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

public class MealParty {
    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.MealPartyAdmin.class)
    private List<String> members = new ArrayList<>();
}