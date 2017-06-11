package com.connect.backend.domian.seprofile;

import com.connect.backend.domian.appusers.ConnectUser;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Profile{
	

@Id
private long ID;               //id for  datastore purposes to be allocated automatically


@Parent
@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
private Key<ConnectUser> connectUserKey;    //the parent key of profile which is connectUser


private AboutMe aboutMe;      //add About Me as an Embeded Entity class x1  //remember to index!!!!!

private String webSafeProfileKey;


public Profile(){}
	
public Profile(long id,String connectUserId, AboutMe aboutMe){       //construct profile from about me class
	
	this.ID=id;     //assign get allocated ID from allocated key and store
	
	this.connectUserKey=Key.create(ConnectUser.class,connectUserId);  //create parentkey and store
	
	this.aboutMe=aboutMe;  //set about me
	
	this.webSafeProfileKey=Key.create(this.connectUserKey,Profile.class,this.ID).getString(); 
}
	


public AboutMe getAboutMe()
{
	                            //return the about me class for on the fly info requests
	return this.aboutMe;
	
}

public void updateAboutMe(AboutMe aboutMe)
{
	this.aboutMe=aboutMe;
}


@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
public Key<ConnectUser> getConnectUserKey()
{                                                 //get the key for the parent User
	return this.connectUserKey;
}

public String getWebSafeProfileKey()
{
	   return this.webSafeProfileKey; //return web safe version of key
}


public long getId(){return this.ID;}
	
	
	
	
	
}