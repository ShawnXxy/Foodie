package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class RestaurantTest {

    // @Test
    // public void test() {
    // fail("Not yet implemented");
    // }

    @Test
    public void testJsonArrayToString() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("Chinese");
        jsonArray.put("Japanese");
        jsonArray.put("Italian");
        assertEquals("Chinese,Japanese,Italian", Restaurant.jsonArrayToString(jsonArray));
    }

    @Test
    public void testJsonArrayToStringCornerCases() {
        JSONArray jsonArray = new JSONArray();
        assertEquals("", Restaurant.jsonArrayToString(jsonArray));
        jsonArray.put("Chinese"); // single input case
        assertEquals("Chinese", Restaurant.jsonArrayToString(jsonArray));
        jsonArray.put("Japanese");
        jsonArray.put(""); // empty case
        String str = Restaurant.jsonArrayToString(jsonArray);
        assertEquals("Chinese,Japanese,", str);
    }

    /**
     * A test fixture is all the things that must be in place in order to run a
     * test and expect a particular outcome. Frequently fixtures are created by
     * handling setUp() and tearDown() events of the unit testing framework. In
     * setUp() one would create the expected state for the test, and in
     * tearDown() it would clean up what had been set up. Four phases of a test:
     * • Set up -- Setting up the test fixture. • Exercise -- Interact with the
     * system under test. • Verify -- Determine whether the expected outcome has
     * been obtained. Tear down -- Tear down the test fixture to return to the
     * original state.
     */

    @Rule 
    public TestName name = new TestName();
    
    @Before
       public void setUp() {
        restaurant = new Restaurant("yam-leaf-bistro-mountain-view", "Yam Leaf Bistro",
                "Vegetarian,vegetarian,Vegan,vegan,Gluten-Free,gluten_free",
                "Mountain View", "CA", 4.5, "699 Calderon Ave,Mountain View, CA 94041",
                37.3851249, -122.075775,
                "http://s3-media1.fl.yelpcdn.com/bphoto/6NchHRhvHpVj4DXs2WQATw/ms.jpg",
                "http://www.yelp.com/biz/yam-leaf-bistro-mountain-view");
       }
    
    @Test
       public void testRestaurantConstructor() {
        String jsonString = "{\"is_claimed\": true, \"rating\": 4.5, \"mobile_url\": \"http://m.yelp.com/biz/yam-leaf-bistro-mountain-view\", \"rating_img_url\": \"http://s3-media2.fl.yelpcdn.com/assets/2/www/img/99493c12711e/ico/stars/v1/stars_4_half.png\", \"review_count\": 204, \"name\": \"Yam Leaf Bistro\", \"snippet_image_url\": \"http://s3-media4.fl.yelpcdn.com/photo/JYmqUtFxgYe-dbbcTqqzkw/ms.jpg\", \"rating_img_url_small\": \"http://s3-media2.fl.yelpcdn.com/assets/2/www/img/a5221e66bc70/ico/stars/v1/stars_small_4_half.png\", \"url\": \"http://www.yelp.com/biz/yam-leaf-bistro-mountain-view\", \"categories\": [[\"Vegetarian\", \"vegetarian\"], [\"Vegan\", \"vegan\"], [\"Gluten-Free\", \"gluten_free\"]], \"phone\": \"6509409533\", \"snippet_text\": \"Phenomenal Pan-Latin vegetarian, vegan (any dish can be made vegan), and gluten-free dishes. There selection of organic wines and beers is incredible--I go...\", \"image_url\": \"http://s3-media1.fl.yelpcdn.com/bphoto/6NchHRhvHpVj4DXs2WQATw/ms.jpg\", \"location\": {\"city\": \"Mountain View\", \"display_address\": [\"699 Calderon Ave\", \"Mountain View, CA 94041\"], \"geo_accuracy\": 9.5, \"postal_code\": \"94041\", \"country_code\": \"US\", \"address\": [\"699 Calderon Ave\"], \"coordinate\": {\"latitude\": 37.3851249, \"longitude\": -122.075775}, \"state_code\": \"CA\"}, \"display_phone\": \"+1-650-940-9533\", \"rating_img_url_large\": \"http://s3-media4.fl.yelpcdn.com/assets/2/www/img/9f83790ff7f6/ico/stars/v1/stars_large_4_half.png\", \"id\": \"yam-leaf-bistro-mountain-view\", \"is_closed\": false, \"distance\": 681.2472686205965}";
        Restaurant new_restaurant = null;
        try {
            new_restaurant = new Restaurant(new JSONObject(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(restaurant.getBusinessId(), new_restaurant.getBusinessId());
        assertEquals(restaurant.getName(), new_restaurant.getName());
        assertEquals(restaurant.getCategories(), new_restaurant.getCategories());
        assertEquals(restaurant.getCity(), new_restaurant.getCity());
        assertEquals(restaurant.getState(), new_restaurant.getState());
        assertEquals(restaurant.getFullAddress(), new_restaurant.getFullAddress());
        assertEquals(restaurant.getStars(), new_restaurant.getStars(), 0);
        assertEquals(restaurant.getLatitude(), new_restaurant.getLatitude(), 0);
        assertEquals(restaurant.getLongitude(), new_restaurant.getLongitude(), 0);
        assertEquals(restaurant.getImageUrl(), new_restaurant.getImageUrl());
        assertEquals(restaurant.getUrl(), new_restaurant.getUrl());
    }
    
    @After
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }

}
