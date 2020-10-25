package test.org.springdoc.api.app82;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
	private Map<Long, User> users = new HashMap<Long, User>();

	@PostConstruct
	public void init() throws Exception {
		users.put(Long.valueOf(1), new User(1, "Jack", "Smith", 20));
		users.put(Long.valueOf(2), new User(2, "Peter", "Johnson", 25));
	}

	@Override
	public Mono<User> getUserById(Long id) {
		return Mono.just(users.get(id));
	}

	@Override
	public Flux<User> getAllUsers() {
		return Flux.fromIterable(this.users.values());
	}

	@Override
	public Flux<User> getAllUsers(String firstname) {
		return Flux.fromIterable(this.users.values().stream().filter(user -> user.getFirstname().equals(firstname)).collect(Collectors.toList()));
	}

	@Override
	public Mono<Void> saveUser(Mono<User> monoUser) {
		Mono<User> userMono = monoUser.doOnNext(user -> {
			// do post
			users.put(user.getId(), user);

			// log on console
			System.out.println("########### POST:" + user);
		});

		return userMono.then();
	}

	@Override
	public Mono<User> putUser(Long id, Mono<User> monoUser) {
		Mono<User> userMono = monoUser.doOnNext(user -> {
			// reset user.Id
			user.setId(id);

			// do put
			users.put(id, user);

			// log on console
			System.out.println("########### PUT:" + user);
		});

		return userMono;
	}

	@Override
	public Mono<String> deleteUser(Long id) {
		// delete processing
		users.remove(id);
		return Mono.just("Delete Succesfully!");
	}
}