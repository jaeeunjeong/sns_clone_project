package com.demo.project.sns.repository;

import com.demo.project.sns.model.entity.AlarmEntity;
import com.demo.project.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmEntityRepository extends JpaRepository<Integer, AlarmEntity> {
    List<AlarmEntity> findAllByUser(UserEntity userEntity);
}
