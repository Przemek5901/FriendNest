package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddCommentRequest;
import pl.polsl.friendnest.repository.CommentRepository;
import pl.polsl.friendnest.repository.PostRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.CommentService;
import pl.polsl.friendnest.service.FileService;

import java.time.OffsetDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(PostRepository postRepository, UserRepository userRepository, FileService fileService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment addComment(AddCommentRequest addCommentRequest) {
        if(addCommentRequest == null){
            throw new CustomException();
        }

        Post post = postRepository.findPostByPostId(addCommentRequest.getPostId())
                .orElseThrow(CustomException::new);

        User user = userRepository.findByUserId(addCommentRequest.getUserId())
                .orElseThrow(CustomException::new);

        Comment comment = new Comment();

        comment.setUser(user);
        comment.setPost(post);

        if(!StringUtils.hasLength(addCommentRequest.getImageBase64()) && !StringUtils.hasLength(addCommentRequest.getContent())) {
            throw new CustomException("Nie udało się dodać komentarza!");
        }

        if(StringUtils.hasLength(addCommentRequest.getImageBase64())) {
            comment.setImageUrl(fileService.saveFileFromBase64(addCommentRequest.getImageBase64(), "comments"));
        }

        if(StringUtils.hasLength(addCommentRequest.getContent())) {
            comment.setContent(addCommentRequest.getContent());
        }

        comment.setCreatedAt(OffsetDateTime.now());

        commentRepository.save(comment);

       return comment;
    }

    @Override
    public Comment deleteComment(Long commentId) {
        Comment comment = commentRepository.findCommentByCommentId(commentId)
                .orElseThrow(CustomException::new);

        commentRepository.delete(comment);
        return comment;

    }
}
