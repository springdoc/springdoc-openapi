package test.org.springdoc.api.app9.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import test.org.springdoc.api.app9.application.dto.ResponseData;
import test.org.springdoc.api.app9.application.dto.FeedResponse;

import java.util.List;
import java.util.UUID;
@Tag(name = "ResponseDataController")
@RestController
@RequestMapping(value = "/some-route", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class FooController {




	@Operation(summary = "Get all data", description = "Get all data")
	@GetMapping(value = "foo/{id}")
	@ResponseStatus(HttpStatus.OK)
	public FeedResponse getFoo(@PathVariable("id") UUID id) {
		var dataList = List.of(ResponseData.builder().dataId(id).build());
		return FeedResponse.createForResponseData(dataList, UUID.randomUUID());
	}
}