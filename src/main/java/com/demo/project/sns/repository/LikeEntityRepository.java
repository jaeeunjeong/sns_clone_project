package com.demo.project.sns.repository;

import com.demo.project.sns.model.Post;
import com.demo.project.sns.model.entity.LikeEntity;
import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    @Query("SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post =: post")
    Integer countByPost(PostEntity postEntity);
}
