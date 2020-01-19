package test.org.springdoc.api.app66;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@Hidden
public class DefaultHealthCheckApi {

    @GetMapping("/test/date/echo/{date}")
    public String testDateEcho(@DateTimeFormat(pattern = "yyyyMMdd") @PathVariable LocalDate date) {
        return date.toString();
    }

}