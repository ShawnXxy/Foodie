package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Restaurant;
import yelp.YelpAPI;

public class MySQLDBConnection implements DBConnection {
    
    private Connection conn = null;
    private static final int MAX_RECOMMENDED_RESTAURANTS = 10;
    
    public MySQLDBConnection() {
        this(DBUtil.URL);
    }
    
    public MySQLDBConnection(String url) {
        try {
            // Forcing the class representing the MySQL driver to load and initialize.
            // The new Instance() call is a work around for some broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                // Ignored
            }
        }
    }

    @Override
    public void setVisitedRestaurants(String userId, List<String> businessIds) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unsetVisitedRestaurants(String userId, List<String> businessIds) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<String> getVisitedRestaurants(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject getRestaurantsById(String businessId, boolean isVisited) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONArray recommendRestaurants(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getCategories(String businessId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getBusinessId(String category) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override  // When search and get some restaurants, store these restaurants in DB such that no need ot fetch them any more from Yelp
    public JSONArray searchRestaurants(String userId, double lat, double lon, String term) { 
        // TODO Auto-generated method stub
//        return null;
        
        try {
            //Connect to Yelp API
            YelpAPI api = new YelpAPI();
            JSONObject response = new JSONObject(api.searchForBusinessesByLocation(lat, lon));
            JSONArray array = (JSONArray) response.get("businesses");
            
            List<JSONObject> list = new ArrayList<>();
            
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Restaurant restaurant = new Restaurant(object);
                JSONObject obj = restaurant.toJSONObject();
                list.add(obj);
            }
            return new JSONArray(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean verifyLogin(String userId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFirstLastName(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

}
