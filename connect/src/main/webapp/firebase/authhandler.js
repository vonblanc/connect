
$("document").ready(function(){
	
	  var sendToken;
	  
	  var webSafeProfileKey="agkke2FwcC5pZH1yOgsSC0Nvbm5lY3RVc2VyIhwwR0x6REN0SllpZFhQOWZld0pVMnNKRHpieW8xDAsSB1Byb2ZpbGUYAQw";
		
	  var currentProfile;
	   
	  var createProfilePath="createProfile";
	  
	  var updateProfilePath="updateProfile";
	  
	  var createServicePath="createService";
	  
	   var request={
	  		   "Message":"Hello it's me",
	  		   
	     };
	
   var request_profileCreate={
		   
		"phoneNumber":"07761006970" ,
		
		"literalAddress":"14 Booth close thamesmead",
		
		"postCode":"SE28 8BW",
		
		"startTime":"07:00",
		
     	 "finishTime":"20:00",
     	 
     	 "serviceAvailability":"HOME",
		
	
		   
   };
   
	  
   var request_profileUpdate={
		   
		   	
		   
			"phoneNumber":"07761006970" ,
			
			"literalAddress":"TEST",
			
			"postCode":"TEST",
			
			"startTime":"07:00",
			
	     	 "finishTime":"20:00",
	     	 
	     	 "serviceAvailability":"HOME",
	     	 
	     	
		
			   
	   };
   
   var service_Offered={
		   
		  "serviceName":"Eye Waxing",
		  "servicePrice": 20.00,
		   "serviceCategory":"Waxing",
		  "servicedescription":"",
		   
		   
		   
		   
   };	  
	
	  var handleSignedInUser=function(user)
	  {    
	  	
	  	$(".hidden").css("display","block");  //select all hidden classes

	  	var name=$("#personal_dets").find("#name").text(user.displayName);
	  	console.log("display name "+name.text());   
	  	
	  	var email=$("#personal_dets").find("#email").text(user.email);
	  	console.log("display name "+email.text());
	  	
	  	$("body").find("#loading").addClass("hidden");
	      

	  }
	  	
	  var handleSignOutUser=function(user)
	  {
	  	
	  firebase.auth().signOut().then(function(){
	  	$("h3").text("Signed Out");
	  	$(".hidden").css("display","none");
	  	console.log("Sign-Out Sucessful");
	  	
	  }).catch(function(error){
	  	console.log("there is an error with signout");
	  	console.log(error.message);
	  	
	  });

	  }
	  
	  var handleServiceForm=function()
	  {
		  var form=new Object();

		  var formButton=$("#send");
                                                     //get object from form entries
		  var myForm=$("#serviceForm");

		  
		  


			  form.serviceName=myForm.find("#serviceName").val();
			  form.servicePrice=myForm.find("#servicePrice").val();
			  form.serviceCategory=myForm.find("#serviceCategory").val();
			  form.serviceDescription=myForm.find("#serviceDescription").val();
			  if(form.name==""||form.price==""||form.category=="") {
				  
				  alert("Please enter values for name, price and category");
				   return null;  
			  
			  }
			   
			   
		  
		  return form;
		  
	  }
	  
	  
	  var sendTokenToConnectUpdate= function(sendToken,request,webSafeProfileKey,path)
	  {
	  	$.ajax({

	          url: 'http://localhost:8080/_ah/api/connectapi/v1/'+path+'?webSafeProfileKey='+ webSafeProfileKey,

	          type: 'POST',
	          
	          cache:false,

	          dataType: 'json',

	          data:  request,

	          success: function (data, textStatus, xhr) {
                  //currentProfile=data;
	              //console.log(currentProfile);
	        	  console.log(data);
	          },
	          
	          headers:{
	       	   'Authorization': 'Bearer '+sendToken
	       	   
	       	   
	          },

	          error: function (xhr, textStatus, errorThrown) {
	              
	       	   
	              console.log('Error in Operation');
	              console.log(textStatus + "Error thrown:"+ errorThrown);
	              
	          }

	      });
	  	   
	  	   
	  }
	  
	  
	  var sendTokenToConnectCreate= function(sendToken,request,path)
	  {
	  	$.ajax({

	          url: 'http://localhost:8080/_ah/api/connectapi/v1/'+path,

	          type: 'POST',
	          
	          cache:false,

	          dataType: 'json',

	          data:  request,

	          success: function (data, textStatus, xhr) {
                 // currentProfile=data;                              add or remove currentProfile creation
	              //console.log(currentProfile);
	        	  console.log(data);
	          },
	          
	          headers:{
	       	   'Authorization': 'Bearer '+sendToken
	       	   
	       	   
	          },

	          error: function (xhr, textStatus, errorThrown) {
	              
	       	   
	              console.log('Error in Operation');
	              console.log(textStatus + "Error thrown:"+ errorThrown);
	              
	          }

	      });
	  	   
	  	   
	  }	  
	  
	  
	  
	  
	  
	  
	  
	
		
		firebase.auth().onAuthStateChanged(function(user) {
			  // The observer is also triggered when the user's token has expired and is
			  // automatically refreshed. In that case, the user hasn't changed so we should
			  // not update the UI.
              currentUid=user.uid;
			  if (user && user.uid == currentUid) {
				  handleSignedInUser(user);

              user.getToken().then(function(accessToken){
					  
					  
     				  console.log("ACCESS TOKEN BABY "+accessToken);

					  
				  });
			    return;
			  }
			  
			  if(user){
				  
				  handleSignedInUser(user);
				  user.getToken().then(function(accessToken){
					  
     				  console.log("ACCESS TOKEN BABY "+accessToken);
					  
					  
				  });
			  }
			  
			  else{console.log("user signed out yeah!!");}
			  
			});                  //, function(error){console.log(error);});
		
		
	
	var button=$("#sign_out").find("button");
	
	button.click(function(){            //sign out button event listener  
		var User=firebase.auth().currentUser;
		
		handleSignOutUser(User);
		
	});
	
	
	
	
	
	
	
	
	
var button_token=$("#send_token").find("button"); //find the button in the html doc 

button_token.click(function(){          //handle create profile button click and call 
	
	
   
  
	
	firebase.auth().currentUser.getToken().then(function(token){
		
		console.log(token);
		console.log("About calling ajax to send token");
		sendTokenToConnectCreate(token,request_profileCreate,createProfilePath);   //ajax create function 
	},function(error){console.log(error);});
	
});




var updateProfileButton=$("#updateProfile").find("button")

updateProfileButton.click(function(){
	
	if(currentProfile==null) {console.log("There is no profile created yet updating so getting the webSafeKey might not work");}
	
	else{console.log("This is the current profile "+currentProfile);}

	firebase.auth().currentUser.getToken().then(function(token){
		
		console.log(token);
		console.log("About calling ajax to send token");
		
		sendTokenToConnectUpdate(token,request_profileUpdate,currentProfile.webSafeProfileKey,updateProfilePath);
		 
	},function(error){console.log(error);});
	
	
	
	
});


var addServiceButton=$("#addService").find("button");      //find button in the add service div tag
var myForm=$("#serviceForm");                              //find my form

addServiceButton.click(function(){myForm.toggleClass("hidden_form");}); //display service creation formon click 

//http://localhost:8080/_ah/api/connectapi/v1/createProfile

myForm.submit(function(event){
    var serviceObject=handleServiceForm();   //get object from service form
	
    
    if(currentProfile==null) {console.log("There is no profile created yet updating so getting the webSafeKey might not work");} 
	
	else{console.log("This is the current profile "+currentProfile);}

	firebase.auth().currentUser.getToken().then(function(token){
		                                                                                //get token from current user(firebase)
		console.log("About calling ajax to send token");
		
		sendTokenToConnectCreate(token,serviceObject,createServicePath);               //call the ajax create function to create a service 
		
	},function(error){console.log(error);});
	
    
    
    
	event.preventDefault();                                                            // make sure forms  default is stopped
	 
});





	

});