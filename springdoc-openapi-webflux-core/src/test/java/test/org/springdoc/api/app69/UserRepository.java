package test.org.springdoc.api.app69;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    public Mono<User> getUserById(@Parameter(in = ParameterIn.PATH, description = "The user Id") Long id);

    @Operation(description = "get all the users")
    public Flux<User> getAllUsers();

	@Operation(description = "get all the users by firstname")
	public Flux<User> getAllUsers(String firstname);

    public Mono<Void> saveUser(Mono<User> user);

    public Mono<User> putUser(@Parameter(in = ParameterIn.PATH) Long id, @RequestBody Mono<User> user);

    public Mono<String> deleteUser(@Parameter(in = ParameterIn.PATH) Long id);
}