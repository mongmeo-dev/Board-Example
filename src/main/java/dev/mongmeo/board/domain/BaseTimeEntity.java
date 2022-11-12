package dev.mongmeo.board.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseTimeEntity {

  private LocalDateTime CreatedAt = LocalDateTime.now();
}
