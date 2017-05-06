
$("document").ready(function(){
	
	//var currentUid;
	
	
	
	
		
		firebase.auth().onAuthStateChanged(function(user) {
			  // The observer is also triggered when the user's token has expired and is
			  // automatically refreshed. In that case, the user hasn't changed so we should
			  // not update the UI.
              currentUid=user.uid;
			  if (user && user.uid == currentUid) {
				  handleSignedInUser(user);

				  console.log("got here in no need func")
			    return;
			  }
			  
			  if(user){
				  
				  handleSignedInUser(user);
				  user.getToken().then(function(accessToken){
					  
					  console.log(accessToken);
					  
					  
				  });
			  }
			  
			  else{console.log("user signed out yeah!!");}
			  
			});                  //, function(error){console.log(error);});
		
		
	
	var button=$("#sign_out").find("button");
	
	button.click(function(){
		var User=firebase.auth().currentUser;
		
		handleSignOutUser(User);
		
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




	

});