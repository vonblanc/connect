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
	
	private User user;

	
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
            Task<FirebaseToken> task = FirebaseAuth.getInstance().verifyIdToken(authorizationHeader.replace("Bearer ", ""));
            		
          
            try{Tasks.await(task);}
            catch(ExecutionException e){logger.log(Level.SEVERE, e.toString(), e);}
            catch (InterruptedException e) {logger.log(Level.SEVERE, e.toString(), e);}
           
            
            firebaseToken=task.getResult();
            user=new AuthUser(firebaseToken.getUid(), firebaseToken.getEmail(), firebaseToken.getName()); 
        }
        
      

        
        
        
        return user;
}
	
	
	
}