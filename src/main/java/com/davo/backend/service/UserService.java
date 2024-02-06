package com.davo.backend.service;

import com.davo.backend.dto.request.UpdateUserRequest;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.User;

import java.util.List;

public interface UserService {
    public User findUserById(Integer id) throws Exception;
    public User findUserProfile(String jwt) throws UserException;
    public User updateUser(Integer userId, UpdateUserRequest reg) throws Exception;
    public List<User> searchUser(String query);
}
