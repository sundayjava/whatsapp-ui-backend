package com.davo.backend.service.serviceimpl;

import com.davo.backend.config.TokenProvider;
import com.davo.backend.dto.request.UpdateUserRequest;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.User;
import com.davo.backend.repository.UserRepository;
import com.davo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public User findUserById(Integer id) throws Exception {
        Optional<User> opt = userRepository.findById(id);

        if (opt.isPresent()){
            return  opt.get();
        }
        throw new UserException("User not found with id "+id);
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);

        if (email == null){
            throw  new BadCredentialsException("Received invalid token---");
        }
        User user = userRepository.findByEmail(email);

        if (user == null){
            throw new UserException("User not found with email "+email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest reg) throws Exception {
        User user = findUserById(userId);

        if (reg.getFull_name() != null){
            user.setFull_name(reg.getFull_name());
        }

        if (user.getProfile_picture() != null){
            user.setProfile_picture(reg.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }
}
