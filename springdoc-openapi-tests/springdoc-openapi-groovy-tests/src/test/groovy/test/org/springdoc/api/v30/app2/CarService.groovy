/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package test.org.springdoc.api.v30.app2


import org.springframework.stereotype.Service

@Service
class CarService {
	List<test.org.springdoc.api.v30.app2.Car> getCars() {
		return [
				new test.org.springdoc.api.v30.app2.Car(id: 1L, name: 'Car 1'),
				new test.org.springdoc.api.v30.app2.Car(id: 2L, name: 'Car 2'),
				new test.org.springdoc.api.v30.app2.Car(id: 3L, name: 'Car 3'),
				new test.org.springdoc.api.v30.app2.Car(id: 4L, name: 'Car 4'),
				new test.org.springdoc.api.v30.app2.Car(id: 5L, name: 'Car 5'),
		]
	}

	test.org.springdoc.api.v30.app2.Car getCar(Long carId) {
		return new test.org.springdoc.api.v30.app2.Car(
				id: carId,
				name: "Car $carId"
		)
	}
}
