package com.demo.project.sns.controller;

import com.demo.project.sns.controller.request.PostCreateRequest;
import com.demo.project.sns.controller.request.PostModifyRequest;
import com.demo.project.sns.exception.ErrorCode;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.fixture.PostEntityFixture;
import com.demo.project.sns.model.Post;
import com.demo.project.sns.service.PostService;
import com.demo.project.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    UserService userService;
    @MockBean
    PostService postService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("포스트작성")
    @Test
    @WithMockUser
    void test1() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("포스트 수정")
    @Test
    @WithMockUser
    public void test2() throws Exception {

        String title = "title";
        String body = "body";

        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("포스트 수정 - 로그인 안한 유저")
    @Test
    @WithAnonymousUser
    public void test3() throws Exception {

        String title = "title";
        String body = "body";

        when(postService.modify(eq(title), eq(body), any(), eq(1L)))
                .thenReturn(Post.fromEntity(PostEntityFixture.get("userName", 1L, 1L)));

        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }


    @DisplayName("포스트 수정 - 작성자가 아닌 경우")
    @Test
    @WithMockUser
    public void test4() throws Exception {

        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1L));

        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("포스트 수정하려는 글이 없는 경우")
    @Test
    @WithMockUser
    public void test5() throws Exception {

        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1L));

        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @DisplayName("포스트 삭제")
    @Test
    @WithMockUser
    public void test6() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
        ;
    }


    @DisplayName("포스트 삭제시 로그인하지 않은 경우")
    @Test
    @WithAnonymousUser
    public void test7() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("포스트 삭제시 작성자와 삭제 요청자가 다른 경우")
    @Test
    @WithAnonymousUser
    public void test8() throws Exception {

        // mocking
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("포스트 삭제시 삭제하려는 포스트가 존재하지 않는 경우")
    @Test
    @WithAnonymousUser
    public void test9() throws Exception {

        // mocking
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }


    @DisplayName("피드 목록 가져오기")
    @Test
    @WithMockUser
    public void test10() throws Exception {
        // TODO mocking
        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("피드목록 요청시 로그인하지 않은 경우")
    @Test
    @WithAnonymousUser
    public void test11() throws Exception {
        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("내 피드 목록 가져오기")
    @Test
    @WithMockUser
    public void test12() throws Exception {
        // TODO mocking
        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("내 피드목록 요청시 로그인하지 않은 경우")
    @Test
    @WithAnonymousUser
    public void test13() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }
}
