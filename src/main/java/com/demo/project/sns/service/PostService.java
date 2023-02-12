package com.demo.project.sns.service;

import com.demo.project.sns.exception.ErrorCode;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.model.Post;
import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.repository.PostEntityRepository;
import com.demo.project.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        // user find
        userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // post save
        PostEntity saved = postEntityRepository.save(new PostEntity());

    }

    @Transactional
    public Post modify(String title, String body, String userName, Long postId){
        // user find
       UserEntity userEntity =  userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(
                        () -> new SnsApplicationException(
                                ErrorCode.POST_NOT_FOUND,
                                String.format("%s not founded", postId)));

        // post permission
        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(
                    () -> new SnsApplicationException(
                            ErrorCode.INVALID_PERMISSION,
                            String.format("%s has no permission with %s", userName, postId)));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        postEntityRepository.save(postEntity);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity)); // saveAndFlush 찾아보기
    }

}
