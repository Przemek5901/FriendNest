package pl.polsl.friendnest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"POST\"")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"POST_ID\"", nullable = false)
    private Integer postId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Interaction> interactions;


}