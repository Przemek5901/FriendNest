package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Profile;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.ProfileRequest;
import pl.polsl.friendnest.repository.*;
import pl.polsl.friendnest.service.ProfileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final InteractionRepository interactionRepository;

    private final FollowRepository followRepository;


    public ProfileServiceImpl(UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository, InteractionRepository interactionRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.interactionRepository = interactionRepository;
        this.followRepository = followRepository;
    }

    @Override
    public Profile getProfile(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new CustomException("Nie znaleziono użytkownika!"));

        return Profile.builder()
                .user(user)
                .postCount(postRepository.countAllByUser(user))
                .followersCount(followRepository.countAllByFollower(user))
                .followingCount(followRepository.countAllByFollowed(user))
                .posts(postRepository.getPostsByUser(user))
                .interactions(interactionRepository.findByUser(user))
                .build();
    }

    @Override
    public Profile editProfile(ProfileRequest profileRequest) {
        if(profileRequest == null) {
            throw new CustomException("Nie można edytować profilu");
        }
        User user = userRepository.findByUserId(profileRequest.getUserId())
                .orElseThrow(() -> new CustomException("Nie można edytować profilu"));

        if(StringUtils.hasLength(profileRequest.getProfileImageBase64())) {
            user.setProfileImageUrl(saveFileFromBase64(profileRequest.getProfileImageBase64(), "profile-pictures"));
        }

        if(StringUtils.hasLength(profileRequest.getBackgroundImageBase64())) {
            user.setBackgroundImageUrl(saveFileFromBase64(profileRequest.getBackgroundImageBase64(), "background-pictures"));
        }

        if(StringUtils.hasLength(profileRequest.getDescription())) {
            user.setProfileDesc(profileRequest.getDescription());
        }
        user.setProfileName(profileRequest.getProfileName());
        user.setGender(profileRequest.getGender());
        user.setUpdatedAt(OffsetDateTime.now());

        User updatedUser = userRepository.save(user);

        return getProfile(updatedUser.getLogin());

    }

    public String saveFileFromBase64(String base64, String path) {
        String[] parts = base64.split(",");

        String mimeType = parts[0].split(":")[1].split(";")[0];
        String base64Content = parts[1];

        String extension = "";
        if (mimeType.equals("image/png")) {
            extension = ".png";
        } else if (mimeType.equals("image/jpeg")) {
            extension = ".jpg";
        } else if (mimeType.equals("image/gif")) {
            extension = ".gif";
        } else {
            throw new IllegalArgumentException("Nieobsługiwany typ pliku: " + mimeType);
        }

        String folderPath = "D:\\uploads\\" + path + "\\";
        File directory = new File(folderPath);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new CustomException("Nie udało się edytować profilu");
            }
        }

        String fileName = UUID.randomUUID().toString();
        String filePath = folderPath + "\\" + fileName + extension;

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Content);

            File outputFile = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(imageBytes);
                return "http://localhost:8080/" + path + "/" + fileName + extension;
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new CustomException("Nie udało się edytować profilu");
        }
    }

}
