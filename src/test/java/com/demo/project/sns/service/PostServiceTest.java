package com.demo.project.sns.service;

import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.repository.PostEntityRepository;
import com.demo.project.sns.repository.UserEntityRepository;
import org.h2.api.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Mock
    private PostService postService;

    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    @DisplayName("포스트 작성이 성공한 경우")
    @Test
    void test1() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userEntityRepository.save(any())).thenReturn(mock(PostEntity.class));


        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));

    }


    @DisplayName("포스트 작성이 실패한 경우")
    @Test
    void test2() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(mock(PostEntity.class));


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND_1, e.getErrorCode());
    }
}
