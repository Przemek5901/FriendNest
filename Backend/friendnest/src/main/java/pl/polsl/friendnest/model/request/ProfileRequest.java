package pl.polsl.friendnest.model.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProfileRequest {
    private Long userId;
    private String profileImageBase64;
    private String backgroundImageBase64;
    private String profileName;
    private String description;
    private String gender;

}
