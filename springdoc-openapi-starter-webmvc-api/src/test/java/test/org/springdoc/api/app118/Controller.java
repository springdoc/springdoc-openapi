package test.org.springdoc.api.app118;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("class-hierarchy")
public class Controller {
	@PostMapping("abstract-parent")
	public Response abstractParent(@RequestBody AbstractParent payload) {
		return null;
	}

	@PostMapping("concrete-parent")
	public Response concreteParent(@RequestBody ConcreteParent payload) {
		return null;
	}
}

class Response {
	AbstractParent abstractParent;

	List<ConcreteParent> concreteParents;

	public AbstractParent getAbstractParent() {
		return abstractParent;
	}

	public void setAbstractParent(AbstractParent abstractParent) {
		this.abstractParent = abstractParent;
	}

	public List<ConcreteParent> getConcreteParents() {
		return concreteParents;
	}

	public void setConcreteParents(List<ConcreteParent> concreteParents) {
		this.concreteParents = concreteParents;
	}
}
