package api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class RecommendRestaurants
 */
@WebServlet("/recommendation")
public class RecommendRestaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecommendRestaurants() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     *     Connect to MySQL
     */
    private static final DBConnection connection = DBConnectionFactory.getDBConnection();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	    
	    // Allow access only if session exists (remove below for Junit Test or ElasticSearch deployment)
//        HttpSession session = request.getSession();
//        if (session.getAttribute("user") == null) {
//            response.setStatus(403);
//            return;
//        }
//        try {
//            JSONArray array = null;
//            String userId = (String) session.getAttribute("user");
//            Set<String> visited_business_id = connection.getVisitedRestaurants(userId);
//            array = new JSONArray();
//            for (String id : visited_business_id) {
//                array.put(connection.getRestaurantsById(id, true));
//            }
//            RpcParser.writeOutput(response, array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        
//        String userId = request.getParameter("user_id");
//        double lat = Double.parseDouble(request.getParameter("lat"));
//        double lon = Double.parseDouble(request.getParameter("lon"));
//        List<Restaurant> list = connection.recommendRestaurants(userId, lat, lon);        
//        RpcParser.writeOutput(response, list);
        
	    JSONArray array = null;
	    if (request.getParameterMap().containsKey("user_id") 
	            ) {
	        String userId = request.getParameter("user_id");
	        array = connection.recommendRestaurants(userId);
	    }
	    RpcParser.writeOutput(response, array);
	    
//	    JSONArray array = new JSONArray();
//	    try {
//	        if (request.getParameterMap().containsKey("user_id")) {
//	            String userId = request.getParameter("user_id");
//	            // return some fake restaurants
//	            array.put(new JSONObject().put("name", "Panda Express").put("location", "downtown").put("country", "US"));
//	            array.put(new JSONObject().put("name", "Hong Kong Express").put("location", "uptown").put("country", "US"));
//	        }
//	    } catch (JSONException e) {
//	        e.printStackTrace();
//	    }
//	    RpcParser.writeOutput(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
