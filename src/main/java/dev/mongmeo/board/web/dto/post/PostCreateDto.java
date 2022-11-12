package dev.mongmeo.board.web.dto.post;

import dev.mongmeo.board.domain.post.PostEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDto {

  private String title;
  private String content;
  private String author;

  public PostEntity toEntity() {
    return PostEntity.builder()
        .title(title)
        .content(content)
        .author(author)
        .build();
  }
}
