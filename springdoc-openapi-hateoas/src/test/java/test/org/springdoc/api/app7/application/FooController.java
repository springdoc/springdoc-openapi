package test.org.springdoc.api.app7.application;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

  private final FooService fooService;
  private final FooResourceAssembler fooResourceAssembler;

  @Autowired
  public FooController(
      FooService fooService,
      FooResourceAssembler fooResourceAssembler) {
    this.fooService = fooService;
    this.fooResourceAssembler = fooResourceAssembler;
  }

  @GetMapping(value = "foo/{id}", produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<EntityModel<Foo>> getFoo(@PathVariable("id") UUID id) throws Exception {
    Foo foo = fooService.getFoo(id).orElseThrow(Exception::new);
    return ResponseEntity.ok(fooResourceAssembler.toModel(foo));
  }
}