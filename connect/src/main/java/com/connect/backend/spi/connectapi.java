package com.connect.backend.spi;

import com.connect.backend.authenticators.FireAuth;
import com.connect.backend.domian.Hello;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.connect.backend.Constants;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;


@Api(
		name="connectapi",
		version="v1",
		authenticators={FireAuth.class},
		clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
		description="Test REST api for the connect app"
		)


public class connectapi{

	
	
	@ApiMethod(name="decMessage",path="test",httpMethod=HttpMethod.GET)
	public Hello decMessage(User user,@Named("Message") final String Message) throws UnauthorizedException
	{
		if(user==null) throw new UnauthorizedException("There is no user returned");
		
		
		Hello hello=new Hello(Message);
		
		
		
		
		return hello;
		
	}
	
	
	
	
	
	
	
	
	
	
	
}