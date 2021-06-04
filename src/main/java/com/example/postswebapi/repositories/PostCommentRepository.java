package com.example.postswebapi.repositories;

import com.example.postswebapi.entities.PostComment;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentRepository extends CrudRepository<PostComment, Long> {
}
