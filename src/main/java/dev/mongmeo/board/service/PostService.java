package dev.mongmeo.board.service;

import dev.mongmeo.board.domain.post.PostEntity;
import dev.mongmeo.board.domain.post.PostRepository;
import dev.mongmeo.board.web.dto.post.PostCreateDto;
import dev.mongmeo.board.web.dto.post.PostResponseDto;
import dev.mongmeo.board.web.dto.post.PostUpdateDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;

  public List<PostResponseDto> getPostsWithPage(int page, int size) {
    List<PostEntity> posts = postRepository.findPosts(page, size);
    return posts.stream().map(PostResponseDto::fromEntity).collect(Collectors.toList());
  }

  public PostResponseDto getPostById(long postId) {
    PostEntity post = tryGetPostById(postId);
    return PostResponseDto.fromEntity(post);
  }

  public PostResponseDto createPosts(PostCreateDto dto) {
    PostEntity savedPost = postRepository.save(dto.toEntity());
    return PostResponseDto.fromEntity(savedPost);
  }

  public PostResponseDto updatePost(PostUpdateDto dto) {
    PostEntity post = tryGetPostById(dto.getId());
    post.update(dto);
    return PostResponseDto.fromEntity(post);
  }

  public void deletePost(long postId) {
    tryGetPostById(postId);
    postRepository.deletePostsById(postId);
  }

  private PostEntity tryGetPostById(long postId) {
    return postRepository.findPostById(postId)
        .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없음"));
  }
}
