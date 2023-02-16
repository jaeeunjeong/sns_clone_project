package com.demo.project.sns.controller.response;

import com.demo.project.sns.model.Alarm;
import com.demo.project.sns.model.AlarmArgs;
import com.demo.project.sns.model.AlarmType;
import com.demo.project.sns.model.User;
import com.demo.project.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp resisterdAt;
    private Timestamp updateAt;
    private Timestamp deleteAt;

    public Alarm fromEntity(AlarmEntity entity) {
        this.id = id;
        this.alarmType = alarmType;
        this.alarmArgs = alarmArgs;
        this.resisterdAt = resisterdAt;
        this.updateAt = updateAt;
        this.deleteAt = deleteAt;
        return null;
    }
}
