package test.org.springdoc.api.app174;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The type Question.
 *
 * @author bnasslahsen  base class for all questions in test with polymorphism
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = TestQuestion.class, name = "test"),
		@JsonSubTypes.Type(value = TextQuestion.class, name = "text")
})
public abstract class Question {
	private final String question;
	private final String type;

	public Question(String question, String type) {
		this.question = question;
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Question{" +
				"question='" + question + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}