package helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Restaurant {
	// * Performed data cleanup and purify from Yelp API.
	public static String parseString(String str) {
		// tell java that quotaion mark and slash are reserved
		return str.replace("\"", "\\\"").replace("/", " or ");
	}

	public static String jsonArrayToString(JSONArray array) {
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < array.length(); i++) {
				String obj = (String) array.get(i);
				sb.append(obj);
				if (i != array.length() - 1) {
					sb.append(",");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// for front-end because front-end cannot read java class
	public static JSONArray stringToJSONArray(String str) {
		try {
			return new JSONArray("[" + parseString(str) + "]");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String businessID;
	private String name;
	private String categories;
	private double rating;
	private String city;
	private String state;
	private String address;	
	private double latitude;
	private double longitude;
	private String imageURL;
	private String url;

	/**
	 * TO convert a JSONObject fetched from Yelp API to a converted restaurant
	 * object
	 * 
	 */
	public Restaurant(JSONObject object) {
		try {
			if (object != null) {
				this.businessID = object.getString("id");
				JSONArray jsonArray = (JSONArray) object.get("categories");
				List<String> list = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject subObejct = jsonArray.getJSONObject(i);
					list.add(subObejct.getString("title"));
				}
				this.categories = String.join(",", list);
				this.name = object.getString("name");
				this.imageURL = object.getString("image_url");
				this.rating = object.getDouble("rating");
				JSONObject coordinates = (JSONObject) object.get("coordinates");
				this.latitude = coordinates.getDouble("latitude");
				this.longitude = coordinates.getDouble("longitude");
				JSONObject location = (JSONObject) object.get("location");
				this.city = location.getString("city");
				this.state = location.getString("state");
				this.address = jsonArrayToString((JSONArray) location.get("display_address")); // display_address
				this.url = object.getString("url");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//constructor, used in MySQL class
	public Restaurant(String businessID, String name, String categories, String city, String state, double rating,
			String address, double latitude, double longitude, String imageURL, String url) {
		this.businessID = businessID;
		this.name = name;
		this.categories = categories;
		this.city = city;
		this.state = state;
		this.address = address;
		this.rating = rating;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageURL = imageURL;
		this.url = url;
	}

	public JSONObject toJSONObject() { //// Convert a Restaurant object to a JSONObject
		JSONObject obj = new JSONObject();
		try {
			obj.put("business_id", businessID);
			obj.put("name", name);
			obj.put("categories", stringToJSONArray(categories));
			obj.put("rating", rating);
			obj.put("city", city);
			obj.put("state", state);
			obj.put("full_address", address);
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("image_url", imageURL);
			obj.put("url", url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 
	 * accessor && mutator SETTERs and GETTERs
	 * 
	 */
	public String getBusinessID() {
		return businessID;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}
}
