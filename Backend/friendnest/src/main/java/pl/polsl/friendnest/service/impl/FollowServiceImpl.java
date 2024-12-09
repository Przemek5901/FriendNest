package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Follow;
import pl.polsl.friendnest.model.FollowId;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.FollowerToRequest;
import pl.polsl.friendnest.model.response.FollowerTo;
import pl.polsl.friendnest.repository.FollowRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.FollowService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public Follow switchFollow(Long followingUserId, Long followedUserId) {
        if (followingUserId == null || followedUserId == null) {
            throw new CustomException("Both followingUserId and followedUserId must not be null.");
        }

        User followingUser = userRepository.findByUserId(followingUserId)
                .orElseThrow(() -> new CustomException("Following user not found"));

        User followedUser = userRepository.findByUserId(followedUserId)
                .orElseThrow(() -> new CustomException("Followed user not found"));

        Follow follow = followRepository.findByFollowerAndFollowed(followingUser, followedUser).orElse(null);

        if (follow != null) {
            followRepository.delete(follow);
            return null;
        } else {
            FollowId followId = new FollowId();
            followId.setFollowerId(toIntExact(followingUserId));
            followId.setFollowedId(toIntExact(followedUserId));

            Follow followToSave = Follow.builder()
                    .id(followId)
                    .follower(followingUser)
                    .followed(followedUser)
                    .createdAt(OffsetDateTime.now())
                    .build();

            return followRepository.save(followToSave);
        }
    }

    @Override
    public boolean isFollowedByLoggedUser(Long followingUserId, Long followedUserId) {
        if (followingUserId == null || followedUserId == null) {
            throw new CustomException("Both followingUserId and followedUserId must not be null.");
        }

        User followingUser = userRepository.findByUserId(followingUserId)
                .orElseThrow(() -> new CustomException("Following user not found"));

        User followedUser = userRepository.findByUserId(followedUserId)
                .orElseThrow(() -> new CustomException("Followed user not found"));

        Follow follow = followRepository.findByFollowerAndFollowed(followingUser, followedUser).orElse(null);

        return follow != null;
    }

    @Override
    public List<FollowerTo> getFollowerTo(FollowerToRequest followerToRequest) {
        if (followerToRequest.getSearchedUserId() == null || followerToRequest.getLoggedUserId() == null) {
            throw new CustomException("Both followingUserId and followedUserId must not be null.");
        }

        User searchedUser = userRepository.findByUserId(followerToRequest.getSearchedUserId())
                .orElseThrow(() -> new CustomException("Following user not found"));

        User loggedUser = userRepository.findByUserId(followerToRequest.getLoggedUserId())
                .orElseThrow(() -> new CustomException("Followed user not found"));

        List<Follow> followList = followerToRequest.getIsFollower() ? followRepository.findFollowsByFollower(searchedUser) :
                followRepository.findFollowsByFollowed(searchedUser);

        List<FollowerTo> followerToList = new ArrayList<>();

        for (Follow follow : followList) {
            FollowerTo followerTo = new FollowerTo();
            followerTo.setUser(followerToRequest.getIsFollower() ? follow.getFollowed() : follow.getFollower());
            followerTo.setIsFollowing(followRepository.findByFollowerAndFollowed(loggedUser, followerTo.getUser()).isPresent());
            followerToList.add(followerTo);
        }

        return followerToList;
    }

}
