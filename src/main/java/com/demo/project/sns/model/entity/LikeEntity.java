package com.demo.project.sns.model.entity;

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
@Table(name = "like")
@SQLDelete(sql = "UPDATE like SET deleted_at = now() where id = ?") // delete 시 이렇게 사용되도록함.
@Where(clause = "deleted_at is NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column
    private Timestamp registeredAt;

    @Column
    private Timestamp updatedAt;

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

    public static LikeEntity of( UserEntity user, PostEntity post){
        LikeEntity entity = new LikeEntity();

        entity.user = user;
        entity.post = post;

        return entity;
    }
}
