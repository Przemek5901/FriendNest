package pl.polsl.friendnest.model.response;

import lombok.*;
import pl.polsl.friendnest.model.PostTo;
import pl.polsl.friendnest.model.User;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResults {
    private List<User> users;
    private List<PostTo> posts;
}
