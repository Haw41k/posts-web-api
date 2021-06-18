package com.example.postswebapi.controllers;

import com.example.postswebapi.entities.Post;
import com.example.postswebapi.entities.PostComment;
import com.example.postswebapi.exceptions.ElementNotFoundException;
import com.example.postswebapi.repositories.PostCommentRepository;
import com.example.postswebapi.repositories.PostRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{post-id}/comments")
public class PostCommentController {
    final private PostCommentRepository commentRepository;
    final private PostRepository postRepository;

    public PostCommentController(PostCommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public Iterable<PostComment> getByPostId(@PathVariable("post-id") Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @GetMapping("/{comment-id}")
    public PostComment getById(@PathVariable("comment-id") Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(id, "comment"));
    }

    @PostMapping
    public PostComment add(@PathVariable("post-id") Long postId, @RequestBody PostComment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ElementNotFoundException(postId, "post"));

        comment.setPostId(post.getId());
        comment.setLikes(0);

        return commentRepository.save(comment);

    }

    @PutMapping("/{comment-id}")
    public PostComment editById(@RequestBody PostComment updatedComment, @PathVariable("comment-id") Long commentId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException(commentId, "comment"));

        comment.setText(updatedComment.getText());

        return commentRepository.save(comment);
    }

    @PutMapping("/{comment-id}/like")
    public void like(@PathVariable("comment-id") Long commentId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException(commentId, "comment"));

        comment.setLikes(comment.getLikes() + 1);

        commentRepository.save(comment);
    }

    @PutMapping("/{comment-id}/dislike")
    public void subLike(@PathVariable("comment-id") Long commentId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException(commentId, "comment"));

        comment.setLikes(comment.getLikes() - 1);

        commentRepository.save(comment);
    }

    @DeleteMapping("/{comment-id}")
    public void deleteById(@PathVariable("comment-id") Long commentId) {
        try {

            commentRepository.deleteById(commentId);

        } catch (EmptyResultDataAccessException ex) {

            throw new ElementNotFoundException(commentId, "comment");

        }
    }
}
