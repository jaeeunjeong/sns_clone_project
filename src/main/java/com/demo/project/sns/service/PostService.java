package com.demo.project.sns.service;

import com.demo.project.sns.controller.request.PostCommentRequest;
import com.demo.project.sns.exception.ErrorCode;
import com.demo.project.sns.exception.SnsApplicationException;
import com.demo.project.sns.model.Comment;
import com.demo.project.sns.model.Post;
import com.demo.project.sns.model.entity.CommentEntity;
import com.demo.project.sns.model.entity.LikeEntity;
import com.demo.project.sns.model.entity.PostEntity;
import com.demo.project.sns.model.entity.UserEntity;
import com.demo.project.sns.repository.CommentEntityRepository;
import com.demo.project.sns.repository.LikeEntityRepository;
import com.demo.project.sns.repository.PostEntityRepository;
import com.demo.project.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        // user find
        userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // post save
        PostEntity saved = postEntityRepository.save(new PostEntity());

    }

    @Transactional
    public Post modify(String title, String body, String userName, Long postId) {
        // user find
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(
                        ErrorCode.POST_NOT_FOUND,
                        String.format("%s not founded", postId)));

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(
                    ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        postEntityRepository.save(postEntity);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity)); // saveAndFlush 찾아보기
    }

    @Transactional
    public void delete(Long postId, String userName) {
        // user find
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntityRepository.delete(postEntity);

    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Long postId, String userName){
        // TODO 자주 사용되는 부분이라서 공통화
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // check like -> throw
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it ->{
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %s", userName, postId));
        });

        // likeEntityRepository
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

    }

    public Integer likeCount(Long postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Long postId, PostCommentRequest request, String userName){
        PostEntity postEntity = getPost(postId);
        UserEntity userEntity = getUser(userName);


        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, userName);
    }

    public Page<CommentEntity> getComments(Long postId, Pageable pageable){
        PostEntity postEntity = getPost(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    private PostEntity getPost(Long postId){
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
        return postEntity;
    }
    private UserEntity getUser(String userName){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        return userEntity;
    }
}
