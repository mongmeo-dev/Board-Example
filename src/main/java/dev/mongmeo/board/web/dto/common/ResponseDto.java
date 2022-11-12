package dev.mongmeo.board.web.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

  @JsonSerialize(using = HttpStatusJsonSerializer.class)
  private HttpStatus status;
  private boolean success;
  private ErrorResponseDto error;
  private List<T> payload;
}
