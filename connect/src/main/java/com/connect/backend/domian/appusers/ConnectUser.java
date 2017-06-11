
package com.connect.backend.domian.appusers;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ConnectUser{
	
   @Id	
    private String Id;
	
	private String email;
	
	
	private String name;
	
	
	public ConnectUser(){}
	
	public ConnectUser(String name, String email, String ID)
	{
		this.Id=ID;
		this.name=name;
		this.email=email;
		
	}
	
	
	public String getName()
	{return name;}
	
	public String getEmail()
	{return email;}
	
	public String getId()
	{return Id; }
	
	
	
}