package com.connect.backend.users;

import com.google.api.server.spi.auth.common.User;

public class AuthUser extends User{
	
	private String name;
	
	public AuthUser(final String uid,String email, String name)
	{
		super(uid,email);
		
		this.name=name;
	}

	
	public String getId()
	{  
		return super.getId();
		
	}
	
	public String getEmail()
	{
		return super.getEmail();
	}
	
	public String getName()
	{
		return this.name;
	}
	
}
