package com.demo.project.sns.model.entity;

import com.demo.project.sns.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table
@SQLDelete(sql = "UPDATE user SET deleted_at = now() where id = ?") // delete 시 이렇게 사용되도록함.
@Where(clause = "deleted_at is NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

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

    public static PostEntity of(String title, String body, UserEntity user){
        PostEntity entity = new PostEntity();

        entity.title = title;
        entity.body = body;
        entity.user = user;

        return entity;
    }
}
