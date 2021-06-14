package com.example.postswebapi.controllers;

import com.example.postswebapi.entities.PostComment;
import com.example.postswebapi.repositories.PostCommentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{post-id}/comments")
public class PostCommentController {
    final private PostCommentRepository commentRepository;

    public PostCommentController(PostCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public Iterable<PostComment> getByPostId(@PathVariable("post-id") Long postId){
        return commentRepository.findAllByPostId(postId);
    }
}
