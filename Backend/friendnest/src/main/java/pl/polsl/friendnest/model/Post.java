package pl.polsl.friendnest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "\"POST\"")
public class Post {
    @Id
    @Column(name = "\"POST_ID\"", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"USER_ID\"", nullable = false)
    private User user;

    @Column(name = "\"CONTENT\"", length = 150)
    private String content;

    @Column(name = "\"IMAGE_URL\"")
    private String imageUrl;

    @Column(name = "\"CATEGORY\"", length = 30)
    private String category;

    @Column(name = "\"CREATED_AT\"", nullable = false)
    private OffsetDateTime createdAt;

}