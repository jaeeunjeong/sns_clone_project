package com.demo.project.sns.controller;

import com.demo.project.sns.controller.request.PostCreateRequest;
import com.demo.project.sns.controller.request.PostModifyRequest;
import com.demo.project.sns.controller.response.PostResponse;
import com.demo.project.sns.controller.response.Response;
import com.demo.project.sns.model.Post;
import com.demo.project.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        // security 이용해서 userName 받아옴

        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success(null);
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Long postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication){
        // 페이징을 통해 성능 저하를 방지해야함. Pageable 이용
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }
    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication){
        // 페이징을 통해 성능 저하를 방지해야함. Pageable 이용
        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Long postId, Authentication authentication){
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> likeCount(@PathVariable Long postId){
        return Response.success(postService.likeCount(postId));
    }
}
