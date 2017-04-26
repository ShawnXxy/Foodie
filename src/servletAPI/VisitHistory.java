package servletAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.DataConnection;
import data.MySQL;

/**
 * Servlet implementation class VisitHistory
 * 
 * TO handle the request that associates a user to a restaurant
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
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			DataConnection connection = new MySQL();
			JSONArray array = null;
			if (request.getParameterMap().containsKey("user_id")) {
				String userID = request.getParameter("user_id");
				Set<String> visited_business_id = connection.getVsitedRestaurantsList(userID);
				array = new JSONArray();
				for (String id: visited_business_id) {
					array.put(connection.getRestaurantsByID(id, true));
				}
				RPCparse.writeOutput(response, array);
			} else {
				RPCparse.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private static final DataConnection connection = new MySQL();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		try { // runs normally
			JSONObject input = RPCparse.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userID = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> businessIDList = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessID = (String) array.get(i);
					businessIDList.add(businessID);
				}
				connection.setVisitedRestaurantsList(userID, businessIDList);
				RPCparse.writeOutput(response, new JSONObject().put("status", "200")); //connection status === ok
			} else {
				RPCparse.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	//Implementation that frontend can call VisitHistory API and unset the visited restaurant 
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONObject input = RPCparse.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userID = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> businessIDList = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessID = (String) array.get(i);
					businessIDList.add(businessID);
				}
				connection.unsetVisitedRestaurantsList(userID, businessIDList);
				RPCparse.writeOutput(response, new JSONObject().put("status", "200"));
			} else {
				RPCparse.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
