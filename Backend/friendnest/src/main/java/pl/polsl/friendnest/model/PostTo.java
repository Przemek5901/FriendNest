package pl.polsl.friendnest.model;

import lombok.*;
import pl.polsl.friendnest.model.response.UserInteractionsToPost;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostTo {
    private Post post;
    private UserInteractionsToPost userInteractionsToPost;
}
