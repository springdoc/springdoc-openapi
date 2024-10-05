package test.org.springdoc.api.app170;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema
class Cat implements Animal {

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
