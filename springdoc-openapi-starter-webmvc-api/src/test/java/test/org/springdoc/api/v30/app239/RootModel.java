package test.org.springdoc.api.v30.app239;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RootModel {

    private Integer rootProperty;

    private UnwrappedModel unwrappedModel;

    public Integer getRootProperty() {
        return rootProperty;
    }

    public void setRootProperty(Integer rootProperty) {
        this.rootProperty = rootProperty;
    }

    @JsonUnwrapped
    public UnwrappedModel getUnwrappedModel() {
        return unwrappedModel;
    }

    public void setUnwrappedModel(UnwrappedModel unwrappedModel) {
        this.unwrappedModel = unwrappedModel;
    }
}
