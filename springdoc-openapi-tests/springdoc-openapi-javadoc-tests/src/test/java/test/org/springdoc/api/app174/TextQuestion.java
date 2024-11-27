package test.org.springdoc.api.app174;

/**
 * The type Text question.
 *
 * @author bnasslahsen
 */
class TextQuestion extends Question {
	private final String answer;

	public TextQuestion(String question, String type, String answer) {
		super(question, type);
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}

	@Override
	public String toString() {
		return "TextQuestion{" +
				"question='" + getQuestion() + '\'' +
				", type='" + getType() + '\'' +
				", answer='" + answer + '\'' +
				'}';
	}
}