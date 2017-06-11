package com.connect.backend.spi;
import static com.connect.backend.service.OfyService.ofy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.connect.backend.authenticators.FireAuth;
import com.connect.backend.domian.Hello;
import com.connect.backend.domian.seprofile.AboutMe;
import com.connect.backend.domian.seprofile.Profile;
import com.connect.backend.domian.seprofile.ServiceOffered;
import com.connect.backend.domian.utilities.queries.ConnectServiceQuery;
import com.connect.backend.forms.AboutMeForms;
import com.connect.backend.forms.ServiceOfferedForm;
import com.connect.backend.users.AuthUser;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.connect.backend.Constants;
import com.connect.backend.domian.appusers.ConnectUser;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;


@Api(
		name="connectapi",
		version="v1",
		authenticators={FireAuth.class},
		clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
		description="Test REST api for the connect app"
		)


public class connectapi{

	
	public static final Logger logger=Logger.getLogger(FireAuth.class.getName());
	
	public ConnectUser getConnectUserFromUser(User user) throws UnauthorizedException{
		
    if(user==null) throw new UnauthorizedException("There is no user returned");
		
		//String name=user.getName();                                            ///creates a connect user class from the injected authorization user class 
	
		//Hello hello=new Hello(Message);
		 String uName=((AuthUser)user).getName();
		 String uId=((AuthUser)user).getId();
		 String uEmail=((AuthUser)user).getEmail();
		
		 ConnectUser connectUser=new ConnectUser(uName,uEmail, uId);
		 
		return connectUser;
	}
	
	
	public Profile getProfileFromConnectUser(ConnectUser user) throws NotFoundException
	{
		Key<ConnectUser> connectUserKey=Key.create(ConnectUser.class,user.getId());       
		                                                                                   //get profile from ConnectUser only one profile allowed per user 
		Query<Profile> query=ofy().load().type(Profile.class).ancestor(connectUserKey);
		
		
		
		List<Profile> profileList=query.list();
		
		Profile profile=profileList.get(0);
		
		if(profile==null) throw new NotFoundException("Profile does not  exist!");
	
		return profile;
	}
	
	
	
	@ApiMethod(name="decMessage",path="test",httpMethod=HttpMethod.GET)
	public ConnectUser decMessage(User user,@Named("Message") final String Message) throws UnauthorizedException
	{      
		 ConnectUser connectUser= getConnectUserFromUser(user);
		 ofy().save().entity(connectUser).now();
		 
		 
		
		return connectUser ;
		
	}
	
	
	
	@ApiMethod(name="createProfile",path="createProfile",httpMethod=HttpMethod.POST)                              //method to be used just once!!!!
	public Profile createProfile(User user,AboutMeForms aboutmeforms) throws ParseException, UnauthorizedException
	{
		
		
		
		 ConnectUser connectUser=getConnectUserFromUser(user);  //method that returns Connect user from authorized user 
		 
		 String userId=connectUser.getId();
		 
		 
		 
		 Key<ConnectUser> userKey=Key.create(ConnectUser.class,userId); //create user key 
		 
		 Query<Profile> query=ofy().load().type(Profile.class).ancestor(userKey);  //query for all the Profiles associated with a particular User(Parent Relationship)
		 
		 List<Profile> profilesForOneUser=query.list();  //Reference to list of profiles
				 
		 int listSize=profilesForOneUser.size();          //store number of profiles 
		 
		 if(listSize>=1) return profilesForOneUser.get(0); //return the first profile if there's already a profile created for a particular user 
		 

		 
		 
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
	
	
	
	@ApiMethod(name="updateProfile",path="updateProfile",httpMethod=HttpMethod.POST)
	
	public Profile updateProfile(User user, @Named("webSafeProfileKey")String webSafeProfileKey, AboutMeForms aboutmeforms) throws UnauthorizedException, NotFoundException, ParseException, ForbiddenException
	{
		
		
		
		
		ConnectUser connectUser=getConnectUserFromUser(user);
		
		
		Key<ConnectUser> connectUserKey=Key.create(ConnectUser.class,connectUser.getId());
		
					
		 Key<Profile> profileKey=Key.create(webSafeProfileKey);
		
		 if(!(profileKey.getParent().equals(connectUserKey))) throw new ForbiddenException("this profile that you tried to load does not belong to the User");
		
		 
		 
		 Profile profile=ofy().load().key(profileKey).now();
		 
		
		 if(profile==null) throw new NotFoundException("Profile you are trying to load not found"); 
		
	     AboutMe aboutMe=profile.getAboutMe();
	     
	     aboutMe.update(aboutmeforms);
		
		 profile.updateAboutMe(aboutMe);
		 
		 ofy().save().entity(profile);
		 
		 
		
		return profile;
	}
	
	@ApiMethod(name="createService",path="createService",httpMethod=HttpMethod.POST)
	public ServiceOffered createService(User user, ServiceOfferedForm serviceOfferedForm) throws UnauthorizedException, NotFoundException
	{  
		ConnectUser connectUser=getConnectUserFromUser(user);
		
		
		Profile profile=getProfileFromConnectUser(connectUser);  //get profile from Key  
		
	   Key<Profile> profileKey=Key.create(profile.getWebSafeProfileKey());  //create profile Key
		 
	   Key<ServiceOffered> serviceOfferedKey= ObjectifyService.factory().allocateId(profileKey,ServiceOffered.class);  //creates key from the user key and profile class(id also created)
       
	   long serviceOfferedId=serviceOfferedKey.getId();
	   
	   ServiceOffered serviceOffered= new ServiceOffered(serviceOfferedId,serviceOfferedForm,profileKey.getString());  //create new ServiceOffered
		
	   ofy().save().entities(profile,serviceOffered);
		
		
		return serviceOffered;
	}
	
	
	@ApiMethod(name="updateService",path="updateService",httpMethod=HttpMethod.POST)
	public ServiceOffered updateService(ServiceOfferedForm serviceOfferedForm,@Named("webSafeServiceOfferedKey")String webSafeServiceOfferedKey) throws UnauthorizedException, NotFoundException
	{
		
		Key<ServiceOffered> serviceOfferedKey=Key.create(webSafeServiceOfferedKey);
		ServiceOffered serviceOffered=ofy().load().key(serviceOfferedKey).now();
		if(serviceOffered==null) throw new NotFoundException("Service is not found");
		
		serviceOffered.updateServiceOffered(serviceOfferedForm);
		
		ofy().save().entity(serviceOffered).now();
		
		return serviceOffered;
		
		
	}
	
	
	@ApiMethod(name="getSpecificService",path="getSpecificService",httpMethod=HttpMethod.GET)
	public List<ServiceOffered> getSpecificService(@Named("serviceName")String serviceName)
	{
		return ConnectServiceQuery.queryServicesUsingPattern(serviceName);
		
		
	}


	
	
}