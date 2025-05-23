package pl.polsl.friendnest.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"FOLLOW\"")
public class Follow {
    @EmbeddedId
    private FollowId id;

    @MapsId("followerId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"FOLLOWER_ID\"", nullable = false)
    private User follower;

    @MapsId("followedId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"FOLLOWED_ID\"", nullable = false)
    private User followed;

    @Column(name = "\"CREATED_AT\"", nullable = false)
    private OffsetDateTime createdAt;

}