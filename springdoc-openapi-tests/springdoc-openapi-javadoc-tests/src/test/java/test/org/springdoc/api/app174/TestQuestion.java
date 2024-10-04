package test.org.springdoc.api.app174;

import java.util.List;

/**
 * The type Test question.
 *
 * @author bnasslahsen  test question
 */
public class TestQuestion extends Question {
	/**
	 * list of variants
	 */
	private final List<String> variants;
	/**
	 * correct answer
	 */
	private final int answer;

	public TestQuestion(String question, String type, List<String> variants, int answer) {
		super(question, type);
		this.variants = variants;
		this.answer = answer;
	}

	public List<String> getVariants() {
		return variants;
	}

	public int getAnswer() {
		return answer;
	}

	@Override
	public String toString() {
		return "TestQuestion{" +
				"question='" + getQuestion() + '\'' +
				", variants=" + variants +
				", answer=" + answer +
				'}';
	}
}
