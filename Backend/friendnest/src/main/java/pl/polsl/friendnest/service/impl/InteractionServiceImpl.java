package pl.polsl.friendnest.service.impl;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.Interaction;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddInteractionRequest;
import pl.polsl.friendnest.model.response.UserInteractions;
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
    public UserInteractions addInteraction(AddInteractionRequest interactionRequest) {
        if (interactionRequest.getUserId() == null || interactionRequest.getPostId() == null ||
                interactionRequest.getReactionType() == null) {
            throw new CustomException();
        }

        User user = userRepository.findByUserId(interactionRequest.getUserId())
                .orElseThrow(CustomException::new);

        Post post = postRepository.findPostByPostId(interactionRequest.getPostId())
                .orElseThrow(CustomException::new);

        var interaction = Interaction.builder().user(user)
                .post(post)
                .interactionType(interactionRequest.getReactionType())
                .createdAt(OffsetDateTime.now())
                .build();

        Comment comment = new Comment();
        if (interactionRequest.getCommentId() != null) {
            comment = commentRepository.findCommentByCommentId(interactionRequest.getCommentId())
                    .orElseThrow(CustomException::new);

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

        if(interactionRequest.getCommentId() != null) {
            return getUserInteractions(user, comment ,post);
        }
        return getUserInteractions(user, null,post);

    }

    @Override
    public UserInteractions getUserInteractions(User user, Comment comment, Post post) {
        if(user == null || post == null) {
            throw new CustomException();
        }

        return UserInteractions.builder()
                .commentsNumber(comment == null ? commentRepository.countByPost(post) : 0)
                .likesNumber(interactionRepository.countAllByPostAndInteractionTypeAndComment(post, 2, comment))
                .dislikesNumber(interactionRepository.countAllByPostAndInteractionTypeAndComment(post, 3, comment))
                .repostsNumber(interactionRepository.countAllByPostAndInteractionTypeAndComment(post, 4, comment))
                .quotesNumber(interactionRepository.countAllByPostAndInteractionTypeAndComment(post, 5, comment))
                .isCommented(interactionRepository.existsAllByUserAndPostAndAndInteractionTypeAndComment(user, post, 1, comment))
                .isLiked(interactionRepository.existsAllByUserAndPostAndAndInteractionTypeAndComment(user, post, 2, comment))
                .isDisliked(interactionRepository.existsAllByUserAndPostAndAndInteractionTypeAndComment(user, post, 3, comment))
                .isReposted(interactionRepository.existsAllByUserAndPostAndAndInteractionTypeAndComment(user, post, 4, comment))
                .isQuoted(interactionRepository.existsAllByUserAndPostAndAndInteractionTypeAndComment(user, post, 5, comment))
                .build();
    }
}
