package test.org.springdoc.api.v31.app207.controller;

import java.util.ArrayList;
import java.util.List;

import test.org.springdoc.api.v31.app207.model.SuperEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class SuperController<T extends SuperEntity<T>> {

	@GetMapping({ "page/{current}/{size}", "page" })
	public List<? extends T> findPage(@PathVariable(required = false) Long current,
			@PathVariable(required = false) Long size,
			@RequestParam(required = false) T params) {
		return new ArrayList<>();
	}
}
