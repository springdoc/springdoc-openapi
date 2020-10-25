package test.org.springdoc.api.app83;

import java.time.Instant;
import java.util.Objects;

public class CoffeeOrder {
    private String coffeeId;
    private Instant whenOrdered;

    public CoffeeOrder() {
    }

    public CoffeeOrder(String coffeeId, Instant whenOrdered) {
        this.coffeeId = coffeeId;
        this.whenOrdered = whenOrdered;
    }

    public String getCoffeeId() {
        return coffeeId;
    }

    public Instant getWhenOrdered() {
        return whenOrdered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeOrder that = (CoffeeOrder) o;
        return Objects.equals(coffeeId, that.coffeeId) &&
                Objects.equals(whenOrdered, that.whenOrdered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coffeeId, whenOrdered);
    }

    @Override
    public String toString() {
        return "CoffeeOrder{" +
                "coffeeId='" + coffeeId + '\'' +
                ", whenOrdered=" + whenOrdered +
                '}';
    }
}
