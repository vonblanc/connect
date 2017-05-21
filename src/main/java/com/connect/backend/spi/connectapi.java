package com.connect.backend.spi;
import static com.connect.backend.service.OfyService.ofy;

import java.text.ParseException;

import com.connect.backend.authenticators.FireAuth;
import com.connect.backend.domian.Hello;
import com.connect.backend.domian.seprofile.AboutMe;
import com.connect.backend.domian.seprofile.Profile;
import com.connect.backend.forms.AboutMeForms;
import com.connect.backend.users.AuthUser;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.connect.backend.Constants;
import com.connect.backend.domian.appusers.ConnectUser;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;


@Api(
		name="connectapi",
		version="v1",
		authenticators={FireAuth.class},
		clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
		description="Test REST api for the connect app"
		)


public class connectapi{

	
	
	
	public ConnectUser getConnectUserFromUser(User user) throws UnauthorizedException{
		
    if(user==null) throw new UnauthorizedException("There is no user returned");
		
		//String name=user.getName();
	
		//Hello hello=new Hello(Message);
		 String uName=((AuthUser)user).getName();
		 String uId=((AuthUser)user).getId();
		 String uEmail=((AuthUser)user).getEmail();
		
		 ConnectUser connectUser=new ConnectUser(uName,uEmail, uId);
		 
		return connectUser;
	}
	
	
	
	@ApiMethod(name="decMessage",path="test",httpMethod=HttpMethod.GET)
	public ConnectUser decMessage(User user,@Named("Message") final String Message) throws UnauthorizedException
	{      
		 ConnectUser connectUser= getConnectUserFromUser(user);
		 ofy().save().entity(connectUser).now();
		 
		 
		
		return connectUser ;
		
	}
	
	
	
	@ApiMethod(name="createProfile",path="createProfile",httpMethod=HttpMethod.POST)
	public Profile createProfile(User user,AboutMeForms aboutmeforms) throws ParseException, UnauthorizedException
	{
		 ConnectUser connectUser=getConnectUserFromUser(user);  //method that returns Connect user from authorized user 
		 
		 String userId=connectUser.getId();
		 
		 
		 
		 Key<ConnectUser> userKey=Key.create(ConnectUser.class,userId); //create user key 
		 
		 
		 
		 final Key<Profile> profileKey=ObjectifyService.factory().allocateId(userKey,Profile.class);  //creates key from the user key and profile class(id also created)
		 
		 
		 
		 final long profileId=profileKey.getId();   //reference to the created profile ID
		 
		 
		 String aboutMePhoneNumber=aboutmeforms.getPhoneNumber(); //get essential phone number from aboutme forms
		 
		 
		 
		 String aboutMeLiteralAddress=aboutmeforms.getLiteralAddress(); //get essential address from aboutmeforms
		 
		 
		 String aboutMePostCode=aboutmeforms.getPostCode();   //get essential  postcode from aboutmeforms
		 
		 
		 AboutMe aboutMe=new AboutMe(aboutMePhoneNumber,aboutMeLiteralAddress,aboutMePostCode); //create about me embeded class from details above
		 
		 
		 aboutMe.update(aboutmeforms); //update the class with the rest of details from form 
		 
		 
		 Profile profile=new Profile(profileId,userId,aboutMe);
		 
		 ofy().save().entities(connectUser,profile);
		 
		 
		
		return profile;
	}
	
	
	
	
	
	
	
}