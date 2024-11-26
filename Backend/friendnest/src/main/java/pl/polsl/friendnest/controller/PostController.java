package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.service.PostService;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/addPost")
    public ResponseEntity<Post> register(@RequestBody AddPostRequest payload) {
        return ResponseEntity.ok(postService.addPost(payload));

    }

}
