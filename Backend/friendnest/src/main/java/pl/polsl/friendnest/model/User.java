package pl.polsl.friendnest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"USER\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"USER_ID\"", nullable = false)
    private Integer userId;

    @Column(name = "\"LOGIN\"", nullable = false, length = 30)
    private String login;

    @Column(name = "\"PASSWORD_HASH\"", nullable = false)
    private String passwordHash;

    @Column(name = "\"GENDER\"", nullable = false, length = 1)
    private String gender;

    @Column(name = "\"PHONE_NUMBER\"", length = 15)
    private String phoneNumber;

    @Column(name = "\"BIRTHDATE\"")
    private LocalDate birthdate;

    @Column(name = "\"PROFILE_NAME\"", nullable = false, length = 30)
    private String profileName;

    @JsonProperty("userName")
    @Column(name = "\"USER_NAME\"", nullable = false, length = 30)
    private String userName;

    @Column(name = "\"PROFILE_IMAGE_URL\"", nullable = false)
    private String profileImageUrl;

    @Column(name = "\"BACKGROUND_IMAGE_URL\"", nullable = false)
    private String backgroundImageUrl;

    @Column(name = "\"PROFILE_DESC\"")
    private String profileDesc;

    @Column(name = "\"CREATED_AT\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"UPDATED_AT\"", nullable = false)
    private OffsetDateTime updatedAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}