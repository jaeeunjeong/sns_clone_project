package com.demo.project.sns.model.entity;

import com.demo.project.sns.model.AlarmArgs;
import com.demo.project.sns.model.AlarmType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "alarm", indexes = {

})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SQLDelete(sql = "UPDATE alarm SET deleted_at = now() where id = ?") // delete 시 이렇게 사용되도록함.
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType; // 알람이 갑자기 울릴 때 서버 부하나 사용자의 불편함을 유발할 수 있기에 덩어리로 리턴하는 게 조흠

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private AlarmArgs args; // 알람이 확장되었을 때 사용할 수 있도록 만듦.

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

    public static AlarmEntity of(UserEntity user, PostEntity post){
        AlarmEntity entity = new AlarmEntity();

        entity.user = user;
        entity.post = post;
        entity.alarmType;
        entity.args;

        return entity;
    }
}
