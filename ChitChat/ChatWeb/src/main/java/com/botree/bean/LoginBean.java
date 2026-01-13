package com.botree.bean;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import com.botree.dao.UserDAO;
import com.botree.model.User;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Component("loginBean")
@RequestScope
public class LoginBean {
    
    @Autowired
    private UserDAO userDAO;
    
    private User user = new User();
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void login() throws IOException {
        if(userDAO.authenticate(user)) {
            User loggedInUser = userDAO.getUser(user.getUsername());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", loggedInUser);
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Invalid credentials"));
        }
    }
    
    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
}
