package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddInteractionRequest {
    private Long userId;
    private Long postId;
    private Long commentId;
    private Integer reactionType;

}
