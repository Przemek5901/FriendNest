package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.ChangeUserCredentialsRequest;

public interface UserService {
    User getUserById(Long userId);

    User changeUserCredentails(ChangeUserCredentialsRequest changeUserCredentialsRequest);
}
