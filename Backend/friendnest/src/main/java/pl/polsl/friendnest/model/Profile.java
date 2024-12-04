package pl.polsl.friendnest.model;

import lombok.*;

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

    private List<PostTo> posts;

    private List<CommentTo> comments;

    private List<Interaction> interactions;


}
