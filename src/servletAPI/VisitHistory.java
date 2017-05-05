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
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.DataConnection;
import data.MySQL;

/**
 * Servlet implementation class VisitHistory
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		
		// allow access only if session exists
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.setStatus(403);
			return;
		}
		
		try {
			//MySQL
			DataConnection connection = new MySQL();
			//MongoDB
//			private static DBConnection connection = new MongoDBConnection();
			JSONArray array = null;
			if (request.getParameterMap().containsKey("user_id")) {
//				String userID= request.getParameter("user_id");
				String userID = (String) session.getAttribute("user"); // Safer method
				Set<String> visited_businessIDs = connection.getVisitedRestaurantsList(userID);
				array = new JSONArray();
				for (String id : visited_businessIDs) {
					array.put(connection.getRestaurantsByID(id, true));
				}
				RPCparse.writeOutput(response, array);
			} else {
				RPCparse.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	} //end of doGet()

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private static final DataConnection connection = new MySQL();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		
		// allow access only if session exists
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.setStatus(403);
			return;
		}
		
		try {
			JSONObject input = RPCparse.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userID = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> visitedRestaurants = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessId = (String) array.get(i);
					visitedRestaurants.add(businessId);
				}
				connection.setVisitedRestaurantsList(userID, visitedRestaurants);
				RPCparse.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RPCparse.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	} //end of doPost()

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// allow access only if session exists
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.setStatus(403);
			return;
		}
		
		try {
			JSONObject input = RPCparse.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userID = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> visitedRestaurants = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessId = (String) array.get(i);
					visitedRestaurants.add(businessId);
				}
				connection.unsetVisitedRestaurantsList(userID, visitedRestaurants);
				RPCparse.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RPCparse.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	} // end of doDelete

}
