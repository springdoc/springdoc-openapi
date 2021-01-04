package test.org.springdoc.api.app20;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "bank")
public class Bank implements EntityDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bnk_seq")
    @SequenceGenerator(name = "bnk_seq", sequenceName = "bnk_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    @NaturalId
    private String code;

    @Column(unique = true, nullable = false)
    private String name;

    public Long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        Bank bank = (Bank) o;
        return Objects.equals(getCode(), bank.getCode());
    }

    @Override
    public String toString() {
        return "Bank{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

    @Override
    public String getKey() {
        return code;
    }

    @Override
    public String getDescription() {
        return name;
    }
}
