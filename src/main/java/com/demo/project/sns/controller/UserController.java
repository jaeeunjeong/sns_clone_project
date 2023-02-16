package com.demo.project.sns.controller;

import com.demo.project.sns.controller.request.UserJoinRequest;
import com.demo.project.sns.controller.request.UserLoginRequest;
import com.demo.project.sns.controller.response.AlarmResponse;
import com.demo.project.sns.controller.response.Response;
import com.demo.project.sns.controller.response.UserJoinResponse;
import com.demo.project.sns.controller.response.UserLoginResponse;
import com.demo.project.sns.model.User;
import com.demo.project.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/*
 * 프론트에서 응답값을 가공할 때 파싱이 어려울 수 있어서
 * 획일화된 응답을 만들어주기
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<Void> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        UserJoinResponse response = UserJoinResponse.fromUser(user);
        return Response.success();
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication){
       return userService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromEntity);
    }
}
