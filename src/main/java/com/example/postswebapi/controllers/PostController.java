package com.example.postswebapi.controllers;

import com.example.postswebapi.entities.Post;
import com.example.postswebapi.exceptions.ElementNotFoundException;
import com.example.postswebapi.repositories.PostRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    final private PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public Iterable<Post> getAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setViews(post.getViews() + 1);

                    return postRepository.save(post);
                })
                .orElseThrow(() -> new ElementNotFoundException(id, "post"));
    }

    @PostMapping
    public Post add(@RequestBody Post post) {
        LocalDateTime currentDate = LocalDateTime.now();

        post.setDateOfCreation(currentDate);
        post.setDateOfUpdate(currentDate);
        post.setViews(0);
        post.setLikes(0);

        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post editById(@RequestBody Post updatedPost, @PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(id, "post"));

        post.setTitle(updatedPost.getTitle());
        post.setText(updatedPost.getText());
        post.setDateOfUpdate(LocalDateTime.now());

        return postRepository.save(post);
    }

    @PutMapping("/{id}/like")
    public void addLike(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(id, "post"));

        post.setLikes(post.getLikes() + 1);

        postRepository.save(post);
    }

    @PutMapping("/{id}/dislike")
    public void subLike(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(id, "post"));

        post.setLikes(post.getLikes() - 1);

        postRepository.save(post);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        try {

            postRepository.deleteById(id);

        } catch (EmptyResultDataAccessException ex) {

            throw new ElementNotFoundException(id, "post");

        }
    }
}
