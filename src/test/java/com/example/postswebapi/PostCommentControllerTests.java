package com.example.postswebapi;


import com.example.postswebapi.entities.Post;
import com.example.postswebapi.entities.PostComment;
import com.example.postswebapi.repositories.PostCommentRepository;
import com.example.postswebapi.repositories.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostCommentControllerTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository commentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void deleteAll() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    public void getAll() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        for (int i = 0; i < 3; i++) {
            PostComment comment = createNewComment();
            comment.setPostId(post.getId());
            commentRepository.save(comment);
        }

        Iterable<PostComment> comments = commentRepository.findAllByPostId(post.getId());

        mockMvc.perform(get("/api/posts/{post-id}/comments", post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(comments)));

    }

    @Test
    public void getById() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        PostComment comment = createNewComment();
        comment.setPostId(post.getId());
        comment = commentRepository.save(comment);

        mockMvc.perform(
                get("/api/posts/{post-id}/comments/{comment-id}",
                        post.getId(), comment.getId())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(comment)));

    }

    @Test
    public void add() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        PostComment comment = createNewComment();
        comment.setPostId(post.getId());

        mockMvc.perform(
                post("/api/posts/{post-id}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.likes").value(0))
                .andExpect(jsonPath("$.dateOfCreation").isNotEmpty());
    }

    @Test
    public void addLike() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        PostComment comment = createNewComment();
        comment.setPostId(post.getId());
        comment = commentRepository.save(comment);

        mockMvc.perform(
                put("/api/posts/{id}/comments/{comment-id}/like", post.getId(), comment.getId()))
                .andExpect(status().isOk());

        PostComment likedComment = commentRepository.findById(comment.getId()).get();

        Assertions.assertEquals(
                comment.getLikes() + 1,
                likedComment.getLikes()
        );
    }

    @Test
    public void subLike() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        PostComment comment = createNewComment();
        comment.setPostId(post.getId());
        comment = commentRepository.save(comment);

        mockMvc.perform(
                put("/api/posts/{id}/comments/{comment-id}/dislike", post.getId(), comment.getId()))
                .andExpect(status().isOk());

        PostComment dislikedComment = commentRepository.findById(comment.getId()).get();

        Assertions.assertEquals(
                comment.getLikes() - 1,
                dislikedComment.getLikes()
        );
    }

    @Test
    public void editById() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        PostComment comment = createNewComment();
        comment.setPostId(post.getId());
        comment = commentRepository.save(comment);

        comment.setText("updated text");

        mockMvc.perform(
                put("/api/posts/{id}/comments/{comment-id}", post.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(comment)));
    }

    @Test
    public void deleteById() throws Exception {
        Post post = createNewPost();
        post = postRepository.save(post);

        PostComment comment = createNewComment();
        comment.setPostId(post.getId());
        comment = commentRepository.save(comment);

        mockMvc.perform(delete("/api/posts/{id}/comments/{comment-id}", post.getId(), comment.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/{id}/comments/{comment-id}", post.getId(), comment.getId()))
                .andExpect(status().isNotFound());
    }

    public Post createNewPost() {
        LocalDateTime currentDate = LocalDateTime.now();
        Post post = new Post();

        post.setTitle("Test post title");
        post.setText("Test post text");
        post.setDateOfCreation(currentDate);
        post.setDateOfUpdate(currentDate);
        post.setViews(0);
        post.setLikes(0);

        return post;
    }

    public PostComment createNewComment() {
        PostComment comment = new PostComment();

        comment.setText("some comment text");
        comment.setLikes(0);

        return comment;
    }

}
