package com.botree.bean;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import com.botree.dao.UserDAO;
import com.botree.model.ChatMessage;
import com.botree.model.User;
import com.botree.service.ChatService;
import jakarta.faces.context.FacesContext;

@Component("chatBean")
@SessionScope
public class ChatBean implements Serializable {
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UserDAO userDAO;
    
    private User selectedContact;
    private String newContactUsername;
    private String message;
    private List<ChatMessage> messages;
    private List<User> contacts;
    private int previousMessageCount = 0;
    
    public void init() {
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if(currentUser != null) {
            loadContacts();
        }
    }
    
    public User getSelectedContact() { return selectedContact; }
    public void setSelectedContact(User selectedContact) { 
        this.selectedContact = selectedContact;
        previousMessageCount = 0;
        loadMessages();
    }
    
    public String getNewContactUsername() { return newContactUsername; }
    public void setNewContactUsername(String newContactUsername) { this.newContactUsername = newContactUsername; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public List<ChatMessage> getMessages() { return messages; }
    public List<User> getContacts() { return contacts; }
    
    public void loadContacts() {
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if(currentUser != null) {
            User refreshedUser = userDAO.getUser(currentUser.getUsername());
            if(refreshedUser != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", refreshedUser);
                contacts = chatService.getContactList(refreshedUser);
            }
        }
    }
    
    public void startNewChat() {
        if(newContactUsername != null && !newContactUsername.isEmpty()) {
            selectedContact = userDAO.getUser(newContactUsername);
            newContactUsername = "";
            loadMessages();
        }
    }
    
    public void send() {
        User sender = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        User refreshedSender = userDAO.getUser(sender.getUsername());
        
        if(refreshedSender != null && selectedContact != null && message != null && !message.isEmpty()) {
            ChatMessage msg = new ChatMessage();
            msg.setSender(refreshedSender);
            msg.setReceiver(selectedContact);
            msg.setMessage(message);
            chatService.sendMessage(msg);
            message = "";
            loadMessages();
            loadContacts();
        }
    }
    
    public void loadMessages() {
        User sender = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if(sender != null && selectedContact != null) {
            User refreshedSender = userDAO.getUser(sender.getUsername());
            User refreshedContact = userDAO.getUser(selectedContact.getUsername());
            if(refreshedSender != null && refreshedContact != null) {
                List<ChatMessage> newMessages = chatService.getMessages(refreshedSender, refreshedContact);
                if(messages != null && newMessages.size() > previousMessageCount) {
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new jakarta.faces.application.FacesMessage(
                            jakarta.faces.application.FacesMessage.SEVERITY_INFO,
                            "New Message", 
                            "You have a new message from " + selectedContact.getFirstName()));
                }
                messages = newMessages;
                previousMessageCount = messages.size();
            }
        }
    }
    
    public void deleteChat() {
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if(currentUser != null && selectedContact != null) {
            User refreshedUser = userDAO.getUser(currentUser.getUsername());
            User refreshedContact = userDAO.getUser(selectedContact.getUsername());
            if(refreshedUser != null && refreshedContact != null) {
                chatService.deleteChat(refreshedUser, refreshedContact);
                selectedContact = null;
                messages = null;
                loadContacts();
            }
        }
    }
}
