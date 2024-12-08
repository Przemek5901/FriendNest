package pl.polsl.friendnest.service.impl;


import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            throw new CustomException();
        }

        return userRepository.findByUserId(userId).orElseThrow(CustomException::new);
    }
}
