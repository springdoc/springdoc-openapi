package test.org.springdoc.api.app66.exception;

/**
 * Created by rajeevkumarsingh on 22/10/17.
 */
public class TweetConflictException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TweetConflictException(String tweetId) {
        super("Tweet conflict with id " + tweetId);
    }
}
