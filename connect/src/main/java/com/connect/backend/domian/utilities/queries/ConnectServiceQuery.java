package com.connect.backend.domian.utilities.queries;

import com.connect.backend.authenticators.FireAuth;
import com.connect.backend.domian.seprofile.ServiceOffered;
import com.googlecode.objectify.cmd.Query;
import static com.connect.backend.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ConnectServiceQuery{
	
	public static final Logger logger=Logger.getLogger(ConnectServiceQuery.class.getName());
	
	public ConnectServiceQuery(){}
	
     private static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
	
	
	public static String normalizeWhiteSpaces(String value)
	{   
		String valueAfter=value.trim().replaceAll(" +", " ");             //implementation of the normalizer function first remove any beginning and end white spaces then remove all inbetween excess whitespace
		
		valueAfter=valueAfter.toUpperCase();                              //set all characters to Upper case
		
		return valueAfter;
		
	}
	
	private static double searchInputForSimilarities(String original, String compare)
	{
		original=normalizeWhiteSpaces(original);
		compare=normalizeWhiteSpaces(compare);                      //convert search string to ALL CAPS and NO EXCESS whitespaces format used to store data  in datastore 

		
        double largestLength=0;  //initialize largest length
        
        if(original.length()>compare.length()) largestLength=original.length();  //set length to the largest string out of both being compared  
        if(original.length()<compare.length()) largestLength=compare.length();
        if(original.length()==compare.length())largestLength=original.length();
        
        
        double distance=minDistance(original,compare);    //use the edit distance algorithim to get the amount changed
        
        
        
       double diff=largestLength-distance;
        
       
        double percentageSimilarity=(diff/largestLength)*100;
         
        
        
		return percentageSimilarity;
		
	}
	
	
	
	public static List<ServiceOffered> queryService(String serviceName)   //product search algorithim
	{
		serviceName=normalizeWhiteSpaces(serviceName);                      //convert search string to ALL CAPS and NO EXCESS whitespaces format used to store data  in datastore 
		
		logger.log(Level.INFO,"This is the value you are about querying for "+ serviceName);
		
		Query<ServiceOffered> query=ofy().load().type(ServiceOffered.class);  //query all products-------test phase complete phase should have a list of profiles to query products from 
		
		List<ServiceOffered> allServices= query.list(); // give reference to query list 
		
     	List<ServiceOffered> filteredServices=new ArrayList<ServiceOffered>();    //createlist to hold filtered results 
		
		for(ServiceOffered iterate:allServices)                //iterate through queried results 
		{
		String comparedName=normalizeWhiteSpaces(iterate.getServiceName());        //normalize name and category to compare 
		String comparedCategory=normalizeWhiteSpaces(iterate.getServiceCategory());
		if(serviceName.indexOf(comparedName)!=-1||serviceName.indexOf(comparedCategory)!=-1) filteredServices.add(iterate); //check if service name is contained in the SERVICENAME field and SERVICECATEGORY field 
			
		else logger.log(Level.INFO,"Did  Not work the querying ");

		}
		
		
		return filteredServices;    //return filtered services
		
	}
	
	
	/* public static boolean queryServicesForInputPattern(String serviceString, String searchInput)
     {   
		 searchInput=normalizeWhiteSpaces(searchInput);
		 serviceString=normalizeWhiteSpaces(serviceString);     //normalize both inputs 
		 
		 
         String [] array;
         array=searchInput.split(" ");        //split input 
         boolean result=true;                 //return value always true set to false if there is no match 
         
         for(int i=0;i<array.length;i++)
            {
                int length=array[i].length();                            //get length of each word 
                int queryLength=(int) Math.round(0.1*length);                                          //get 60% of the length of each word then roundup
                String smallerString=array[i].substring(0,queryLength);                                //get the substring of new smaller length calculated 
                
                if(serviceString.indexOf(smallerString)!=-1){}                           //check if that smaller string lies in the service word
                
                
                
                else {System.out.println("not contained!!!");
                    result=false;                                                                       //return false if not contained
                    return false;
                }
                
            }
         
         
         return result;
     }
	 */
	 
	
	 
	 public static List<ServiceOffered> queryServicesUsingPattern(String findService)
	 {   
		 String serviceName;   //to store service name string from service
		 
		 List<ServiceOffered> results;    //to store generic query results 
		 
		 List<ServiceOffered> finalList= new ArrayList<>();   //to hold list that have a similar words to search of up to 40%
		 
		 Query<ServiceOffered> query=ofy().load().type(ServiceOffered.class);  //query all products-------test phase complete phase should have a list of profiles to query products from 
		
		query=query.order("SERVICECATEGORY");
		 
		 results=query.list();   //query all services test phase in production list of profile keys to be given 
		 
		 
		 
		 double [] finalListPercentageList=new double[results.size()];
		 int percentageArrayCounter=0;                                      //create array to store percentage similarity values and its counter 
		 
		 for(ServiceOffered iterate:results)                         //loop through services queried
		 {
			 serviceName=iterate.getServiceName();
			 
			 finalListPercentageList[percentageArrayCounter]=searchInputForSimilarities(serviceName,findService);   //store percentage value for each serviceOffered 
			 
			 if(searchInputForSimilarities(serviceName,findService)>40)          //check if the percentage similarity of goods offered and searched string is greater than 40%  
			 {
				 finalList.add(iterate);
				 
			 }
			 
			 percentageArrayCounter++;                                            //increament array counter 
		 }
		 
		logger.log(Level.SEVERE,"Old percentage siliraties are:   "+ Arrays.toString(finalListPercentageList));

		 
		 
		 if(finalList!=null&&(finalList.size()==finalListPercentageList.length))        //check if query list isn't empty and that the percentage list matches the qquery list 
		 {  ServiceOffered current;
		    ServiceOffered next;                      //to hold current and next services from the query list 
		    
		    
		    double currentPercentageValue;
		    double nextPercentageValue;              //to hold current and next percentage values from the percentage list 
			
			 for(int i=0;i<finalList.size();i++)
			 {
				 for(int j=0;j<finalList.size()-1;j++)
				 {                                             
					 
					 
					 if(finalListPercentageList[j]<finalListPercentageList[j+1])   //if the current percentage value is less than the next percentage swap both percentage values and service offered values so they are in sync
						 
						 
						 
					 {  
						 currentPercentageValue=finalListPercentageList[j];
						 nextPercentageValue=finalListPercentageList[j+1];
						 
						 finalListPercentageList[j]=nextPercentageValue;
						 finalListPercentageList[j+1]=currentPercentageValue;
						 

						 current=finalList.get(j);
						 next=finalList.get(j+1);
						 
						 finalList.set(j,next);
						 finalList.set(j+1,current);
					 }
				 }
				 
			 }
			 
			 
		 }
		 
		    else{logger.log(Level.SEVERE,"Either ArrayList null or percentage similarity array size does not match ArrayList");}
		 

		 
		return finalList; 
		 
	 }
	
	
}