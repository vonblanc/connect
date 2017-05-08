package com.connect.backend.authenticators;
import com.connect.backend.users.AuthUser;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.api.server.spi.response.NotFoundException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;


public class FireAuth implements Authenticator{
	
	public static final Logger logger=Logger.getLogger(FireAuth.class.getName());
	private FirebaseToken firebaseToken;
	
	User user;

	
	static {
        try {
            
       
        
        
        	
        	FileInputStream inputStream = new FileInputStream(
                    new File("WEB-INF/confrencecenter-firebase-adminsdk-c7b4h-07ab89e0da.json"));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(inputStream))
                    .setDatabaseUrl("https://confrencecenter.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            
        	
        	
            
            logger.log(Level.SEVERE, "Successfully initiaalized Firebase app");


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
   
	}

	
	@Override
    public User authenticate(HttpServletRequest httpServletRequest) {

        //get token
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        
        
        
        //verify
        if(authorizationHeader != null) {
            Task<FirebaseToken> task = FirebaseAuth.getInstance().verifyIdToken(authorizationHeader.replace("Bearer ", "")).addOnFailureListener(new OnFailureListener(){

						@Override
						public void onFailure(Exception e) {
				            
							logger.log(Level.SEVERE, "Verification of firebase token faailed but aat least it go to this stage");
							
							logger.log(Level.SEVERE, e.toString(), e);
							
						}
            			
            			
            			
            			
            			
            			
            			
            		}).addOnCompleteListener(new OnCompleteListener<FirebaseToken>(){

						@Override
						public void onComplete(Task<FirebaseToken> arg0) {
							// TODO Auto-generated method stub
							firebaseToken=arg0.getResult();
							logger.log(Level.SEVERE, "Verification of firebase token Successful");
							logger.log(Level.SEVERE, "This is the users email from the firebase token "+firebaseToken.getEmail());

							user=new AuthUser(firebaseToken.getUid(), firebaseToken.getEmail(), firebaseToken.getName());
			                logger.log(Level.SEVERE, "User created is "+ user.toString());

							
						}});
            
			
            
            try{Tasks.await(task);}
            catch(ExecutionException e){logger.log(Level.SEVERE, e.toString(), e);}
            catch (InterruptedException e) {logger.log(Level.SEVERE, e.toString(), e);}
           
            
        }
        
      

        
        
        
        return user;
}
	
	
	
}