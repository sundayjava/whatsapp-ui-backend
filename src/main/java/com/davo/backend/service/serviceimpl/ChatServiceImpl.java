package com.davo.backend.service.serviceimpl;

import com.davo.backend.dto.request.GroupChatRequest;
import com.davo.backend.exception.ChatException;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.Chat;
import com.davo.backend.model.User;
import com.davo.backend.repository.ChatRepository;
import com.davo.backend.service.ChatService;
import com.davo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public Chat createChat(User reqUser, Integer userId2, boolean isGroup) throws Exception {
        User user = userService.findUserById(userId2);
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);
        if (isChatExist != null) {
            return isChatExist;
        }

        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);

        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw new ChatException("Chat not found with id: " + chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws Exception {
        User user = userService.findUserById(userId);

        return chatRepository.findChatByUserId(user.getId());
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws Exception {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(req.getChat_image());
        group.setChat_name(req.getChat_name());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);

        for (Integer userId : req.getUserIds()) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws Exception {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);

        if (opt.isPresent()) {
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            } else {
                throw new UserException("You are not admin");
            }
        }
        throw new ChatException("Chat not found with id: " + chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User regUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);

        if (opt.isPresent()) {
            Chat chat = opt.get();
            if (chat.getUsers().contains(regUser)) {
                chat.setChat_name(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You are not member of this group");
        }

        throw new ChatException("Chat not found with id: " + chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws Exception {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);

        if (opt.isPresent()) {
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            } else if (chat.getUsers().contains(reqUser)) {
                if (user.getId().equals(reqUser.getId())) {
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            }
            throw new UserException("You cannot remove another user");

        }
        throw new ChatException("Chat not found with id: " + chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);

        if (opt.isPresent()) {
            Chat chat = opt.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
