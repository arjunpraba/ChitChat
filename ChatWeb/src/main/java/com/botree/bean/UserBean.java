package com.botree.bean;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import com.botree.dao.UserDAO;
import com.botree.model.User;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Component("userBean")
@RequestScope
public class UserBean {
	
	@Autowired
	UserDAO ud;
	
	private User user;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void register() throws IOException {
		User use = new User();
		use.setUsername(username);
		use.setPassword(password);
		use.setFirstName(firstName);
		use.setLastName(lastName);
		use.setEmail(email);
		use.setMobileNumber(mobileNumber);
		ud.register(use);
		FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
	}
	
	public void login() throws IOException {
		User use = new User(username, password);
		if(ud.authenticate(use)) {
			this.user = ud.getUser(username);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid credentials"));
		}
	}
}