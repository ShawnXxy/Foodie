package yelpAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetYelp { //initialize the connection with Yelp Developer account, will be used by RPCparse()
	
	//Yelp API source sample code https://github.com/Yelp/yelp-api/blob/master/v2/java/YelpAPI.java
	private static final String API_HOST = "https://api.yelp.com";
	private static final String DEFAULT_TERM = "dinner";
	private static final int SEARCH_LIMIT = 30;
	private static final String SEARCH_PATH = "/v3/businesses/search";
	private static final String TOKEN_HOST = "https://api.yelp.com/oauth2/token";
	private static final String CLIENT_ID = "GVs-s_-YSLeowugSzktDzg";
	private static final String CLIENT_SECRET = "kipvlduDU8hUQ5UIyGvWnnAchHH9FAGr2j836gROl7lfwL4o9XKWu06JFDoy29Fq";
	private static final String GRANT_TYPE = "client_credentials";
	private static final String TOKEN_TYPE = "Bearer";

	public GetYelp() {};

	/**
	 * Create and send a request to Yelp Token Host and return the access token
	 */	
	private JSONObject obtainAccessToken() {
		//https://github.com/Yelp/yelp-api/blob/master/v2/java/YelpAPI.java
		try {
			String query = String.format("grant_type=%s&client_id=%s&client_secret=%s",	GRANT_TYPE, CLIENT_ID, CLIENT_SECRET); //GRANT_TYPE = "client_credentials"; 
			
			HttpURLConnection connection = (HttpURLConnection) new URL(TOKEN_HOST).openConnection(); //TOKEN_TYPE = "Bearer";

			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			// add request header
			//connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			DataOutputStream getSomething = new DataOutputStream(connection.getOutputStream());
			getSomething.write(query.getBytes());

			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + TOKEN_HOST); //TOKEN_TYPE = "Bearer";
			System.out.println("Response Code : " + responseCode);

			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = input.readLine()) != null) {
				response.append(inputLine);
			}
			//close the response
			input.close();
			//print result
			return new JSONObject(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Creates and sends a request to the Search API by term and location.
	 * 
	 * Builds the query to Yelp API
	 * 
	 * Yelp official API document for search and business
	 * https://www.yelp.com/developers/documentation/v2/search_api
	 * https://www.yelp.com/developers/documentation/v2/business
	 */
	public String searchForBusinessByLocation(double lat, double lon) {
		String latitude = lat + "";
		String longitude = lon + "";
		String query = String.format("term=%s&latitude=%s&longitude=%s&limit=%s", DEFAULT_TERM, latitude, longitude, SEARCH_LIMIT); //DEFAULT_TERM = "dinner"; 
		String url = API_HOST + SEARCH_PATH; //API_HOST = "https://api.yelp.com"; SEARCH_PATH = "/v3/businesses/search";
		try {
			String access_token = obtainAccessToken().getString("access_token");
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();

			// optional default is GET
			connection.setRequestMethod("GET");

			// add request header
			//connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Authorization",  TOKEN_TYPE + " " + access_token);

			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = input.readLine()) != null) {
				response.append(inputLine);
			}
			//close response
			input.close();
			// print result
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Queries the Search API based on the command line arguments and takes the first result to query the Business API.
	 * 
	 *  Internal method to test Yelp API and make sure the configuration is correct
	 */
	private static void queryAPI(GetYelp yelp, double lat, double lon) {
		String searchResponseJSON = yelp.searchForBusinessByLocation(lat, lon);
		JSONObject response = null;
		try {
			response = new JSONObject(searchResponseJSON);
			JSONArray businessList = (JSONArray) response.get("businesses");
			for (int i = 0; i < businessList.length(); i++) {
				JSONObject business = (JSONObject) businessList.get(i);
				System.out.println(business);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * TEST:
	 * 
	 * Main entry for sample Yelp API requests.
	 * 
	 * Use FHSU as an example
	 */
	public static void main(String[] args) {
		GetYelp yelp = new GetYelp();
		//FHSU
		queryAPI(yelp, 38.8799294, -99.3349108);
		//SF
//		queryAPI(yelp, 37.38, -122.08);
	}
}

