package eu.blulic.test.content.length.issue;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@RestController
public class TextController {

  @GetMapping ("/text")
  public String getText() {
    ClassPathResource resource = new ClassPathResource("loremipsum.txt");
    try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read Lorem Ipsum file", e);
    }
  }


  @GetMapping(value = "/octetstream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> getTextAsBytes() {
    ClassPathResource resource = new ClassPathResource("loremipsum.txt");
    try {
      byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
      return ResponseEntity
          .ok()
          .header("Content-Disposition", "attachment; filename=loremipsum.txt")
          .body(data);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read Lorem Ipsum file", e);
    }
  }
}