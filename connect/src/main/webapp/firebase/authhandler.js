
$("document").ready(function(){
	
	  var sendToken;
	  
	
	
	
		
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
	
	button.click(function(){
		var User=firebase.auth().currentUser;
		
		handleSignOutUser(User);
		
	});
	
var button_token=$("#send_token").find("button");

button_token.click(function(){
	
	var request={
	  		   "Message":"Hello it's me",
	  		   
	     };
	

	
	
	firebase.auth().currentUser.getToken().then(function(token){
		
		console.log(token);
		console.log("About calling ajax to send token");
		sendTokenToConnect(token,request);
		
	},function(error){console.log(error);});
	
});

var handleSignedInUser=function(user)
{    
	
	$(".hidden").css("display","block");

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

var sendTokenToConnect= function(sendToken,request)
{
	$.ajax({

        url: 'http://localhost:8080/_ah/api/connectapi/v1/test',

        type: 'GET',

        dataType: 'json',

        data: request,

        success: function (data, textStatus, xhr) {

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


	

});