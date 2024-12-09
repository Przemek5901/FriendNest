package pl.polsl.friendnest.model.response;

import lombok.*;
import pl.polsl.friendnest.model.User;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FollowerTo {
    private User user;
    private Boolean isFollowing;
}
