package test.org.springdoc.api.app83;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
public class CoffeeRepository {
	Flux<Object> deleteAll(){
		return null;
	}

	Mono<Coffee> findById(String id){
		return null;
	}

	<R> Publisher<? extends R> save(Coffee coffee){
		return null;
	}

	Flux<Coffee> findAll() {
		return null;
	}
}
