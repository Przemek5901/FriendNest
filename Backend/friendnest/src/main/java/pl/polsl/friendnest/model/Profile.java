package pl.polsl.friendnest.model;

import lombok.*;

import java.sql.ConnectionBuilder;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Profile {
    private User user;
    private Long postCount;
    private Long followersCount;
    private Long followingCount;

    private List<Post> posts;

    private List<Comment> comments;

    private List<Interaction> interactions;


}
