package com.connect.backend.forms;

import com.connect.backend.domian.seprofile.AboutMe.ServiceAvailability;

public class AboutMeForms{
	
   private String phoneNumber;
	
	
	private String literalAddress;
	
	private String postCode;
	
	//maybe pin point location coordinates in future
	
	private String profileDescription;
	
	private String startTime;
	
	private String finishTime;
	
	private ServiceAvailability serviceAvailability;
	
public AboutMeForms(){};
	
	
	public String getPhoneNumber(){return phoneNumber;}
	public String getLiteralAddress(){return literalAddress;}
	public String getPostCode(){return  postCode;}
	public String getProfileDescription(){return profileDescription;}
	public String getStartTime(){return startTime;}
	public String getFinishTime(){return finishTime;}
    public ServiceAvailability getServiceAvailability(){return serviceAvailability;}


	
	
	
}