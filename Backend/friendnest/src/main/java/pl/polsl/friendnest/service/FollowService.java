package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Follow;
import pl.polsl.friendnest.model.request.FollowerToRequest;
import pl.polsl.friendnest.model.response.FollowerTo;

import java.util.List;

public interface FollowService {

    Follow switchFollow(Long followingUserId, Long followedUserId);

    boolean isFollowedByLoggedUser(Long followingUserId, Long followedUserId);

    List<FollowerTo> getFollowerTo(FollowerToRequest followerToRequest);
}
