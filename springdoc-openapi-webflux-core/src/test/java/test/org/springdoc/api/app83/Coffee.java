package test.org.springdoc.api.app83;

import java.util.Objects;

public class Coffee {

    private String id;
    private String name;

    public Coffee() {
    }

    public Coffee(String name) {
        this.name = name;
    }

    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coffee coffee = (Coffee) o;
        return Objects.equals(id, coffee.id) &&
                Objects.equals(name, coffee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
