package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import model.Restaurant;
import yelp.YelpAPI;

public class MySQLDBConnection implements DBConnection {
    
    private static MySQLDBConnection instance;

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new MySQLDBConnection();
        }
        return instance;
    }
    
    private Connection conn = null;
    private static final int MAX_RECOMMENDED_RESTAURANTS = 30;
    
    public MySQLDBConnection() {
        this(DBUtil.URL);
    }
    
    public MySQLDBConnection(String url) {
        try {
            // Forcing the class representing the MySQL driver to load and initialize.
            // The new Instance() call is a work around for some broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            conn = DriverManager.getConnection(url);
            
            // Set connection properties for connecting to Azure DB MySQL.
            Properties properties = new Properties();
            properties.setProperty("user", DBUtil.USERNAME);
            properties.setProperty("password", DBUtil.PASSWORD);
            properties.setProperty("useSSL", "true");
            properties.setProperty("verifyServerCertificate", "true");
            properties.setProperty("requireSSL", "false");
            conn = DriverManager.getConnection(url, properties);
            
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
        String query = "DELETE FROM history WHERE user_id = ? and business_id = ?";
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
//        return null;
        
        try {
            String sql = "SELECT * from restaurants where business_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, businessId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Restaurant restaurant = new Restaurant(
                        rs.getString("business_id"), 
                        rs.getString("name"), 
                        rs.getString("categories"), 
                        rs.getString("city"), 
                        rs.getString("state"), 
                        rs.getFloat("stars"), 
                        rs.getString("full_address"), 
                        rs.getFloat("latitude"), 
                        rs.getFloat("longitude"), 
                        rs.getString("image_url"), 
                        rs.getString("url")
                 );
                JSONObject obj = restaurant.toJSONObject();
                obj.put("is_visited", isVisited);
                return obj;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public JSONArray recommendRestaurants(String userId) {
//    public JSONArray recommendRestaurants(String userId, double lat, double lon) {
        // TODO Auto-generated method stub
//        return null;
        
        try {
            if (conn == null) {
                return null;
            }           
            // Step 1: Fetch all the restaurants(id) the user has visited
            Set<String> visitedRestaurants = getVisitedRestaurants(userId);
            // Step 2: Given all these restaurants, fetch the categories
            Set<String> allCategories = new HashSet<>();
            for (String restaurant : visitedRestaurants) {
                allCategories.addAll(getCategories(restaurant));
            }
            // Step 3: Given these categories, search restaurants with these categories
            Set<String> allRestaurants = new HashSet<>();
            for (String category : allCategories) {
                Set<String> set = getBusinessId(category);
//                Set<String> set = (Set<String>) searchRestaurants(userId, lat, lon, category);
                allRestaurants.addAll(set);
            }
            // Step 4 : Filter restaurants that the user has visited
            Set<JSONObject> diff = new HashSet<>();
            int count = 0;
            for (String businessId : allRestaurants) {
                if (!visitedRestaurants.contains(businessId)) {
                    diff.add(getRestaurantsById(businessId, false));
                    count++;
                    if (count >= MAX_RECOMMENDED_RESTAURANTS) {
                        break;
                    }
                }
            }
            return new JSONArray(diff);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<String> getCategories(String businessId) {
        // TODO Auto-generated method stub
//        return null;
        
        try {
            String sql = "SELECT categories from restaurants WHERE business_id = ? ";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, businessId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Set<String> set = new HashSet<>();
                String[] categories = rs.getString("categories").split(",");
                for (String category : categories) {
                    // ' Japanese ' -> 'Japanese'
                    set.add(category.trim());
                }
                return set;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new HashSet<String>();
    }

    @Override
    public Set<String> getBusinessId(String category) {
        // TODO Auto-generated method stub
//        return null;
        
        Set<String> set = new HashSet<>();
        try {
            String  sql =  "SELECT business_id from restaurants WHERE categories LIKE ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + category + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String businessId = rs.getString("business_id");
                set.add(businessId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return set;
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

            String sql = "SELECT user_id from users WHERE user_id = ? and password = ?";
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
    
    // Calculate the distances between two geolocations
    // http://andrew.hedges.name/experiments/haversine/
    private static double getDistance (double lat1, double lon1, double lat2, double lon2) {
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.sin(dlat / 2 / 180 * Math.PI) * Math.sin(dlat / 2 / 180 * Math.PI) + Math.cos(lat1 / 180 * Math.PI) * Math.cos(lat2 / 180 * Math.PI) * Math.sin(dlon / 2 / 180 * Math.PI) * Math.sin(dlon / 2 / 180 * Math.PI);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Radius of earth in miles.
        double R = 3961;
        return R * c;

    }

}
