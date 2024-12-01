package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.Interaction;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddInteractionRequest;
import pl.polsl.friendnest.model.response.UserInteractionsToPost;
import pl.polsl.friendnest.repository.CommentRepository;
import pl.polsl.friendnest.repository.InteractionRepository;
import pl.polsl.friendnest.repository.PostRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.InteractionService;

import java.time.OffsetDateTime;

@Service
public class InteractionServiceImpl implements InteractionService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final InteractionRepository interactionRepository;

    public InteractionServiceImpl(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, InteractionRepository interactionRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.interactionRepository = interactionRepository;
    }


    @Override
    public UserInteractionsToPost addInteraction(AddInteractionRequest interactionRequest) {
        if (interactionRequest.getUserId() == null || interactionRequest.getPostId() == null ||
                interactionRequest.getReactionType() == null) {
            throw new CustomException("Błąd, skontaktuj się z administratorem");
        }

        User user = userRepository.findByUserId(interactionRequest.getUserId())
                .orElseThrow(() -> new CustomException("Błąd, skontaktuj się z administratorem"));

        Post post = postRepository.findPostByPostId(interactionRequest.getPostId())
                .orElseThrow(() -> new CustomException("Błąd, skontaktuj się z administratorem"));

        var interaction = Interaction.builder().user(user)
                .post(post)
                .interactionType(interactionRequest.getReactionType())
                .createdAt(OffsetDateTime.now())
                .build();

        if (interactionRequest.getCommentId() != null) {
            Comment comment = commentRepository.findCommentByCommentId(interactionRequest.getCommentId())
                    .orElseThrow(() -> new CustomException("Błąd, skontaktuj się z administratorem"));

            interaction.setComment(comment);
        }

        Interaction oldInteraction = interactionRepository.findByUserAndPostAndInteractionTypeAndComment(
                user, post, interaction.getInteractionType(), interaction.getComment()
        ).orElse(null);

        if (oldInteraction != null) {
            interactionRepository.delete(oldInteraction);
        } else {
            interactionRepository.save(interaction);
        }

        return getUserInteractionsToPost(user, post);

    }

    public UserInteractionsToPost getUserInteractionsToPost(User user, Post post) {
        if(user == null || post == null) {
            throw new CustomException("Wystąpił błąd, skontakuj się z administratorem");
        }

        return UserInteractionsToPost.builder()
                .commentsNumber(interactionRepository.countAllByPostAndInteractionType(post, 1))
                .likesNumber(interactionRepository.countAllByPostAndInteractionType(post, 2))
                .dislikesNumber(interactionRepository.countAllByPostAndInteractionType(post, 3))
                .repostsNumber(interactionRepository.countAllByPostAndInteractionType(post, 4))
                .quotesNumber(interactionRepository.countAllByPostAndInteractionType(post, 5))
                .isCommented(interactionRepository.existsAllByUserAndPostAndAndInteractionType(user, post, 1))
                .isLiked(interactionRepository.existsAllByUserAndPostAndAndInteractionType(user, post, 2))
                .isDisliked(interactionRepository.existsAllByUserAndPostAndAndInteractionType(user, post, 3))
                .isReposted(interactionRepository.existsAllByUserAndPostAndAndInteractionType(user, post, 4))
                .isQuoted(interactionRepository.existsAllByUserAndPostAndAndInteractionType(user, post, 5))
                .build();

    }
}
