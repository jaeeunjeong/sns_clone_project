package com.demo.project.sns.controller;

import com.demo.project.sns.controller.request.UserJoinRequest;
import com.demo.project.sns.controller.request.UserLoginRequest;
import com.demo.project.sns.exception.ErrorCode;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.fixture.UserEntityFixture;
import com.demo.project.sns.model.User;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;

    @DisplayName("회원가입에 성공한다.")
    @Test
    public void test1() throws Exception {

        String userName = "username";
        String password = "password";

        // when
        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("회원가입에 실패한다.")
    @Test
    public void test2() throws Exception {

        String userName = "username";
        String password = "password";

        // when
        when(userService.join(userName, password)).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isConflict())
        ;
    }

    @DisplayName("로그인에 성공한다.")
    @Test
    public void test3() throws Exception {

        String userName = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        // when
        when(userService.login(userName, password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }


    @DisplayName("회원가입이 안 된 경우 로그인")
    @Test
    public void test4() throws Exception {

        String userName = "username";
        String password = "password";

        // when
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @DisplayName("로그인에 정보가 틀린 경우.")
    @Test
    public void test5() throws Exception {

        String userName = "username";
        String password = "password";

        // when
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "DUP"));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("인증되지 않은 유저가 로그인할 경우")
    @Test
    @WithAnonymousUser
    public void test6() throws Exception {

        String userName = "username";
        String password = "password";

        // when
        when(userService.login(userName, password))
                .thenThrow(
                        new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is Duplicated", userName)));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("알람 기능")
    void test7() throws Exception{
        when(userService.alarmList(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("/api/v1/users/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    @DisplayName("알람 리스트 요청시 로그인 하지 않은 경우")
    void test8() throws Exception{
        when(userService.alarmList(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("/api/v1/users/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .andDo(print())
                .andExpect(status().isOk());
    }
}
