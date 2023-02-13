package com.demo.project.sns.service;

import com.demo.project.sns.exception.ErrorCode;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.fixture.PostEntityFixture;
import com.demo.project.sns.fixture.UserEntityFixture;
import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.repository.PostEntityRepository;
import com.demo.project.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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


        SnsApplicationException e = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }


    @DisplayName("포스트 수정이 성공한 경우")
    @Test
    void test4() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long postId = 1L;
        Long userId = 1L;

        // PostEntity mockPostEntity = mock(PostEntity.class);
        PostEntity postEntity = PostEntityFixture.get(userName, postId, userId);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));

    }

    @DisplayName("포스트 수정시 요청한 포스트가 존재하지 않는 경우")
    @Test
    void test5() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long postId = 1L;
        Long userId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, userId);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("포스트 수정시 권한이 없는 경우")
    @Test
    void test6() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long postId = 1L;
        Long userId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, userId);
        UserEntity writer = UserEntityFixture.get("other", "password", 1L);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @DisplayName("포스트 삭제이 성공한 경우")
    @Test
    void test7() {

        String userName = "userName";
        Long postId = 1L;
        Long userId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, userId);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.delete(postId, userName));

    }

    @DisplayName("포스트 삭제시 요청한 포스트가 존재하지 않는 경우")
    @Test
    void test8() {

        String userName = "userName";
        Long postId = 1L;
        Long userId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, userId);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.delete(postId, userName));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("포스트 삭제시 권한이 없는 경우")
    @Test
    void test9() {
        String userName = "userName";
        Long postId = 1L;
        Long userId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, userId);
        UserEntity writer = UserEntityFixture.get("other", "password", 1L);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(
                SnsApplicationException.class, () -> postService.delete(postId, userName));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @DisplayName("피드 목록 요청이 성공한 경우")
    @Test
    void test10() {
        Pageable pageable = mock(Pageable.class);

        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.list(pageable));

    }

    @DisplayName("내 피드 목록 요청이 성공한 경우")
    @Test
    void test11() {
        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity = mock(UserEntity.class);

        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findAllByUser(any(), pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.my("", pageable));
    }
}
