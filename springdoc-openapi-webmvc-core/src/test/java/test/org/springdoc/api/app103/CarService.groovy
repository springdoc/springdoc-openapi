package test.org.springdoc.api.app103


import org.springframework.stereotype.Service

@Service
class CarService {
    List<Car> getCars() {
        return [
				new Car(id: 1L, name: 'Car 1'),
				new Car(id: 2L, name: 'Car 2'),
				new Car(id: 3L, name: 'Car 3'),
				new Car(id: 4L, name: 'Car 4'),
				new Car(id: 5L, name: 'Car 5'),
        ]
    }

    Car getCar(Long carId) {
        return new Car(
                id: carId,
                name: "Car $carId"
        )
    }
}
