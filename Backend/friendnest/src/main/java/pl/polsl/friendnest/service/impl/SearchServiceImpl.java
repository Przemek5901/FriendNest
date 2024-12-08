package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Interaction;
import pl.polsl.friendnest.model.PostTo;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.response.SearchResults;
import pl.polsl.friendnest.repository.InteractionRepository;
import pl.polsl.friendnest.repository.PostRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.InteractionService;
import pl.polsl.friendnest.service.SearchService;

import java.util.ArrayList;
import java.util.List;


@Service
public class SearchServiceImpl implements SearchService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final InteractionService interactionService;

    public SearchServiceImpl(UserRepository userRepository, PostRepository postRepository, InteractionService interactionService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.interactionService = interactionService;
    }

    @Override
    public SearchResults searchAll(String keyword, Long userId) {
        if(!StringUtils.hasLength(keyword) || userId == null) {
            throw new CustomException("Błąd wyszukiwania");
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(CustomException::new);

        List<User> users = userRepository.searchUsersByKeyword(keyword, null);
        List<Post> posts = postRepository.searchPosts(keyword);
        List<PostTo> postToList = new ArrayList<>();

        posts.forEach(post -> {
            PostTo postTo = new PostTo();
            postTo.setPost(post);
            postTo.setUserInteractions(interactionService.getUserInteractions(user, null, post));
            postToList.add(postTo);
        });

        return SearchResults.builder().posts(postToList).users(users).build();
    }

    @Override
    public List<User> searchUsers(String keyword, Long userId) {
        if(!StringUtils.hasLength(keyword)) {
            throw new CustomException("Błąd wyszukiwania");
        }

        return userRepository.searchUsersByKeyword(keyword, userId);
    }
}
