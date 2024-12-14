package pl.polsl.friendnest.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"HASHTAG\"")
public class Hashtag {
    @Id
    @Column(name = "\"HASHTAG_ID\"", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hashtagId;

    @Column(name = "\"TAG\"", nullable = false, length = 30)
    private String tag;

}