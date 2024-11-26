package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddPostRequest {
    private Long userId;
    private String content;
    private String imageBase64;
    private String category;
}
