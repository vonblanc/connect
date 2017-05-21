package com.connect.backend.domian.seprofile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.connect.backend.forms.AboutMeForms;
import com.google.appengine.repackaged.com.google.api.client.util.Preconditions;



public class AboutMe{
	public static final Logger logger=Logger.getLogger(AboutMe.class.getName());
    
	
	
	private String phoneNumber;
	
	
	private String literalAddress;
	
	private String postCode;
	
	//maybe pin point location coordinates in future
	
	private StringBuilder profileDescription;
	
	private Date startTime;
	
	private Date finishTime;
	
	private String profilePicUrl;
	
	
	public AboutMe(){};
	
	public AboutMe(String phoneNumber, String literalAddress, String postCode)
	{
		Preconditions.checkNotNull(phoneNumber, "you have to give a phone number");
		Preconditions.checkNotNull(literalAddress, "you have to give a litreal address");
		Preconditions.checkNotNull(postCode, "you have to give a postcode");
		
		this.phoneNumber=phoneNumber;
		this.literalAddress=literalAddress;
		this.postCode=postCode;

		
		
	}
	
	
	
	public void update(AboutMeForms aboutmeforms) throws ParseException
	{ 
	   
	   
	  	
	  	if(!(this.literalAddress.equals(aboutmeforms.getLiteralAddress()))){this.literalAddress=aboutmeforms.getLiteralAddress();}
        if(!(this.phoneNumber.equals(aboutmeforms.getPhoneNumber()))){this.phoneNumber=aboutmeforms.getPhoneNumber();}    //  check if the data in the forms equals to data stored if not set them
        if(!(this.postCode.equals(aboutmeforms.getPostCode()))){this.postCode=aboutmeforms.getPostCode();}


       // try{
	  	
        String strtTime=aboutmeforms.getStartTime();
	  	String finshTime=aboutmeforms.getFinishTime();         //get starttime and finishtime strings from forms 
	  	
        
	  	if(strtTime!=null&&finshTime!=null)
	  	{
	  	 Date strtTimeDate=new SimpleDateFormat("HH:mm").parse(strtTime);  	  	  //if both start time and finish time not null parse string and create date instance 
		
	     Date finishTimeDate=new SimpleDateFormat("HH:mm").parse(finshTime);
	  	//}
	  	
	  	//catch(Exception e) {logger.log(Level.SEVERE, e.toString(), e);}
	  	
        startTime=strtTimeDate;
        finishTime=finishTimeDate;
        
        
	}  
	  	
	  	else
	  	{
	  		startTime=null;
	        finishTime=null;
	  	}
	
	}
	
	public String getPhoneNumber(){return phoneNumber;}
	
	public String getLiteralAddress(){return literalAddress;}
	
	public String getPostCode(){return postCode;}
	
	public Date getStartTime() throws ParseException{return startTime==null ? new SimpleDateFormat("HH:mm:ss").parse("09:00:00"):startTime;}
	
	public Date getfinishTime() throws ParseException{return finishTime==null ? new SimpleDateFormat("HH:mm:ss").parse("17:00:00"):finishTime;}
	
	

	public void setProfilePicUrl(String profilePicUrl){this.profilePicUrl=profilePicUrl;}
	
	public String getProfilePicUrl(){return this.profilePicUrl;}
	
	
}