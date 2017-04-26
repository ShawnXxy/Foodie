package data;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DataConnection {

	//Close connection
	public void close();
	
	//Insert the visited restaurants for a user
	public void setVisitedRestaurantsList(String userID, List<String> businessIDList);	
	//Delete the visited restaurants for a user
	public void unsetVisitedRestaurantsList(String userID, List<String> businessIDList);
	//Get the visted restaurants for a user
	public Set<String> getVsitedRestaurantsList(String userID);
	
	//Get the restaurant json by ID
	public JSONObject getRestaurantsByID(String businessID, boolean isVisted);
	//Recommend restaurants based on userID
	public JSONArray recommendRestaurants(String userID);	
	//Get categories base on business ID
	public Set<String> getCategories(String businessID);
	//Get business ID based on categories
	public Set<String> getBusinessID(String category);
	//Search restaurants near a geolocation
	public JSONArray searchRestaurants(String userID, double lat, double lon, String term);
	
	//Verify if the userID matches the password
	public Boolean verifyLogin(String userID, String password);
	//Get user's name ofr the userID
	public String getUserName(String userID);
	
}
