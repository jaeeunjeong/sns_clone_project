package com.demo.project.sns.controller;

import com.demo.project.sns.controller.request.PostCreateRequest;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.service.PostService;
import com.demo.project.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.api.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        mockMvc.perform(post("/api/v1/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("인증되지 않은 유저가 로그인할 경우")
    @Test
    @WithAnonymousUser
    public void test5() throws Exception {

        String userName = "username";
        String password = "password";
        String title = "title";
        String body = "body";

        // when
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATE_COLUMN_NAME_1));

        mockMvc.perform(post("/api/v1/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }
}
