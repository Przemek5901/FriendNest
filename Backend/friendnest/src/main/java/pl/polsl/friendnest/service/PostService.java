package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.CommentTo;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.PostTo;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.model.request.GetPostDetailsRequest;
import pl.polsl.friendnest.model.request.GetPostsRequest;
import pl.polsl.friendnest.model.request.QuotePostRequest;
import pl.polsl.friendnest.model.response.PostDetails;

import java.util.List;

public interface PostService {

    Post addPost(AddPostRequest payload);
    List<PostTo> getPostsToExceptUser(GetPostsRequest getPostsRequest);

    List<PostTo> getUserPosts(Long userId);
    List<CommentTo> getUserComments(Long userId);

    PostDetails getPostDetails(GetPostDetailsRequest getPostDetailsRequest);

    Post deletePost(Long postId);

    Post quotePost(QuotePostRequest quotePostRequest);
}
