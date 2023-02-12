package com.demo.project.sns.fixture;

import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Long postId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(userEntity);
        result.setId(postId);

        return result;
    }
}
