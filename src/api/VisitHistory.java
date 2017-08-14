package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.MySQLDBConnection;

/**
 * Servlet implementation class VisitHistory
 * 
 *  To handle the request that associates a user to a restaurant
 */
@WebServlet("/history")
public class VisitHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VisitHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 *     Connect to MySQL
	 */
	private static final DBConnection connection = new MySQLDBConnection();
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
	    
	    try {
	        JSONObject input = RpcParser.parseInput(request);
	        if (input.has("user_id") && input.has("visited")) {
	            String userId = (String) input.get("user_id");
	            JSONArray array = (JSONArray) input.get("visited");
	            List<String> visitedRestaurants = new ArrayList<>();
	            for (int i = 0; i < array.length(); i++) {
	                String businessId = (String) array.get(i);
	                visitedRestaurants.add(businessId);
	            }
	            connection.setVisitedRestaurants(userId, visitedRestaurants);
	            RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));;
	        } else {
	            RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
	        }
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	}

}
