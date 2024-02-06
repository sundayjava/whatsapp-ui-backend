package com.davo.backend.controller;

import com.davo.backend.dto.request.GroupChatRequest;
import com.davo.backend.dto.request.SingleChatRequest;
import com.davo.backend.dto.response.ApiResponse;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.Chat;
import com.davo.backend.model.User;
import com.davo.backend.service.ChatService;
import com.davo.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }
    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId(), false);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest, @RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createGroup(groupChatRequest,reqUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws Exception {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId,@RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserProfile(jwt);
        Chat chats = chatService.addUserToGroup(userId, chatId, reqUser);
        return new ResponseEntity<Chat>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId,@RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserProfile(jwt);
        Chat chats = chatService.removeFromGroup(chatId, userId, reqUser);
        return new ResponseEntity<Chat>(chats, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId,reqUser.getId());
        ApiResponse response = new ApiResponse("Chat deleted successfully", false);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
