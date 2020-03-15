package test.org.springdoc.api.app100;

import java.util.List;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/
public class DummyEntity {
    private String name;
    private String code;
    private SpringDocApp100Test.Status status;
    private ChildEntity child;
    private List<SpringDocApp100Test.Status> notStatuses;

    public List<SpringDocApp100Test.Status> getNotStatuses() {
        return notStatuses;
    }

    public DummyEntity setNotStatuses(List<SpringDocApp100Test.Status> notStatuses) {
        this.notStatuses = notStatuses;
        return this;
    }

    public SpringDocApp100Test.Status getStatus() {
        return status;
    }

    public DummyEntity setStatus(SpringDocApp100Test.Status status) {
        this.status = status;
        return this;
    }

    public ChildEntity getChild() {
        return child;
    }

    public DummyEntity setChild(ChildEntity child) {
        this.child = child;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
