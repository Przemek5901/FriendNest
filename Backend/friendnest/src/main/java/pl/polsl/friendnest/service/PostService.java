package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.request.AddPostRequest;

public interface PostService {

    Post addPost(AddPostRequest payload);
}
