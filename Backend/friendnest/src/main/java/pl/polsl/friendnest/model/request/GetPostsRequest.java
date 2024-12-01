package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetPostsRequest {
    private Long userId;
    private String sortOption;
    private String category;
}
