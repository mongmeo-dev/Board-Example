package dev.mongmeo.board.domain.post;

import dev.mongmeo.board.domain.BaseTimeEntity;
import dev.mongmeo.board.web.dto.post.PostUpdateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostEntity extends BaseTimeEntity {

  private Long id;
  private String title;
  private String content;
  private String author;

  public void update(PostUpdateDto dto) {
    this.title = dto.getTitle();
    this.content = dto.getContent();
  }
}
