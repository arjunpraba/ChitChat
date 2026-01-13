package com.botree.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.botree.model.User;

@Repository
public class UserDAO {
	@Autowired
	SessionFactory sf;

	public void register(User u) {
		var session = sf.openSession();
		var trx = session.beginTransaction();
		session.save(u);
		trx.commit();
		session.close();
	}

	public boolean authenticate(User u) {
		var session = sf.openSession();
		var query = session.createQuery("FROM User WHERE username = :username", User.class);
		query.setParameter("username", u.getUsername());
		var user = query.uniqueResult();
		session.close();
		if(user != null && user.getPassword().equals(u.getPassword())) {
			return true;
		}
		return false;
	}

	public User getUser(String username) {
		var session = sf.openSession();
		var query = session.createQuery("FROM User WHERE username = :username", User.class);
		query.setParameter("username", username);
		var user = query.uniqueResult();
		session.close();
		return user;
	}
}
