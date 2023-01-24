package com.demo.project.sns.service;

import com.demo.project.sns.exception.ErrorCode;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.model.User;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password) {

        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is Duplicated", userName));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, password));

        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {

        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is Duplicated", userName)));

        if (!userEntity.getPassword().equals(password))
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is Duplicated", userName));

        return "";
    }
}
