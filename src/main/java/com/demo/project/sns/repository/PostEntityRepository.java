package com.demo.project.sns.repository;

import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByUser(UserEntity userEntity, Pageable pageable);
}
