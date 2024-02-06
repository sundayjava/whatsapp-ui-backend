package com.davo.backend.service.serviceimpl;

import com.davo.backend.dto.request.SendMessageRequest;
import com.davo.backend.exception.ChatException;
import com.davo.backend.exception.MessageException;
import com.davo.backend.exception.UserException;
import com.davo.backend.model.Chat;
import com.davo.backend.model.Message;
import com.davo.backend.model.User;
import com.davo.backend.repository.MessageRepository;
import com.davo.backend.service.ChatService;
import com.davo.backend.service.MessageService;
import com.davo.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;

    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Message sendMessage(SendMessageRequest req) throws Exception {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);

        if(!chat.getUsers().contains(reqUser)){
            throw new UserException("You are not related to this chat "+chat.getId());
        }

        return messageRepository.findByChatId(chat.getId());
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> opt = messageRepository.findById(messageId);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new MessageException("Message not found with id "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);

        if (message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
        }

        throw new UserException("You cant delete this message "+reqUser.getFull_name());
    }
}
