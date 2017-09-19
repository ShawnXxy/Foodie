//package algorithm;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import db.DBConnection;
//import db.DBConnectionFactory;
//import model.Restaurant;
//
//public class GeoRecommendation implements Recommendation {
//
//    @Override
//    public JSONArray recommendRestaurants(String userId, double lat, double lon) {
//        // TODO Auto-generated method stub
////        return null;
//        
//        DBConnection conn = DBConnectionFactory.getDBConnection();
//        // Step 1: Fetch all the restaurants(id) the user has visited
//        Set<String> visitedRestaurants = conn.getVisitedRestaurants(userId);
//        // Step 2: Given all these restaurants, fetch the categories
//        Set<String> allCategories = new HashSet<>();
//        for (String restaurant : visitedRestaurants) {
//            allCategories.addAll(conn.getCategories(restaurant));
//        }
//        // Step 3: Given these categories, search restaurants with these categories
//        Set<String> allRestaurants = new HashSet<>();
//        for (String category : allCategories) {
////            Set<String> set = getBusinessId(category);
//            Set<String>restaurants = (Set<String>) conn.searchRestaurants(userId, lat, lon, category);
//            allRestaurants.addAll(restaurants);
//        }
////        allCategories.remove("Undefined"); // tune category set
////        if (allCategories.isEmpty()) {
////            allCategories.add("");
////        }
//        // Step 4 : Filter restaurants that the user has visited
//        Set<JSONObject> diff = new HashSet<>();
//        int count = 0;
//        for (String businessId : allRestaurants) {
//            if (!visitedRestaurants.contains(businessId)) {
//                diff.add(conn.getRestaurantsById(businessId, false));
////                count++;
////                if (count >= conn.MAX_RECOMMENDED_RESTAURANTS) {
////                    break;
////                }
//            }
//        }
//        // Step 5: perform ranking of these items based on distance
//        Collections.sort((List)diff, new Comparator<Restaurant>() {
//            @Override
//            public int compare (Restaurant item1, Restaurant item2) {
//                double distance1 = getDistance(item1.getLatitude(), item1.getLongitude(), lat, lon);
//                double distance2 = getDistance(item2.getLatitude(), item2.getLongitude(), lat, lon);
//                // return the increasing order of distance
//                return (int) (distance1 - distance2);
//            }
//        });
//        return new JSONArray(diff);
//    }
//    
// // Calculate the distances between two geolocations
//    // http://andrew.hedges.name/experiments/haversine/
//    private static double getDistance (double lat1, double lon1, double lat2, double lon2) {
//        double dlon = lon2 - lon1;
//        double dlat = lat2 - lat1;
//        double a = Math.sin(dlat / 2 / 180 * Math.PI) * Math.sin(dlat / 2 / 180 * Math.PI) + Math.cos(lat1 / 180 * Math.PI) * Math.cos(lat2 / 180 * Math.PI) * Math.sin(dlon / 2 / 180 * Math.PI) * Math.sin(dlon / 2 / 180 * Math.PI);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        // Radius of earth in miles.
//        double R = 3961;
//        return R * c;
//
//    }
//
//}
