package com.demo.project.sns.controller;

import com.demo.project.sns.controller.request.PostCreateRequest;
import com.demo.project.sns.controller.response.Response;
import com.demo.project.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){
        // security 이용해서 userName 받아옴


        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success(null);
    }
}
