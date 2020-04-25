package test.org.springdoc.api.app1


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CarController {

	CarService carService

	CarController(CarService carService) {
		this.carService = carService
	}

	@GetMapping(path = '/cars')
	List<Car> getCars() {
		return carService.getCars()
	}

	@GetMapping(path = 'cars/{carId}')
	Car getCar(@PathVariable(value = 'carId') Long carId) {
		return carService.getCar(carId)
	}
}
