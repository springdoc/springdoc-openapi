package test.org.springdoc.api.app1;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

@org.springframework.web.bind.annotation.RestController() public class CarController
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
;
public CarController
(test.org.springdoc.api.app1.CarService carService) {}
@groovy.transform.Generated() @groovy.transform.Internal() public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
@groovy.transform.Generated() @groovy.transform.Internal() public  void setMetaClass(groovy.lang.MetaClass mc) { }
@groovy.transform.Generated() @groovy.transform.Internal() public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
@groovy.transform.Generated() @groovy.transform.Internal() public  java.lang.Object getProperty(java.lang.String property) { return null;}
@groovy.transform.Generated() @groovy.transform.Internal() public  void setProperty(java.lang.String property, java.lang.Object value) { }
public  test.org.springdoc.api.app1.CarService getCarService() { return (test.org.springdoc.api.app1.CarService)null;}
public  void setCarService(test.org.springdoc.api.app1.CarService value) { }
@org.springframework.web.bind.annotation.GetMapping(path="/cars") public  java.util.List<test.org.springdoc.api.app1.Car> getCars() { return (java.util.List<test.org.springdoc.api.app1.Car>)null;}
@org.springframework.web.bind.annotation.GetMapping(path="cars/{carId}") public  test.org.springdoc.api.app1.Car getCar(@org.springframework.web.bind.annotation.PathVariable(value="carId") java.lang.Long carId) { return (test.org.springdoc.api.app1.Car)null;}
}
