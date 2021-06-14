package com.example.postswebapi.repositories;

import com.example.postswebapi.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Iterable<PostComment> findAllByPostId(Long postId);
}
