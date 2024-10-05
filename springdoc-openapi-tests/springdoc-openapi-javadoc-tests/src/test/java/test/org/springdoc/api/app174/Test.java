package test.org.springdoc.api.app174;

import java.io.Serializable;
import java.util.List;

/**
 * The type Test.
 *
 * @author bnasslahsen
 */

class Test implements Serializable {

	private static final long serialVersionUID = 1L;  // Recommended for Serializable classes

	private List<Question> questions;

	// No-argument constructor
	public Test() {
	}

	// Constructor with arguments
	public Test(List<Question> questions) {
		this.questions = questions;
	}

	// Getter method for 'questions'
	public List<Question> getQuestions() {
		return questions;
	}

	// Setter method for 'questions'
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	// Optionally, you can override toString, hashCode, equals, etc.
	@Override
	public String toString() {
		return "Test{" +
				"questions=" + questions +
				'}';
	}
}