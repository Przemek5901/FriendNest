package pl.polsl.friendnest.service.impl;


import org.springframework.stereotype.Service;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(Long userId) {
        User user = new User();
        return user;
    }
}
