package test.org.springdoc.api.app118;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Controller.
 */
@RestController
@RequestMapping("class-hierarchy")
class Controller {
	/**
	 * Abstract parent response.
	 *
	 * @param payload the payload 
	 * @return the response
	 */
	@PostMapping("abstract-parent")
	public Response abstractParent(@RequestBody AbstractParent payload) {
		return null;
	}

	/**
	 * Concrete parent response.
	 *
	 * @param payload the payload 
	 * @return the response
	 */
	@PostMapping("concrete-parent")
	public Response concreteParent(@RequestBody ConcreteParent payload) {
		return null;
	}
}

/**
 * The type Response.
 */
class Response {
	/**
	 * The Abstract parent.
	 */
	AbstractParent abstractParent;

	/**
	 * The Concrete parents.
	 */
	List<ConcreteParent> concreteParents;

	/**
	 * Gets abstract parent.
	 *
	 * @return the abstract parent
	 */
	public AbstractParent getAbstractParent() {
		return abstractParent;
	}

	/**
	 * Sets abstract parent.
	 *
	 * @param abstractParent the abstract parent
	 */
	public void setAbstractParent(AbstractParent abstractParent) {
		this.abstractParent = abstractParent;
	}

	/**
	 * Gets concrete parents.
	 *
	 * @return the concrete parents
	 */
	public List<ConcreteParent> getConcreteParents() {
		return concreteParents;
	}

	/**
	 * Sets concrete parents.
	 *
	 * @param concreteParents the concrete parents
	 */
	public void setConcreteParents(List<ConcreteParent> concreteParents) {
		this.concreteParents = concreteParents;
	}
}
