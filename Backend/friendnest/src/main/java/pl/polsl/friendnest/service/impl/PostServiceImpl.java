package pl.polsl.friendnest.service.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.*;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.model.request.GetPostDetailsRequest;
import pl.polsl.friendnest.model.request.GetPostsRequest;
import pl.polsl.friendnest.model.response.PostDetails;
import pl.polsl.friendnest.repository.CommentRepository;
import pl.polsl.friendnest.repository.PostRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.FileService;
import pl.polsl.friendnest.service.InteractionService;
import pl.polsl.friendnest.service.PostService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    private final InteractionService interactionService;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FileService fileService, InteractionService interactionService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.interactionService = interactionService;
        this.commentRepository = commentRepository;
    }

    @Override
    public Post addPost(AddPostRequest payload) {
        if (payload == null) {
            throw new CustomException("Nie udało się dodać postu");
        }

        User user = userRepository.findByUserId(payload.getUserId())
                .orElseThrow(() -> new CustomException("Nie znaleziono użytkownika"));

        Post post = new Post();

        post.setUser(user);

        if (!StringUtils.hasLength(payload.getImageBase64()) && !StringUtils.hasLength(payload.getContent())) {
            throw new CustomException("Nie udało się dodać postu");
        }

        if (StringUtils.hasLength(payload.getImageBase64())) {
            post.setImageUrl(fileService.saveFileFromBase64(payload.getImageBase64(), "posts"));
        }

        if (StringUtils.hasLength(payload.getContent())) {
            post.setContent(payload.getContent());
        }

        post.setCategory(payload.getCategory());
        post.setCreatedAt(OffsetDateTime.now());

        postRepository.save(post);

        return post;
    }

    @Override
    public List<PostTo> getPostsToExceptUser(GetPostsRequest getPostsRequest) {
        if (getPostsRequest.getUserId() == null) {
            throw new CustomException();
        }

        User user = userRepository.findByUserId(getPostsRequest.getUserId())
                .orElseThrow(CustomException::new);


        Specification<Post> specification = Specification
                .where(PostSpecification.userNot(user))
                .and(PostSpecification.category(Integer.decode(getPostsRequest.getCategory())))
                .and(PostSpecification.sortByCriteria(Integer.decode(getPostsRequest.getSortOption())));


        List<Post> posts = postRepository.findAll(specification);


        List<PostTo> postToList = new LinkedList<>();

        for (Post post : posts) {
            PostTo postTo = new PostTo();
            postTo.setPost(post);

            postTo.setUserInteractions(interactionService.getUserInteractions(user, null, post));
            postToList.add(postTo);
        }

        return postToList;
    }

    @Override
    public List<PostTo> getUserPosts(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(CustomException::new);

        List<PostTo> postToList = new ArrayList<>();
        List<Post> userPosts = postRepository.getPostsByUser(user);


        if (!userPosts.isEmpty()) {
            for (Post post : userPosts) {
                PostTo postTo = new PostTo();

                postTo.setPost(post);
                postTo.setUserInteractions(interactionService.getUserInteractions(user, null, post));
                postToList.add(postTo);
            }
        }

        return postToList;
    }

    @Override
    public List<CommentTo> getUserComments(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(CustomException::new);

        List<CommentTo> commentToList = new ArrayList<>();
        List<Comment> userComments = commentRepository.findByUser(user);

        if (!userComments.isEmpty()) {
            for (Comment comment : userComments) {
                CommentTo commentTo = new CommentTo();

                commentTo.setComment(comment);
                commentTo.setUserInteractions(interactionService.getUserInteractions(user, comment, comment.getPost()));
                commentToList.add(commentTo);
            }
        }

        return commentToList;
    }

    @Override
    public PostDetails getPostDetails(GetPostDetailsRequest getPostDetailsRequest) {
        if (getPostDetailsRequest.getUserId() == null) {
            throw new CustomException();
        }

        Post post = postRepository.findPostByPostId(getPostDetailsRequest.getPostId())
                .orElseThrow(CustomException::new);

        User user = userRepository.findByUserId(getPostDetailsRequest.getUserId())
                .orElseThrow(CustomException::new);

        PostTo postTo = new PostTo();
        postTo.setPost(post);

        postTo.setUserInteractions(interactionService.getUserInteractions(user, null, post));

        List<Comment> comments = commentRepository.findCommentsByPost(post);

        List<CommentTo> commentToList = new ArrayList<>();

        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                CommentTo commentTo = new CommentTo();
                commentTo.setComment(comment);
                commentTo.setUserInteractions(interactionService.getUserInteractions(user, comment, post));
                commentToList.add(commentTo);
            }
        }

        return PostDetails.builder().postTo(postTo).commentTo(commentToList).build();
    }

    @Override
    public Post deletePost(Long postId) {
        Post post = postRepository.findPostByPostId(postId)
                .orElseThrow(CustomException::new);

        postRepository.delete(post);
        return post;
    }

}
