package com.example.postswebapi.repositories;

import com.example.postswebapi.entities.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
