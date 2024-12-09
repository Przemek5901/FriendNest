package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.Follow;
import pl.polsl.friendnest.model.request.FollowerToRequest;
import pl.polsl.friendnest.model.response.FollowerTo;
import pl.polsl.friendnest.service.FollowService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/switchFollow")
    public ResponseEntity<Follow> switchFollow(@RequestBody Map<String, Long> payload) {
        Long followingId = payload.get("followingId");
        Long followedId = payload.get("followedId");
        return ResponseEntity.ok(followService.switchFollow(followingId, followedId));
    }

    @PostMapping("/isFollowedByLoggedUser")
    public ResponseEntity<Boolean> isFollowedByLoggedUser(@RequestBody Map<String, Long> payload) {
        Long followingId = payload.get("followingId");
        Long followedId = payload.get("followedId");
        return ResponseEntity.ok(followService.isFollowedByLoggedUser(followingId, followedId));
    }

    @PostMapping("/getFollowerList")
    public ResponseEntity<List<FollowerTo>> getFollowerList(@RequestBody FollowerToRequest followerToRequest) {
        return ResponseEntity.ok(followService.getFollowerTo(followerToRequest));
    }
}
