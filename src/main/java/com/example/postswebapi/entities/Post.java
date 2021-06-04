package com.example.postswebapi.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime dateOfCreation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime dateOfUpdate;

    private String title;
    private String text;
    private Integer views;
    private Integer likes;

    @OneToMany(orphanRemoval=true)
    @JoinColumn(name="POST_ID")
    private Set<PostComment> comments;

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public LocalDateTime getDateOfUpdate() {
        return dateOfUpdate;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Integer getViews() {
        return views;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setTitle(String title) {
        this.title = title;
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
        Post post = (Post) o;
        return id.equals(post.id)
                && Objects.equals(dateOfCreation, post.dateOfCreation)
                && Objects.equals(dateOfUpdate, post.dateOfUpdate)
                && Objects.equals(title, post.title)
                && Objects.equals(text, post.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfCreation, dateOfUpdate, title, text);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Post.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dateOfCreation=" + dateOfCreation)
                .add("dateOfUpdate=" + dateOfUpdate)
                .add("title='" + title + "'")
                .add("text='" + text + "'")
                .add("views=" + views)
                .add("likes=" + likes)
                .toString();
    }
}
