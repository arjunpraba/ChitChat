package com.botree.bean;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.botree.dao.UserDAO;
import com.botree.model.User;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Component
@Scope("request")
public class RegisterBean {
    
    @Autowired
    private UserDAO userDAO;
    
    private User user = new User();
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void register() throws IOException {
        userDAO.register(user);
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage("Registration successful!"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
}
