package com.demo.project.sns.fixture;

import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Long postId, Long userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(userEntity);
        result.setId(postId);

        return result;
    }
}
