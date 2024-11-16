package pl.polsl.friendnest.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"POST_HASHTAG\"")
public class PostHashtag {
    @EmbeddedId
    private PostHashtagId id;

    @MapsId("hashtagId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"HASHTAG_ID\"", nullable = false)
    private Hashtag hashtag;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"POST_ID\"", nullable = false)
    private Post post;

}