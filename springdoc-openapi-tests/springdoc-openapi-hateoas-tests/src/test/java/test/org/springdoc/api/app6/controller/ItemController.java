package test.org.springdoc.api.app6.controller;

import java.util.List;
import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import test.org.springdoc.api.app6.model.Item;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Item", description = "The Item API")
public class ItemController {

  List<Item> items = Stream.of(new Item("foo"), new Item("bar")).toList();

  @Operation(summary = "Get all items")
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CollectionModel<Item>> getAllItems() {

    CollectionModel<Item> collection = CollectionModel.of(items);
    collection.add(linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel().expand());

    return new ResponseEntity<>(collection, HttpStatus.OK);
  }
}
