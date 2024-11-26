package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.AddPostRequest;
import pl.polsl.friendnest.repository.PostRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.FileService;
import pl.polsl.friendnest.service.PostService;

import java.time.OffsetDateTime;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FileService fileService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
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
}
