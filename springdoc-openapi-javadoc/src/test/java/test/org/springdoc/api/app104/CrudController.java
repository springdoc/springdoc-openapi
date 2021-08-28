package test.org.springdoc.api.app104;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The type Crud controller.
 *
 * @param <T>  the type parameter
 */
@Controller
@SuppressWarnings("rawtypes")
public abstract class CrudController<T extends HavingPK> {

	/**
	 * Get t.
	 *
	 * @param id the id
	 * @return the t
	 */
	@GetMapping(path = "{id}")
	@ResponseBody
	@Operation(description = "Get single object")
	public T get( //
			@Parameter(description = "The id to get.", required = true) @PathVariable("id") int id) {
		return null;
	}

	/**
	 * List list.
	 *
	 * @return the list
	 */
	@GetMapping(path = "")
	@ResponseBody
	@Operation(description = "Receive a list of objects")
	public List<T> list() {
		return null;
	}
}