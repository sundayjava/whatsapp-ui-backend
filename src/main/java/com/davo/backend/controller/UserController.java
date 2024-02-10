package com.davo.backend.controller;

import com.davo.backend.dto.request.UpdateUserRequest;
import com.davo.backend.dto.response.ApiResponse;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.User;
import com.davo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<Optional<List<User>>> searchUserByName(@Param("name") String keyword) {
        Optional<List<User>> user = userService.searchUser(keyword);
        return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
    }

    @PutMapping("/user/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest reg, @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(), reg);
        ApiResponse res = new ApiResponse("User updated successfully", true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }
}
