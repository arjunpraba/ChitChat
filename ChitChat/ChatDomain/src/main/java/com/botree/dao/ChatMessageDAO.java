package com.botree.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.botree.model.ChatMessage;
import com.botree.model.User;
import java.util.List;

@Repository
public class ChatMessageDAO {
    
    @Autowired
    SessionFactory sf;
    
    public void save(ChatMessage msg) {
        var session = sf.openSession();
        var trx = session.beginTransaction();
        session.save(msg);
        trx.commit();
        session.close();
    }
    
    public List<User> getContactList(User user) {
        var session = sf.openSession();
        var query = session.createQuery(
            "SELECT u FROM User u WHERE u.id IN " +
            "(SELECT DISTINCT c.sender.id FROM ChatMessage c WHERE c.receiver.id = :userId) " +
            "OR u.id IN " +
            "(SELECT DISTINCT c.receiver.id FROM ChatMessage c WHERE c.sender.id = :userId)", User.class);
        query.setParameter("userId", user.getId());
        var contacts = query.list();
        session.close();
        return contacts;
    }
    
    public void deleteChat(User user1, User user2) {
        var session = sf.openSession();
        var trx = session.beginTransaction();
        var query = session.createQuery(
            "DELETE FROM ChatMessage c WHERE (c.sender = :user1 AND c.receiver = :user2) OR (c.sender = :user2 AND c.receiver = :user1)");
        query.setParameter("user1", user1);
        query.setParameter("user2", user2);
        query.executeUpdate();
        trx.commit();
        session.close();
    }
    
    public List<ChatMessage> getMessages(User sender, User receiver) {
        var session = sf.openSession();
        var query = session.createQuery(
            "FROM ChatMessage WHERE (sender = :sender AND receiver = :receiver) OR (sender = :receiver AND receiver = :sender) ORDER BY sentTime", 
            ChatMessage.class);
        query.setParameter("sender", sender);
        query.setParameter("receiver", receiver);
        var messages = query.list();
        session.close();
        return messages;
    }
}
