package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.PostTo;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.model.request.GetPostsRequest;
import pl.polsl.friendnest.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/addPost")
    public ResponseEntity<Post> addPost(@RequestBody AddPostRequest payload) {
        return ResponseEntity.ok(postService.addPost(payload));

    }

    @PostMapping("/getPostsExceptUser")
    public ResponseEntity<List<PostTo>> getPostsToExceptUser(@RequestBody GetPostsRequest getPostsRequest) {
        return ResponseEntity.ok(postService.getPostsToExceptUser(getPostsRequest));

    }


}
