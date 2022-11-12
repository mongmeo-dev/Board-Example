package dev.mongmeo.board.web.controller;

import dev.mongmeo.board.service.PostService;
import dev.mongmeo.board.web.dto.post.PostCreateDto;
import dev.mongmeo.board.web.dto.post.PostResponseDto;
import dev.mongmeo.board.web.dto.post.PostUpdateDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @GetMapping
  public String postList(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size,
      Model model) {
    if (page == null) {
      page = 1;
    }
    if (size == null) {
      size = 10;
    }

    List<PostResponseDto> posts = postService.getPostsWithPage(page, size);
    model.addAttribute("posts", posts);
    return "post_list";
  }

  @GetMapping("/{postId}")
  public String postDetail(@PathVariable long postId, Model model) {
    PostResponseDto post = postService.getPostById(postId);
    model.addAttribute("post", post);
    return "post_detail";
  }

  @GetMapping("/create")
  public String postCreateForm(Model model) {
    model.addAttribute("dto", new PostCreateDto());
    return "post_create_form";
  }

  @PostMapping("/create")
  public String createPost(@ModelAttribute PostCreateDto dto,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("dto", dto);
      redirectAttributes.addFlashAttribute("bindError", bindingResult.getAllErrors());
      return "redirect:/posts/create";
    }

    PostResponseDto savedPost = postService.createPosts(dto);
    return "redirect:/posts/" + savedPost.getId();
  }

  @GetMapping("/{postId}/update")
  public String postUpdateForm(@PathVariable long postId, Model model) {
    PostResponseDto post = postService.getPostById(postId);

    model.addAttribute("post", post);
    return "post_update_form";
  }

  @PostMapping("/{postId}/update")
  public String updatePost(@ModelAttribute PostUpdateDto dto,
      @PathVariable long postId,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    dto.setId(postId);

    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("post", dto);
      redirectAttributes.addFlashAttribute("bindError", bindingResult.getAllErrors());
      return "redirect:/posts/" + postId + "/update";
    }

    postService.updatePost(dto);
    return "redirect:/posts/" + postId;
  }

  @GetMapping("/{postId}/delete")
  public String postDeleteForm(@PathVariable long postId, Model model) {
    PostResponseDto post = postService.getPostById(postId);
    model.addAttribute("post", post);
    return "post_delete_form";
  }

  @PostMapping("/{postId}/delete")
  public String deletePost(@PathVariable long postId) {
    postService.deletePost(postId);
    return "redirect:/posts";
  }
}
