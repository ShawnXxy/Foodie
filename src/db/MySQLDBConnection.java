package db;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Restaurant;
import yelp.YelpAPI;

public class MySQLDBConnection implements DBConnection {

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
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

    @Override
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
