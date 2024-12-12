package pl.polsl.friendnest.model.request;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuotePostRequest {
    private Long postId;
    private Long userId;
    private String content;
}
