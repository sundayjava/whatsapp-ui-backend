package com.davo.backend.service;

import com.davo.backend.dto.request.SendMessageRequest;
import com.davo.backend.exception.ChatException;
import com.davo.backend.exception.MessageException;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.Message;
import com.davo.backend.model.User;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SendMessageRequest req) throws Exception;
    public List<Message> getChatsMessages(Integer chatId, User regUser) throws ChatException, UserException;
    public Message findMessageById(Integer messageId) throws MessageException;
    public void deleteMessage(Integer messageId, User regUser) throws MessageException, UserException;
}
