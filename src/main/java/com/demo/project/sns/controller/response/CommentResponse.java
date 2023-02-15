package com.demo.project.sns.controller.response;

import com.demo.project.sns.model.Post;
import com.demo.project.sns.model.User;

import java.sql.Timestamp;

public class CommentResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment){
        return new CommentResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getUser(),
                post.getRegisteredAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
}
