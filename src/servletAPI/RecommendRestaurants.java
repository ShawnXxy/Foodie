package servletAPI;

import java.io.IOException;
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

    /***
     * CONNECT TO DATABASE
     */
    private static DataConnection connection = new MySQL();
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());

//		JSONArray array = new JSONArray();		
		JSONArray array = null;
//		try {
			if (request.getParameterMap().containsKey("user_id")) {
				String userID = request.getParameter("user_id");
				array = connection.recommendRestaurants(userID);
//				//TEST: return fake restaurants
//				array.put(new JSONObject()
//						.put("name", "Panda Express")
//						.put("location", "downtown")
//						.put("country", "US"));
//				array.put(new JSONObject()
//						.put("name", "Hong Kong Express")
//						.put("location", "uptown")
//						.put("country", "US"));
			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		RPCparse.writeOutput(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
