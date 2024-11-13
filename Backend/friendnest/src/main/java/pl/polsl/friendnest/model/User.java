package pl.polsl.friendnest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "\"USER\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"USER_ID\"", nullable = false)
    private Integer id;

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

    @Column(name = "\"USER_NAME\"", nullable = false, length = 30)
    private String userName;

    @Column(name = "\"PROFILE_IMAGE_URL\"", nullable = false)
    private String profileImageUrl;

    @Column(name = "\"BACKGROUND_IMAGE_URL\"", nullable = false)
    private String backgroundImageUrl;

    @Column(name = "\"CREATED_AT\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"UPDATED_AT\"", nullable = false)
    private OffsetDateTime updatedAt;

}