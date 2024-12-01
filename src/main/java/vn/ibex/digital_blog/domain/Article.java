package vn.ibex.digital_blog.domain;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.ibex.digital_blog.util.SecurityUtil;

@Table(name = "articles")
@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title is too long")
    private String title;
    
    @NotBlank(message = "Content is required")
    @Column(columnDefinition = "text")
    private String content;

    private long createdBy;
    
    private Instant createdAt;

    private Instant updatedAt;


    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @OneToMany(mappedBy= "article", fetch = FetchType.LAZY)
    private List<Comment> comments;
    



    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserId();
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handlBeforeUpdate() {
        // this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
        this.updatedAt = Instant.now();

    }

    
}
