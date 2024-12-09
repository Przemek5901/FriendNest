package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FollowerToRequest {
    private Long searchedUserId;
    private Long loggedUserId;
    private Boolean isFollower;

}
