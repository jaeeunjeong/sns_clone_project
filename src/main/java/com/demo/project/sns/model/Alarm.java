package com.demo.project.sns.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    private Integer id;
    private User user;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp registeredAt;
    private Timestamp updateAt;
    private Timestamp deleteAt;

    public static Alarm ofEntity(Integer id, User user, AlarmType alarmType, AlarmArgs alarmArgs, Timestamp registeredAt, Timestamp updateAt, Timestamp deleteAt) {

        Alarm inst = new Alarm();

        inst.id = id;
        inst.user = user;
        inst.alarmType = alarmType;
        inst.alarmArgs = alarmArgs;
        inst.registeredAt = registeredAt;
        inst.updateAt = updateAt;
        inst.deleteAt = deleteAt;

        return inst;
    }
}
