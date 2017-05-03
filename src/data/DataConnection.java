  package data;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DataConnection {

    /**
     * Close the connection.
     */
    public void close() ;
    
    /**
     * Insert the visited restaurants for a user.
     * @param userID
     * @param businessIDs
     * @return 
     */
    public void setVisitedRestaurantsList(String userID, List<String> businessIDList);

    /**
     * Delete the visited restaurants for a user.
     * @param userID
     * @param businessIDs
     */
    public void unsetVisitedRestaurantsList(String userID, List<String> businessIDList);

    /**
     * Get the visited restaurants for a user.
     * @param userID
     * @return
     */
    public Set<String> getVisitedRestaurantsList(String userID);

    /**
     * Get the restaurant json by ID.
     * @param businessID
     * @param isVisited, set the visited field in json.
     * @return
     */
    public JSONObject getRestaurantsByID(String businessID, boolean isVisited);

    /**
     * Recommend restaurants based on userID
     * @param userID
     * @return
     */
    public JSONArray recommendRestaurants(String userID);
    
    /**
     * Gets categories based on business ID
     * @param businessID
     * @return
     */
    public Set<String> getCategories(String businessID);

    /**
     * Gets business ID based on category
     * @param category
     * @return
     */
    public Set<String> getBusinessID(String category);
    
    /**
     * Search restaurants near a geolocation.
     * @param userID
     * @param lat
     * @param lon
     * @return
     */
    public JSONArray searchRestaurants(String userID, double lat, double lon, String term);

   /**
     * Verify if the userID matches the password.
     * @param userID
     * @param password
     * @return
     */
    public Boolean verifyLogin(String userID, String password);

    /**
     * Get user's name for the userID.
     * @param userID
     * @return First and Last Name
     */
    public String getUserName(String user);

}
