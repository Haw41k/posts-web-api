package com.example.postswebapi;

import com.example.postswebapi.entities.Post;
import com.example.postswebapi.entities.PostComment;
import com.example.postswebapi.repositories.PostCommentRepository;
import com.example.postswebapi.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DemoDataBaseLoader {
    private static final Logger log = LoggerFactory.getLogger(PostsWebApiApplication.class);

    @Bean
    public CommandLineRunner demo(PostRepository postRepository, PostCommentRepository commentRepository) {
        return (args) -> {
            for (int i = 0; i < 3; i++) {
                Post post = new Post();
                LocalDateTime currentDate = LocalDateTime.now();

                post.setDateOfCreation(currentDate);
                post.setDateOfUpdate(currentDate);
                post.setTitle("post: " + i);
                post.setText("Some text " + i);
                post.setViews(0);
                post.setLikes(0);

                log.info("\n added new: " + postRepository.save(post));
            }

            for (int i = 0; i < 3; i++) {
                PostComment comment = new PostComment();

                comment.setPostId(1L);
                comment.setText("comment " + i);
                comment.setLikes(0);

                log.info("\n added new: " + commentRepository.save(comment));
            }
        };
    }
}
