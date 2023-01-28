package com.demo.project.sns.service;

import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.fixture.UserEntityFixture;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @DisplayName("회원가입이 정상적으로 작동하는 경우")
    @Test
    void test1() {

        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @DisplayName("회원가입이 정상적으로 작동하지 않는 경우")
    @Test
    void test2() {

        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));

        SnsAppExcep e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
        Assertions.assertEquals(e.g , );
    }


    @DisplayName("로그인 정상적으로 작동하는 경우")
    @Test
    void test3() {

        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @DisplayName("로그인 정상적으로 작동하지않는 경우 회원 아님")
    @Test
    void test4() {

        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
    }

    @DisplayName("로그인 정상적으로 작동하지않는 경우 비밀번호 틀림")
    @Test
    void test5() {

        String userName = "userName";
        String password = "password";
        String password2 = "password2";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password2));
    }
}
