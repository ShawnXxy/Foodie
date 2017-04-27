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
	private static final int MAX_RECOMMENDED_RESTAURANTS = 20;

	public MySQL() {
		this(Configure.URL);
	}

	public MySQL(String url) {
		// TODO Auto-generated constructor stub
		try {
			// Forcing the class representing the MySQL driver to load and initialize.
			// The newInstance() call is a work around for some broken Java implementations
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
				// igored
			}
		}
	}

	/**
	 * Call Yelp API and parse into Restaurant object(defined in Restaurant
	 * class)
	 * 
	 * When search and get some restaurants results, store the data in MySQL so
	 * no need to fetch them over and over every time when running
	 */
	@Override
	public JSONArray searchRestaurants(String userID, double lat, double lon, String term) {
		try {
			// Connect to Yelp API
			GetYelp yelp = new GetYelp();
			JSONObject response = new JSONObject(yelp.searchForBusinessByLocation(lat, lon));
			JSONArray array = (JSONArray) response.get("businesses");

			List<JSONObject> list = new ArrayList<>();
			Set<String> visited = getVisitedRestaurantsList(userID);

			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				// Clean and purify
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

				// return clean restaurant object
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
				// Perform filtering if term is specified
				if (term == null || term.isEmpty()) {
					list.add(obj);
				} else if (categories.contains(term) || address.contains(term) || name.contains(term)) {
					list.add(obj);
				}
			}
			return new JSONArray(list);
		} catch (Exception e) {
			// print result message in console
			System.out.println(e.getMessage());
		}
		return null;
	}

	private Set<String> getVisitedRestaurantsList(String userID) { // Query the history table and get all the restaurants from the table
		// TODO Auto-generated method stub
		// return null;
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

	@Override // Insert a new row in history table
	public void setVisitedRestaurantsList(String userID, List<String> businessIDList) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO history (user_id, business_id) VALUES (?, ?)";
		try {
			PreparedStatement statement = connect.prepareStatement(sql);
			for (String businessID : businessIDList) {
				statement.setString(1, userID);
				statement.setString(2, businessID);
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override // Delete an existed row
	public void unsetVisitedRestaurantsList(String userID, List<String> businessIDList) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM history WHERE user_id = ? and business_id = ?";
		try {
			PreparedStatement statement = connect.prepareStatement(sql);
			for (String businessID : businessIDList) {
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
//		return null;
		Set<String> visitedRestaurantSet = new HashSet<>();
		try {
			String sql = "SELECT business_id from history WHERE user_id = ?";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, userID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String visitedRestaurant = result.getString("business_id");
				visitedRestaurantSet.add(visitedRestaurant);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return visitedRestaurantSet;
	}

	@Override
	public JSONObject getRestaurantsByID(String businessID, boolean isVisted) {
		// TODO Auto-generated method stub
		// return null;
		try {
			String sql = "SELECT * from restaurants where business_id = ?";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, businessID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				Restaurant restaurant = new Restaurant(result.getString("business_id"), result.getString("name"), result.getString("categories"), result.getString("city"), result.getString("state"), result.getFloat("rating"), result.getString("full_address"), result.getFloat("latitude"), result.getFloat("longitude"), result.getString("image_url"), result.getString("url"));
				JSONObject obj = restaurant.toJSONObject();
				obj.put("is_visited", isVisted);
				return obj;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public JSONArray recommendRestaurants(String userID) {
		// TODO Auto-generated method stub
		// return null;
		try {
			if (connect == null) {
				return null;
			}
			// STEP 1: fetch all the visited restaurants from history table
			Set<String> visitedRestaurants = getVisitedRestaurantsList(userID);
			// STEP 2: Given all these restaurants, fetch all categories
			Set<String> categorySet = new HashSet<>();
			for (String restaurant : visitedRestaurants) {
				categorySet.addAll(getCategories(restaurant));
			}
			// STEP 3: Given all these categories, find restaurants with these
			// categories from restaurants table
			Set<String> categoriedRestaurants = new HashSet<>();
			for (String category : categoriedRestaurants) {
				Set<String> categoriedBusinessID = getBusinessID(category);
				categoriedRestaurants.addAll(categoriedBusinessID);
			}
			// STEP 4: Filter restaurants that the user visited
			Set<JSONObject> result = new HashSet<>();
			int count = 0;
			for (String businessID : categoriedRestaurants) {
				if (!visitedRestaurants.contains(businessID)) {
					result.add(getRestaurantsByID(businessID, false));
					count++;
					if (count >= MAX_RECOMMENDED_RESTAURANTS) {
						break;
					}
				}
			}
			return new JSONArray(result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Set<String> getCategories(String businessID) {
		// TODO Auto-generated method stub
		// return null;
		try {
			String sql = "SELECT categories from restaurants WHERE business_id = ?";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, businessID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				Set<String> categorySet = new HashSet<>();
				String[] categoryArray = result.getString("categories").split(",");
				for (String category : categoryArray) {
					categorySet.add(category.trim());
				}
				return categorySet;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new HashSet<String>();
	}

	@Override
	public Set<String> getBusinessID(String category) {
		// TODO Auto-generated method stub
		// return null;
		Set<String> businessIDSet = new HashSet<>();
		try {
			String sql = "SELECT business_id from restaurants WHERE categories LIKE ?";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, "%" + category + "%");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String businessID = result.getString("business_id");
				businessIDSet.add(businessID);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return businessIDSet;
	}

	@Override
	public Boolean verifyLogin(String userID, String password) {
		// TODO Auto-generated method stub
		// return null;
		try {
			if (connect == null) {
				return false;
			}
			String sql = "SELECT user_id from suers WHERE user_id = ? and password = ?";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, userID);
			statement.setString(2, password);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public String getUserName(String userID) {
		// TODO Auto-generated method stub
		// return null;
		String name = "";
		try {
			if (connect != null) {
				String sql = "SELECT first_name, last_name from users WHERE user_id = ?";
				PreparedStatement statement = connect.prepareStatement(sql);
				statement.setString(1, userID);
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					name += result.getString("first_name") + result.getString("last_name");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return name;
	}

}
