package dev.mongmeo.board.domain.post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

  boolean existPostById(Long postId);

  Optional<PostEntity> findPostById(Long postId);

  List<PostEntity> findPosts();

  List<PostEntity> findPosts(int page, int size);

  PostEntity save(PostEntity entity);

  void deletePostsById(Long postId);

  long count();

}
