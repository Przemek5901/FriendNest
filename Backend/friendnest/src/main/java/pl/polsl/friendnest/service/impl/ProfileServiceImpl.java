package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Profile;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.ProfileRequest;
import pl.polsl.friendnest.repository.*;
import pl.polsl.friendnest.service.FileService;
import pl.polsl.friendnest.service.PostService;
import pl.polsl.friendnest.service.ProfileService;

import java.time.OffsetDateTime;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final InteractionRepository interactionRepository;

    private final FollowRepository followRepository;
    private final FileService fileService;
    private final PostService postService;


    public ProfileServiceImpl(UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository, InteractionRepository interactionRepository, FollowRepository followRepository, FileService fileService, PostService postService) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.interactionRepository = interactionRepository;
        this.followRepository = followRepository;
        this.fileService = fileService;
        this.postService = postService;
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
                .posts(postService.getUserPosts(Long.valueOf(user.getUserId())))
                .comments(postService.getUserComments(Long.valueOf(user.getUserId())))
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
            user.setProfileImageUrl(fileService.saveFileFromBase64(profileRequest.getProfileImageBase64(), "profile-pictures"));
        }

        if(StringUtils.hasLength(profileRequest.getBackgroundImageBase64())) {
            user.setBackgroundImageUrl(fileService.saveFileFromBase64(profileRequest.getBackgroundImageBase64(), "background-pictures"));
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
}
