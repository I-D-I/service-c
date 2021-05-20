package es.vn.sb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import brave.Span;
import brave.Tracer;
import es.vn.sb.model.User;

@Service
public class UserServiceImpl implements UserService {

 
    @Autowired
	Tracer tracer;
    
	@Override
	public User getUser() {
		User user = new User(1,"Antonio");
		return user;
	}

	public List<User> getUsers() {
		User user = new User(1,"Antonio");
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		Span span = tracer.currentSpan();
		span.tag("service", "entrada al servicio");
		span.annotate(String.format("Generando el user %s", userList.toArray().toString()));
		return userList;
	}
    
}   
