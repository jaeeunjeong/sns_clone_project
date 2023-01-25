package com.demo.project.sns.model;

import com.demo.project.sns.model.entity.UserEntity;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * dto와 entity의 분리의 이유
 * 서비스에선 dto
 * db에선 entity
 * entity는 db랑 밀접해서 클래스의 변화에 민감함
 */
@Getter
public class User {
    private Long id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        return new User();
    }
}
