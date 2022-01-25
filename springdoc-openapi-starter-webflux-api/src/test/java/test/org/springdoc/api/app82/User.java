package test.org.springdoc.api.app82;

public class User {
    private long id;
    private String firstname;
    private String lastname;
    private int age;

    public User() {
    }

    public User(long id, String firstname, String lastname, int age) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        String info = String.format("id = %d, firstname = %s, lastname = %s, age = %d", id, firstname, lastname, age);
        return info;
    }
}