package data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBConnection implements DataConnection {
	private static final int MAX_RECOMMENDED_RESTAURANTS = 20;
	
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	public MongoDBConnection() {
		// Connects to local mongodb server
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(Configure.DB_NAME);
	}
	
	@Override
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	@Override
	public void setVisitedRestaurantsList(String userID, List<String> businessIDList) {
		// TODO Auto-generated method stub
		db.getCollection("users").updateOne(new Document("user_id", userID), new Document("$pushAll", new Document("visited", businessIDList)));
	}

	@Override
	public void unsetVisitedRestaurantsList(String userID, List<String> businessIDList) {
		// TODO Auto-generated method stub
		db.getCollection("users").updateOne(new Document("user_id", userID), new Document("$pushAll", new Document("visited", businessIDList)));
	}

	@Override
	public Set<String> getVisitedRestaurantsList(String userID) {
		// TODO Auto-generated method stub
//		return null;
		Set<String> set = new HashSet<>();
		// db.users.find({user_id: 1111});
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userID));
		
		if (iterable.first().containsKey("visited")) {
			List<String> list = (List<String>) iterable.first().get("visited");
			set.addAll(list);
		}
		return set;
	}

	@Override
	public JSONObject getRestaurantsByID(String businessID, boolean isVisited) {
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
	public JSONArray searchRestaurants(String userID, double lat, double lon, String term) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean verifyLogin(String userID, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserName(String user) {
		// TODO Auto-generated method stub
		return null;
	}
}
