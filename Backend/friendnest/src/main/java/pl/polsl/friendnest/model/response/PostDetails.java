package pl.polsl.friendnest.model.response;


import lombok.*;
import pl.polsl.friendnest.model.CommentTo;
import pl.polsl.friendnest.model.PostTo;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDetails {

    private PostTo postTo;
    private List<CommentTo> commentTo;
}
