package test.org.springdoc.api.app11;

/**
 * @author bnasslahsen
 */

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

public class TestRequest {
	@Schema(description = "Joe was here with a tuna melt!")
	private String joeWasHere;

	@Schema(description = "This is an example of a map that does not work.!")
	private Map<String, String> testingTheMap;

	public String getJoeWasHere() { return joeWasHere; }
	public void setJoeWasHere(String joeWasHere) { this.joeWasHere = joeWasHere; }
	public Map<String, String> getTestingTheMap() { return testingTheMap; }
	public void setTestingTheMap(Map<String, String> testingTheMap) { this.testingTheMap = testingTheMap; }
}