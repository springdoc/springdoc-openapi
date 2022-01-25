package test.org.springdoc.api.app83;

import java.time.Duration;
import java.time.Instant;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
public class CoffeeService {
    private final CoffeeRepository repo;

    public CoffeeService(CoffeeRepository repo) {
        this.repo = repo;
    }

    Flux<Coffee> getAllCoffees() {
        return repo.findAll();
    }

    Mono<Coffee> getCoffeeById(@Parameter(in = ParameterIn.PATH) String id) {
        return repo.findById(id);
    }

    Flux<CoffeeOrder> getOrdersForCoffeeById(@Parameter(in = ParameterIn.PATH) String coffeeId) {
        return Flux.interval(Duration.ofSeconds(1))
                .onBackpressureDrop()
                .map(i -> new CoffeeOrder(coffeeId, Instant.now()));
    }
}
