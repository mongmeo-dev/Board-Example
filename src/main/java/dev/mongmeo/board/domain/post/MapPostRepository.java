package dev.mongmeo.board.domain.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class MapPostRepository implements PostRepository {

  private final Map<Long, PostEntity> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong();

  @Override
  public boolean existPostById(Long postId) {
    return store.get(postId) != null;
  }

  @Override
  public Optional<PostEntity> findPostById(Long postId) {
    return Optional.ofNullable(store.get(postId));
  }

  @Override
  public List<PostEntity> findPosts() {
    return new ArrayList<>(store.values());
  }

  @Override
  public List<PostEntity> findPosts(int page, int size) {
    int startIndex = (page - 1) * size;
    int toIndex = Math.min(startIndex + size, store.size());
    return new ArrayList<>(store.values()).subList(startIndex, toIndex);
  }

  @Override
  public PostEntity save(PostEntity entity) {
    entity.setId(sequence.incrementAndGet());
    entity.setCreatedAt(LocalDateTime.now());
    store.put(entity.getId(), entity);
    return entity;
  }

  @Override
  public void deletePostsById(Long postId) {
    store.remove(postId);
  }

  @Override
  public long count() {
    return store.size();
  }
}
