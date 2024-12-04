package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.request.AddCommentRequest;
import pl.polsl.friendnest.service.CommentService;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<Comment> addComment(@RequestBody AddCommentRequest payload) {
        return ResponseEntity.ok(commentService.addComment(payload));

    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Comment> deletePost(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));

    }
}
