package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.request.AddCommentRequest;

public interface CommentService {

    Comment addComment(AddCommentRequest addCommentRequest);
}
