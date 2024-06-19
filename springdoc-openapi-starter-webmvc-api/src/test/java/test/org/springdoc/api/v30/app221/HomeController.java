package test.org.springdoc.api.v30.app221;

import java.io.IOException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HomeController implements HomeApi {
	
  @Override
  public HelloDto uploadContent(@Valid HelloUploadDto uploadDto)
      throws IOException {
    var fileBytes = uploadDto.file.getBytes();
    return new HelloDto(uploadDto.title, fileBytes);
  }

  public record HelloDto(String title, byte[] file) {}

  public record HelloUploadDto(@NotNull String title, MultipartFile file) {}
}
