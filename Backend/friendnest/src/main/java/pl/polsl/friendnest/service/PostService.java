package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.PostTo;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.model.request.GetPostsRequest;

import java.util.List;

public interface PostService {

    Post addPost(AddPostRequest payload);
    List<PostTo> getPostsToExceptUser(GetPostsRequest getPostsRequest);
}
