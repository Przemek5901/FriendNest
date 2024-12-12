package pl.polsl.friendnest.model;

import lombok.*;
import pl.polsl.friendnest.model.response.UserInteractions;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostTo {
    private Post post;
    private Boolean isReposted;
    private Boolean isQuoted;
    private User reposter;
    private PostTo quotedPost;
    private UserInteractions userInteractions;
}
