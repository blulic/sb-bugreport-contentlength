package eu.blulic.test.content.length.issue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TextControllerIntegrationTest {

  public static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain;charset=UTF-8";
  public static final int CONTENT_LENGTH = 1694;

  String applicationUrl;

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  void setup() {
    // Configure actuator endpoint
    applicationUrl = "http://localhost:%d/text".formatted(port);
    log.info("Application endpoint: {}", applicationUrl);
  }

  @Test
  void GETshouldHaveContentLengthHeaderInTheResponseHeaders() {

    // Execute request
    ResponseEntity<String> response = restTemplate.getForEntity(applicationUrl, String.class);
    log.info("Received headers: {}", response.getHeaders());

    // Assertions
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getHeaders().getContentLength()).isEqualTo(CONTENT_LENGTH);
    assertThat(Objects.requireNonNull(response.getHeaders().getContentType()).toString()).hasToString(TEXT_PLAIN_CHARSET_UTF_8);
  }

  @Test
  void HEADshouldHaveContentLengthHeaderInTheResponseHeaders() {

    // Execute request
    HttpHeaders httpHeaders = restTemplate.headForHeaders(applicationUrl);
    log.info("Received headers: {}", httpHeaders);

    // Assertions
    assertThat(httpHeaders.getContentLength()).isEqualTo(CONTENT_LENGTH);
    assertThat(Objects.requireNonNull(httpHeaders.getContentType()).toString()).hasToString(TEXT_PLAIN_CHARSET_UTF_8);
  }
}