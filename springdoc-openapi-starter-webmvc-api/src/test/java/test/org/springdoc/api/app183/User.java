package test.org.springdoc.api.app183;

public class User {

	String id;

	String toto;


	public User() {
	}

	public User(String id, String toto) {
		this.id = id;
		this.toto = toto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToto() {
		return toto;
	}

	public void setToto(String toto) {
		this.toto = toto;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", toto='" + toto + '\'' +
				'}';
	}
}
