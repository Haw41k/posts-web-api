package com.example.postswebapi;

import com.example.postswebapi.entities.Post;
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
public class PostControllerTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void deleteAll() {
        postRepository.deleteAll();
    }

    @Test
    public void getAll() throws Exception {
        Post post = createNewPost();
        postRepository.save(post);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(postRepository.findAll())));
    }

    @Test
    public void getById() throws Exception {
        Post post = createNewPost();

        post = postRepository.save(post);
        post.setViews(post.getViews() + 1);

        mockMvc.perform(get("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(post)));
    }

    @Test
    public void add() throws Exception {
        Post post = createNewPost();

        mockMvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.text").value(post.getText()))
                .andExpect(jsonPath("$.dateOfCreation").isNotEmpty())
                .andExpect(jsonPath("$.dateOfUpdate").isNotEmpty())
                .andExpect(jsonPath("$.likes").value(0))
                .andExpect(jsonPath("$.views").value(0));
    }

    @Test
    public void addLike() throws Exception {

        Post post = postRepository.save(createNewPost());

        mockMvc.perform(
                put("/api/posts/{id}/like", post.getId()))
                .andExpect(status().isOk());

        Post likedPost = postRepository.findById(post.getId()).get();

        Assertions.assertEquals(
                post.getLikes() + 1,
                likedPost.getLikes()
        );
    }

    @Test
    public void subLike() throws Exception {

        Post post = postRepository.save(createNewPost());

        mockMvc.perform(
                put("/api/posts/{id}/dislike", post.getId()))
                .andExpect(status().isOk());

        Post dislikedPost = postRepository.findById(post.getId()).get();

        Assertions.assertEquals(
                post.getLikes() - 1,
                dislikedPost.getLikes()
        );
    }

    @Test
    public void editById() throws Exception {
        Post post = postRepository.save(createNewPost());

        post.setTitle("updated title");
        post.setText("updated text");

        mockMvc.perform(
                put("/api/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(post)));
    }

    @Test
    public void deleteById() throws Exception {
        Post post = postRepository.save(createNewPost());

        mockMvc.perform(delete("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/{id}", post.getId()))
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
}
