package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddInteractionRequest;
import pl.polsl.friendnest.model.response.UserInteractions;

public interface InteractionService {

    UserInteractions addInteraction(AddInteractionRequest interactionRequest);

    UserInteractions getUserInteractions(User user, Comment comment, Post post);
}
