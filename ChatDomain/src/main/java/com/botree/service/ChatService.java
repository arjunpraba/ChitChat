package com.botree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.botree.dao.ChatMessageDAO;
import com.botree.model.ChatMessage;
import com.botree.model.User;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    
    @Autowired
    private ChatMessageDAO chatMessageDAO;
    
    public void sendMessage(ChatMessage msg) {
        msg.setSentTime(LocalDateTime.now());
        chatMessageDAO.save(msg);
    }
    
    public List<ChatMessage> getMessages(User sender, User receiver) {
        return chatMessageDAO.getMessages(sender, receiver);
    }
    
    public List<User> getContactList(User user) {
        return chatMessageDAO.getContactList(user);
    }
    
    public void deleteChat(User user1, User user2) {
        chatMessageDAO.deleteChat(user1, user2);
    }
}
