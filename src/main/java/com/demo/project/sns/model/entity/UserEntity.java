package com.demo.project.sns.model.entity;

import com.demo.project.sns.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table
@SQLDelete(sql = "UPDATE user SET deleted_at = now() where id = ?")
@Where(clause = "deleted_at is NULL")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Column
    private UserRole role = UserRole.User;

    @Column
    private String password;

    @Column
    private Timestamp registeredAt;

    @Column
    private Timestamp updatedAt;

    // 로그와 같은 관리나 삭제의 flag로서 사용하기 위함
    @Column
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }
}
