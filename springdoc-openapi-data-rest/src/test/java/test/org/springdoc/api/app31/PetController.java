package test.org.springdoc.api.app31;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetController {

    @GetMapping("/any")
    public Pet getAnyPet() {
        return new Cat(true, "cat");
    }

    @GetMapping("/dog")
    public Dog getDog() {
        return new Dog(true, "dog");
    }
}
