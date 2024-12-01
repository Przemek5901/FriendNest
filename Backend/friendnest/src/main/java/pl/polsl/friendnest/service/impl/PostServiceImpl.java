package pl.polsl.friendnest.service.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.PostSpecification;
import pl.polsl.friendnest.model.PostTo;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.model.request.GetPostsRequest;
import pl.polsl.friendnest.repository.PostRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.FileService;
import pl.polsl.friendnest.service.InteractionService;
import pl.polsl.friendnest.service.PostService;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    private final InteractionService interactionService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FileService fileService, InteractionService interactionService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.interactionService = interactionService;
    }

    @Override
    public Post addPost(AddPostRequest payload) {
        if(payload == null) {
            throw new CustomException("Nie udało się dodać postu");
        }

        User user = userRepository.findByUserId(payload.getUserId())
                .orElseThrow(() -> new CustomException("Nie znaleziono użytkownika"));

        Post post = new Post();

        post.setUser(user);

        if(!StringUtils.hasLength(payload.getImageBase64()) && !StringUtils.hasLength(payload.getContent())) {
            throw new CustomException("Nie udało się dodać postu");
        }

        if(StringUtils.hasLength(payload.getImageBase64())) {
            post.setImageUrl(fileService.saveFileFromBase64(payload.getImageBase64(), "posts"));
        }

        if(StringUtils.hasLength(payload.getContent())) {
            post.setContent(payload.getContent());
        }

        post.setCategory(payload.getCategory());
        post.setCreatedAt(OffsetDateTime.now());

        postRepository.save(post);

        return post;
    }

    @Override
    public List<PostTo> getPostsToExceptUser(GetPostsRequest getPostsRequest) {
        if(getPostsRequest.getUserId() == null) {
            throw new CustomException("Błąd, skontaktuj się z administratorem");
        }

        User user = userRepository.findByUserId(getPostsRequest.getUserId())
                .orElseThrow(() -> new CustomException("Błąd, skontaktuj się z administratorem"));


        Specification<Post> specification = Specification
                .where(PostSpecification.userNot(user))
                .and(PostSpecification.category(Integer.decode(getPostsRequest.getCategory())))
                .and(PostSpecification.sortByCriteria(Integer.decode(getPostsRequest.getSortOption())));


        List<Post> posts = postRepository.findAll(specification);


        List<PostTo> postToList = new LinkedList<>();

        for (Post post : posts) {
            PostTo postTo = new PostTo();
            postTo.setPost(post);

            postTo.setUserInteractionsToPost(interactionService.getUserInteractionsToPost(user, post));
            postToList.add(postTo);
        }

        return postToList;
    }

}
