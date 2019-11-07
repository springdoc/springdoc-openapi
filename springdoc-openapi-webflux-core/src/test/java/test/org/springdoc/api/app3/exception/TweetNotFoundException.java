package test.org.springdoc.api.app3.exception;

/**
 * Created by rajeevkumarsingh on 22/10/17.
 */
public class TweetNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TweetNotFoundException(String tweetId) {
        super("Tweet not found with id " + tweetId);
    }
}
