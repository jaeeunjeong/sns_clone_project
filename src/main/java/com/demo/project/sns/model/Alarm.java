package com.demo.project.sns.model;

import java.sql.Timestamp;

public class Alarm {
    private Integer id;
    private User user;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp resisterdAt;
    private Timestamp updateAt;
    private Timestamp deleteAt;

    public Alarm ofEntity(Integer id, User user, AlarmType alarmType, AlarmArgs alarmArgs, Timestamp resisterdAt, Timestamp updateAt, Timestamp deleteAt) {
        this.id = id;
        this.user = user;
        this.alarmType = alarmType;
        this.alarmArgs = alarmArgs;
        this.resisterdAt = resisterdAt;
        this.updateAt = updateAt;
        this.deleteAt = deleteAt;
    }
}
