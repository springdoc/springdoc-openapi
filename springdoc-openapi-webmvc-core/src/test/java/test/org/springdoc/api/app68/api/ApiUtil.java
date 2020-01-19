package test.org.springdoc.api.app68.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiUtil {

    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            req.getNativeResponse(HttpServletResponse.class).addHeader("Content-Type", contentType);
            req.getNativeResponse(HttpServletResponse.class).getOutputStream().print(example);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkApiKey(NativeWebRequest req) {
        if (!"1".equals(System.getenv("DISABLE_API_KEY")) && !"special-key".equals(req.getHeader("api_key"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing API key!");
        }
    }
}