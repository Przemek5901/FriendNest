package pl.polsl.friendnest.model;

import lombok.*;
import pl.polsl.friendnest.model.response.UserInteractions;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentTo {
    private Comment comment;
    private UserInteractions userInteractions;
}
