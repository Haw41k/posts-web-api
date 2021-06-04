package com.example.postswebapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "POST_ID")
    private Long postId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime dateOfCreation;

    private String text;
    private Integer likes;

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public String getText() {
        return text;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return id.equals(that.id)
                && Objects.equals(postId, that.postId)
                && Objects.equals(dateOfCreation, that.dateOfCreation)
                && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, dateOfCreation, text);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PostComment.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("postId=" + postId)
                .add("dateOfCreation=" + dateOfCreation)
                .add("text='" + text + "'")
                .add("likes=" + likes)
                .toString();
    }
}
