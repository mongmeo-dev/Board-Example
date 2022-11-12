package dev.mongmeo.board.web.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateDto {

  private Long id;
  private String title;
  private String content;
}
