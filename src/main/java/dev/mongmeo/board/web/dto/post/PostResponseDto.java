package dev.mongmeo.board.web.dto.post;

import dev.mongmeo.board.domain.post.PostEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

  private Long id;
  private String title;
  private String content;
  private String author;
  private LocalDateTime createdAt;

  public static PostResponseDto fromEntity(PostEntity entity) {
    return new PostResponseDto(entity.getId(),
        entity.getTitle(),
        entity.getContent(),
        entity.getAuthor(),
        entity.getCreatedAt());
  }
}
