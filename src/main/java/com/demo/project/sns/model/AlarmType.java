package com.demo.project.sns.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NOW_COMMENT_ON_POST("new comment"), // 값이 변경될 때마다 db 변경 비효율적 -> 서버로 고정 값 관리하는 방법임
    NEW_LIKE_ON_POST("new like"),
    ;

    private final String alarmText;
}
