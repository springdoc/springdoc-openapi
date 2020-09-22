package test.org.springdoc.api.app18;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.format.annotation.DateTimeFormat;

public class HelloWorldModel {

	@Parameter(description = "Description for abc", example = "def")
	@NotBlank
	private String abc;

	@Parameter(description = "Description of this date", example = "2020-10-25")
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate thisDate;

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public LocalDate getThisDate() {
		return thisDate;
	}

	public void setThisDate(LocalDate thisDate) {
		this.thisDate = thisDate;
	}
}