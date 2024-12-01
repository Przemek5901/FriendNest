package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddInteractionRequest;
import pl.polsl.friendnest.model.response.UserInteractionsToPost;

public interface InteractionService {

    UserInteractionsToPost addInteraction(AddInteractionRequest interactionRequest);

    UserInteractionsToPost getUserInteractionsToPost(User user, Post post);
}
