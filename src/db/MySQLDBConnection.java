package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
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
        String query = "INSERT INTO history (user_id, business_id) VALUES (?, ?)";
        try {
            PreparedStatement statement =  conn.prepareStatement(query);
            for (String businessId : businessIds) {
                statement.setString(1, userId);
                statement.setString(2, businessId);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsetVisitedRestaurants(String userId, List<String> businessIds) {
        // TODO Auto-generated method stub
        String query = "DELETE FROM history WHERE useri_id = ? and business_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            for (String businessId : businessIds) {
                statement.setString(1, userId);
                statement.setString(2, businessId);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getVisitedRestaurants(String userId) {
        // TODO Auto-generated method stub
//        return null;
        
        Set<String> visitedRestaurants = new HashSet<>();
        try {
            String sql = "SELECT business_id from history WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String visitedRestaurant = rs.getString("business_id");
                visitedRestaurants.add(visitedRestaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return visitedRestaurants;
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
            Set<String> visited = getVisitedRestaurants(userId);
            
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Restaurant restaurant = new Restaurant(object);
                
                String businessId  = restaurant.getBusinessId();
                String name = restaurant.getName();
                String categories = restaurant.getCategories();
                String city = restaurant.getCity();
                String state = restaurant.getState();
                String fullAddress = restaurant.getFullAddress();
                double stars = restaurant.getStars();
                double latitude = restaurant.getLatitude();
                double longitude = restaurant.getLongitude();
                String imageUrl = restaurant.getImageUrl();
                String url = restaurant.getUrl();
                
                JSONObject obj = restaurant.toJSONObject();
                
                if (visited.contains(businessId)) {
                    obj.put("is_visited", true);
                } else {
                    obj.put("is_visited", false);
                }
                
                // Update to MySQL
                String sql = "INSERT IGNORE INTO restaurants VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, businessId);
                statement.setString(2, name);
                statement.setString(3, categories);
                statement.setString(4, city);
                statement.setString(5, state);
                statement.setDouble(6, stars);
                statement.setString(7, fullAddress);
                statement.setDouble(8, latitude);
                statement.setDouble(9, longitude);
                statement.setString(10, imageUrl);
                statement.setString(11, url);
                statement.execute(); // End update
                
                // Perform filtering if term is specified
                if (term == null || term.isEmpty()) {
                    list.add(obj);
                } else {
                    if (categories.contains(term) || fullAddress.contains(term) || name.contains(term)) {
                        list.add(obj);
                    }
                }   
//                list.add(obj);
            } // End for loop
            return new JSONArray(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean verifyLogin(String userId, String password) {
        // TODO Auto-generated method stub
//        return null;
        
        try {
            if (conn == null) {
                return false;
            }
            
            String sql = " SELECT user_id from users WHERE user_id = ? and password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public String getFirstLastName(String userId) {
        // TODO Auto-generated method stub
//        return null;
        
        String name = "";
        try {
            if (conn != null) {
                String sql = "SELECT first_name, last_name from users WHERE user_id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, userId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    name += rs.getString("first_name") + " " + rs.getString("last_name");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return name;
    }

}
