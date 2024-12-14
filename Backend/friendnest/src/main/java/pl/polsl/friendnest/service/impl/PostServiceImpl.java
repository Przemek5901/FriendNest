package pl.polsl.friendnest.service.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.*;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.model.request.GetPostDetailsRequest;
import pl.polsl.friendnest.model.request.GetPostsRequest;
import pl.polsl.friendnest.model.request.QuotePostRequest;
import pl.polsl.friendnest.model.response.PostDetails;
import pl.polsl.friendnest.repository.*;
import pl.polsl.friendnest.service.FileService;
import pl.polsl.friendnest.service.InteractionService;
import pl.polsl.friendnest.service.PostService;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final InteractionService interactionService;
    private final CommentRepository commentRepository;
    private final InteractionRepository interactionRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FileService fileService, InteractionService interactionService, CommentRepository commentRepository, InteractionRepository interactionRepository, HashtagRepository hashtagRepository, PostHashtagRepository postHashtagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.interactionService = interactionService;
        this.commentRepository = commentRepository;
        this.interactionRepository = interactionRepository;
        this.hashtagRepository = hashtagRepository;
        this.postHashtagRepository = postHashtagRepository;
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


        if(StringUtils.hasLength(payload.getContent())) {
            Pattern pattern = Pattern.compile("\\B#[a-zA-Z0-9_]+");
            Matcher matcher = pattern.matcher(payload.getContent());

            while (matcher.find()) {
                Hashtag hashtag = hashtagRepository.findByTag(matcher.group()).orElse(null);

                if(hashtag == null) {
                    hashtag = Hashtag.builder().tag(matcher.group()).build();
                    hashtagRepository.save(hashtag);
                }

                PostHashtagId postHashtagId = new PostHashtagId(hashtag.getHashtagId(), post.getPostId());
                PostHashtag postHashtag = new PostHashtag(postHashtagId, hashtag, post);
                postHashtagRepository.save(postHashtag);

            }
        }

        return post;
    }

    @Override
    public List<PostTo> getPostsToExceptUser(GetPostsRequest getPostsRequest) {
        if (getPostsRequest.getUserId() == null) {
            throw new CustomException();
        }

        User user = userRepository.findByUserId(getPostsRequest.getUserId())
                .orElseThrow(CustomException::new);

        List<PostTo> postToList = generatePostToList(user, getPostsRequest);

        postToList = postToList.stream()
                .filter(postTo -> filterByCategory(postTo, getPostsRequest.getCategory()))
                .collect(Collectors.toList());

        postToList = postToList.stream()
                .sorted(getComparatorBySortOption(getPostsRequest.getSortOption()))
                .collect(Collectors.toList());

        return postToList;
    }

    private boolean filterByCategory(PostTo postTo, String category) {
        if (!StringUtils.hasLength(category) || category.equals("0") || !StringUtils.hasLength(postTo.getPost().getCategory())) {
            return true;
        }
        return postTo.getPost().getCategory().equals(category);
    }

    private Comparator<PostTo> getComparatorBySortOption(String sortOption) {
        if (sortOption == null || sortOption.equals("1")) {
            return Comparator.comparing(postTo -> postTo.getPost().getCreatedAt(), Comparator.reverseOrder());
        } else if (sortOption.equals("2")) {
            return Comparator.comparing(postTo -> postTo.getPost().getCreatedAt());
        } else if (sortOption.equals("3")) {
            return Comparator.comparing(
                    postTo -> postTo.getPost().getInteractions().stream()
                            .filter(interaction -> interaction.getInteractionType() == 2)
                            .count(),
                    Comparator.reverseOrder()
            );
        } else if (sortOption.equals("4")) {
            return Comparator.comparing(
                    postTo -> postTo.getPost().getInteractions().stream()
                            .filter(interaction -> interaction.getInteractionType() == 2)
                            .count()
            );
        } else {
            return Comparator.comparing(postTo -> postTo.getPost().getCreatedAt(), Comparator.reverseOrder());
        }
    }



    private List<PostTo> generatePostToList(User user, GetPostsRequest getPostsRequest) {
        List<Post> posts = postRepository.findByUserNot(user);
        List<Interaction> reposts = postRepository.getInteractionsByTypeAndUserCondition(4L, user, false);
        List<Interaction> quotedPosts = postRepository.getInteractionsByTypeAndUserCondition(5L, user, false);

        List<PostTo> postToList = new LinkedList<>();
        Set<Integer> processedPostIds = new HashSet<>();

        for (Interaction interaction : quotedPosts) {
            PostTo postTo = new PostTo();
            PostTo quotedPost = new PostTo();
            quotedPost.setPost(interaction.getPost());
            quotedPost.setIsReposted(false);
            quotedPost.setIsQuoted(false);
            quotedPost.setReposter(null);
            quotedPost.setUserInteractions(interactionService.getUserInteractions(user, null, interaction.getPost()));
            postTo.setIsReposted(false);
            postTo.setIsQuoted(true);
            postTo.setReposter(null);
            postTo.setPost(interaction.getNewPost());
            postTo.setQuotedPost(quotedPost);
            postTo.setUserInteractions(interactionService.getUserInteractions(user, null, interaction.getNewPost()));
            postToList.add(postTo);
            processedPostIds.add(interaction.getNewPost().getPostId());
        }

        for (Interaction interaction : reposts) {
            PostTo postTo = new PostTo();
            postTo.setPost(interaction.getPost());
            postTo.setIsReposted(true);
            postTo.setIsQuoted(false);
            postTo.setReposter(interaction.getUser());
            postTo.setQuotedPost(null);
            postTo.setUserInteractions(interactionService.getUserInteractions(user, null, interaction.getPost()));
            postToList.add(postTo);
            processedPostIds.add(interaction.getPost().getPostId());
        }

        for (Post post : posts) {
            if (processedPostIds.contains(post.getPostId())) {
                continue;
            }
            PostTo postTo = new PostTo();
            postTo.setPost(post);
            postTo.setIsReposted(false);
            postTo.setIsQuoted(false);
            postTo.setReposter(null);
            postTo.setQuotedPost(null);
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

        List<Interaction> reposts = postRepository.getInteractionsByTypeAndUserCondition(4L, user, true);
        List<Interaction> quotedPosts = postRepository.getInteractionsByTypeAndUserCondition(5L, user, true);

        Set<Integer> processedPostIds = new HashSet<>();

        for (Interaction interaction : quotedPosts) {
            PostTo postTo = new PostTo();
            PostTo quotedPost = new PostTo();
            quotedPost.setPost(interaction.getPost());
            quotedPost.setIsReposted(false);
            quotedPost.setIsQuoted(false);
            quotedPost.setReposter(null);
            quotedPost.setUserInteractions(interactionService.getUserInteractions(user, null, interaction.getPost()));
            postTo.setIsReposted(false);
            postTo.setIsQuoted(true);
            postTo.setReposter(null);
            postTo.setPost(interaction.getNewPost());
            postTo.setQuotedPost(quotedPost);
            postTo.setUserInteractions(interactionService.getUserInteractions(user, null, interaction.getNewPost()));
            postToList.add(postTo);
            processedPostIds.add(interaction.getNewPost().getPostId());
        }

        for (Interaction interaction : reposts) {
            PostTo postTo = new PostTo();
            postTo.setPost(interaction.getPost());
            postTo.setIsReposted(true);
            postTo.setReposter(interaction.getUser());
            postTo.setQuotedPost(null);
            postTo.setIsQuoted(false);
            postTo.setUserInteractions(interactionService.getUserInteractions(user, null, interaction.getPost()));
            processedPostIds.add(interaction.getPost().getPostId());
            postToList.add(postTo);
        }

        if (!userPosts.isEmpty()) {
            for (Post post : userPosts) {
                if (processedPostIds.contains(post.getPostId())) {
                    continue;
                }
                PostTo postTo = new PostTo();
                postTo.setPost(post);
                postTo.setUserInteractions(interactionService.getUserInteractions(user, null, post));
                postToList.add(postTo);
            }
        }

        postToList.sort(Comparator.comparing(postTo -> postTo.getPost().getCreatedAt(), Comparator.reverseOrder()));

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

        User reposter = new User();

        if (getPostDetailsRequest.getReposterId() != null) {
            reposter = userRepository.findByUserId(getPostDetailsRequest.getReposterId()).orElse(null);
        }

        PostTo postTo = new PostTo();
        postTo.setPost(post);

        if (reposter != null && reposter.getUserId() != null) {
            postTo.setReposter(reposter);
            postTo.setIsReposted(true);
        }

        Post quotedPost = new Post();

        if (getPostDetailsRequest.getQuotedPostId() != null) {
            quotedPost = postRepository.getPostsByPostId(getPostDetailsRequest.getQuotedPostId()).orElse(null);
        }

        if (quotedPost != null && quotedPost.getPostId() != null) {
            postTo.setIsQuoted(true);
            PostTo quotedPostTo = new PostTo();
            quotedPostTo.setPost(quotedPost);
            quotedPostTo.setUserInteractions(interactionService.getUserInteractions(user, null, quotedPost));
            quotedPostTo.setIsReposted(false);
            quotedPostTo.setIsQuoted(false);
            postTo.setQuotedPost(quotedPostTo);
        }

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

        List <PostHashtag> postHashtags = postHashtagRepository.findByPost(post);

        if(postHashtags != null && !postHashtags.isEmpty()) {
            postHashtagRepository.deleteAll(postHashtags);
        }

        postRepository.delete(post);
        return post;
    }

    @Override
    public Post quotePost(QuotePostRequest quotePostRequest) {
        if (quotePostRequest.getUserId() == null || quotePostRequest.getPostId() == null &&
                StringUtils.hasLength(quotePostRequest.getContent())) {
            throw new CustomException();
        }

        User user = userRepository.findByUserId(quotePostRequest.getUserId())
                .orElseThrow(CustomException::new);

        Post post = postRepository.findPostByPostId(quotePostRequest.getPostId())
                .orElseThrow(CustomException::new);

        var postToSave = Post.builder()
                .user(user)
                .category("0")
                .content(quotePostRequest.getContent())
                .createdAt(OffsetDateTime.now())
                .build();

        postRepository.save(postToSave);

        var interaction = Interaction.builder()
                .interactionType(5)
                .createdAt(OffsetDateTime.now())
                .post(post)
                .newPost(postToSave)
                .user(user)
                .build();

        interactionRepository.save(interaction);

        return postToSave;
    }

}
