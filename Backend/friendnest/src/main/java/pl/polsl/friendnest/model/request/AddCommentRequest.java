package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddCommentRequest {
    private Long userId;
    private Long postId;
    private String imageBase64;
    private String content;
}
