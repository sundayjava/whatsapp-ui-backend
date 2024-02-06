package com.davo.backend.service;

import com.davo.backend.dto.request.GroupChatRequest;
import com.davo.backend.exception.ChatException;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.Chat;
import com.davo.backend.model.User;

import java.util.List;

public interface ChatService {
    public Chat createChat(User reqUserId, Integer userId2, boolean isGroup) throws Exception;

    public Chat findChatById(Integer chatId) throws ChatException;

    public List<Chat> findAllChatByUserId(Integer userId) throws Exception;

    public Chat createGroup(GroupChatRequest req, User reqUser) throws Exception;

    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws Exception;

    public Chat renameGroup(Integer chatId, String groupName, User regUser) throws UserException, ChatException;

    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws Exception;

    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException;
}
