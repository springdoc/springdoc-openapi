package test.org.springdoc.api.app1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Parameter;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
@RestController
public class InventoryApiController implements InventoryApi {


	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public InventoryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

	public ResponseEntity<Void> addInventory(
			@Parameter(description = "Inventory item to add") @Valid @RequestBody InventoryItem body) {
		@SuppressWarnings("unused")
		String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

	public ResponseEntity<List<InventoryItem>> searchInventory(
			@Parameter(description = "pass an optional search string for looking up inventory") @Valid @RequestParam(value = "searchString", required = false) String searchString,
			@Min(0) @Parameter(description = "number of records to skip for pagination") @Valid @RequestParam(value = "skip", required = true) Integer skip,
			@Min(0) @Max(50) @Parameter(description = "maximum number of records to return") @Valid @RequestParam(value = "limit", required = true) Integer limit) {
		@SuppressWarnings("unused")
		String accept = request.getHeader("Accept");
        return new ResponseEntity<List<InventoryItem>>(HttpStatus.NOT_IMPLEMENTED);
    }

	public String getme(String language) {
		return language;
	}

}
