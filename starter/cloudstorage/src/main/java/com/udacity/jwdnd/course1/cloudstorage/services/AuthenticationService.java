package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService implements AuthenticationProvider {

    private UserMapper userMapper;
    private HashService hashService;

    public AuthenticationService(UserMapper userMapper, HashService hashService) {
	this.userMapper = userMapper;
	this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	String username = authentication.getName();
	String password = authentication.getCredentials().toString();
	User user = userMapper.getUser(username);
	if (user != null) {
	    String encodedSalt = user.getSalt();
	    String hashedPassword = hashService.getHashedValue(password, encodedSalt);
	    if (user.getPassword().equals(hashedPassword)) {
		// successful authentication
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
		grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		User loggedInUser = new User(user.getUserid(), user.getUsername(), user.getSalt(), user.getPassword(), user.getFirstName(), user.getLastName());
		return new UsernamePasswordAuthenticationToken(loggedInUser, "", grantedAuthorityList);
	    }
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
	return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
