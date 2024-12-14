package pl.polsl.friendnest.model.request;

import lombok.*;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChangeUserCredentialsRequest {
    private String login;
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
    private OffsetDateTime birthday;
    private String phoneNumber;
}
