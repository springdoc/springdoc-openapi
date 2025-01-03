package test.org.springdoc.api.v31.app193;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a Cat class.")
public class Cat implements Animal {

	private Integer speed;

	public Cat(Integer speed) {
		this.speed = speed;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
}
