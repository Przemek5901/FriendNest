package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetPostDetailsRequest {
    private Long postId;
    private Long userId;
}
