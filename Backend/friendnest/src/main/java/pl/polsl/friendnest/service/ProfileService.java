package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Profile;
import pl.polsl.friendnest.model.request.ProfileRequest;

public interface ProfileService {

    Profile getProfile(String login);
    Profile editProfile(ProfileRequest profileRequest);
}
