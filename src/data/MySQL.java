package data;

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

import helper.Restaurant;
import yelpAPI.GetYelp;

public class MySQL implements DataConnection {
	
	/**
	 * Query MySQL during the runtime
	 */
	private Connection connect = null;
	private static final int MAX_RECOMMENDED_RESTAURANTS = 10;
	
	public MySQL() {
		this(Configure.URL);
	}
	public MySQL(String url) {
		// TODO Auto-generated constructor stub
		try {
			//Forcing the class representing the MySQL driver to load and initialize.
			//The newInstance() call is a work around for some broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (connect != null) {
			try {
				connect.close();
			} catch (Exception e) {
				//igored
			}
		}
	}

	/**
	 * Call Yelp API and parse into Restaurant object(defined in Restaurant class)
	 * 
	 * When search and get some restaurants results, store the data in MySQL so no need to fetch them over and over every time when running
	 */
	@Override
	public JSONArray searchRestaurants(String userID, double lat, double lon, String term) {
		try {
			//Connect to Yelp API
			GetYelp yelp = new GetYelp();
			JSONObject response = new JSONObject(yelp.searchForBusinessesByLocation(lat, lon));
			JSONArray array = (JSONArray) response.get("businesses");
			
			List<JSONObject> list = new ArrayList<>();
			Set<String> visited = getVisitedRestaurantsList(userID);
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				 //Clean and purify 
				Restaurant restaurant = new Restaurant(object);
				String businessID = restaurant.getBusinessID();
				String name = restaurant.getName();
				String categories = restaurant.getCategories();
				double rating = restaurant.getRating();
				String city = restaurant.getCity();
				String state = restaurant.getState();
				String address = restaurant.getAddress();			
				double latitude = restaurant.getLatitude();
				double longitude = restaurant.getLongitude();
				String imageURL = restaurant.getImageURL();
				String url = restaurant.getURL();
				
				//return clean restaurant object
				JSONObject obj = restaurant.toJSONObject();
				
				if (visited.contains(businessID)) {
					obj.put("is_visited", true);
				} else {
					obj.put("is_visited", false);
				}
				String sql = "INSERT IGNORE INTO restaurants VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement statement = connect.prepareStatement(sql); 
				statement.setString(1, businessID);
				statement.setString(2, name);
				statement.setString(3, categories);
				statement.setDouble(4, rating);
				statement.setString(5, city);
				statement.setString(6, state);
				statement.setString(7, address);			
				statement.setDouble(8, latitude);
				statement.setDouble(9, longitude);
				statement.setString(10, imageURL);
				statement.setString(11, url);
				statement.execute();
				//Perform filtering if term is specified
				if (term == null || term.isEmpty()) {
					list.add(obj);
				} else if (categories.contains(term) || address.contains(term) || name.contains(term)) {
						list.add(obj);
				}				
			}
			return new JSONArray(list);
		} catch (Exception e) {
			//print result message in console 
			System.out.println(e.getMessage());
		}
		return null;
	}

	private Set<String> getVisitedRestaurantsList(String userID) { //Query the history table and get all the restaurants from the table
		// TODO Auto-generated method stub
//		return null;
		Set<String> visitedRestaurants = new HashSet<>();
		try {
			String sql = "SELECT business_id from history WHERE user_id = ?";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, userID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String visitedRestaurant = result.getString("business_id");
				visitedRestaurants.add(visitedRestaurant);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return visitedRestaurants;
	}
	
	@Override //Insert a new row in history table
	public void setVisitedRestaurantsList(String userID, List<String> businessIDList) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO history (user_id, business_id) VALUES (?, ?)";
		try {
			PreparedStatement statement = connect.prepareStatement(sql);
			for (String businessID: businessIDList) {
				statement.setString(1, userID);
				statement.setString(2, businessID);
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override //Delete an existed row 
	public void unsetVisitedRestaurantsList(String userID, List<String> businessIDList) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM history WHERE user_id = ? and business_id = ?";
		try {
			PreparedStatement statement = connect.prepareStatement(sql);
			for (String businessID: businessIDList) {
				statement.setString(1, userID);
				statement.setString(2, businessID);
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getVsitedRestaurantsList(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getRestaurantsByID(String businessID, boolean isVisted) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray recommendRestaurants(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getCategories(String businessID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getBusinessID(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean verifyLogin(String userID, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserName(String userID) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
