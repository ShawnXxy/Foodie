package servletAPI;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class SearchRestaurants
 */
@WebServlet("/restaurants")
public class SearchRestaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchRestaurants() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		// TODO Auto-generated method stub
////	response.getWriter().append("Served at: ").append(request.getContextPath());		
//	/**
//	 * -------------
//	 * TEST : RPC prcess moved to new servletAPI package named RPCparse()
//	 * -------------
//	 * */
//	//Tell browser that server is returning a response in a format of JSON
//	response.setContentType("application/json");
////	response.setContentType("text/html");
//	//Allow all viewers to view this response
//	response.addHeader("Access-Control-Allow-Origin", "*"); 	
//	//Create a PrintWriter from response such that we can add data to response
//	String username = "";
//	PrintWriter out = response.getWriter();
//	//Get the username sent from the client(user)
//	if (request.getParameter("username") != null) {			
//		username = request.getParameter("username");
////		out.print("Hello " + username);
//	}
//	JSONObject obj = new JSONObject();
//	try {
//		obj.put("username", username);
//	} catch (JSONException e) {
//		e.printStackTrace();
//	}
//	
//	out.print(obj);
////	out.println("<html><body>");
////	out.println("<h1>This is a HTML page</h1>");
////	out.println("<body></html>");
//	
//	out.flush(); //Flush the output stream and send the data to the client side
//	out.close(); //Close this response
		
		// allow access only if session exists
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.setStatus(403);
			return;
		}

		JSONArray array = new JSONArray();
		//MySQL
		DataConnection connection = new MySQL();
		//MongoDB
//		DBConnection connection = new MongoDBConnection();
//		try {
			if (request.getParameterMap().containsKey("lat")
//					&& request.getParameterMap().containsKey("user_id")
					&& request.getParameterMap().containsKey("lon")) {
				// term is null or empty by default
				String term = request.getParameter("term");
				//String userID = request.getParameter("user_id");
				//String userID = (String) session.getAttribute("user");
	            String userID = "1111"; //fake user ID for test
				double lat = Double.parseDouble(request.getParameter("lat"));
				double lon = Double.parseDouble(request.getParameter("lon"));
				array = connection.searchRestaurants(userID, lat, lon, term);
				//TEST: return fake restaurants
//				array.put(new JSONObject().put("name", "Panda Express"));
//				array.put(new JSONObject().put("name", "Hong Kong Express"));				
			}
	//	} catch(JSONException e) {
	//		e.printStackTrace();
	//	}
		RPCparse.writeOutput(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
