package api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class SearchRestarants
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
     *  For ELK
     */
//    private static final Logger LOGGER = Logger.getLogger(SearchRestaurants.class.getName());
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // response.getWriter().append("Served at: ").append(request.getContextPath());
        
        // Allow access only if session exists (remove below for ElasticSearch deployment)
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.setStatus(403);
            return;
        }
        
        JSONArray array = null;
//        JSONArray array = new JSONArray();
        DBConnection connection = DBConnectionFactory.getDBConnection();
//        DBConnection connection = new MongoDBConnection();
        if (request.getParameterMap().containsKey("user_id") && request.getParameterMap().containsKey("lat") && request.getParameterMap().containsKey("lon")) {
//        if (request.getParameterMap().containsKey("lat") && request.getParameterMap().containsKey("lon")) {
            String term = request.getParameter("term");
            String userId = (String) session.getAttribute("user");
//            String userId = "1111";
//            String userId = request.getParameter("user_id");
            double lat = Double.parseDouble(request.getParameter("lat"));
            double lon = Double.parseDouble(request.getParameter("lon"));
//            LOGGER.log(Level.INFO, "lat: " + lat + ", lon: " + lon);
            array = connection.searchRestaurants(userId, lat, lon, term);
        }
        RpcParser.writeOutput(response, array);

//        JSONArray  array = new JSONArray();
//        try {
//            if (request.getParameterMap().containsKey("user_id") && request.getParameterMap().containsKey("lat") && request.getParameterMap().containsKey("lon")) {
//                String userId = request.getParameter("user_id");
//                double lat = Double.parseDouble(request.getParameter("lat"));
//                double lon = Double.parseDouble(request.getParameter("lon"));
//                // return some fake restaurants
//                array.put(new JSONObject().put("name", "Panda Express"));
//                array.put(new JSONObject().put("name", "Hong Kong Express"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RpcParser.writeOutput(response, array);
        
        /*
         *  TEST Connection
         */
//        response.setContentType("application/json"); // tell browser that server is returning a response in a format of JSON
//        response.addHeader("Access-Control-Allow-Origin", "*"); // allow all viewers to view this response
//        // Create a PrintWriter from response such that we can add data to response
//        String username = "";
//        PrintWriter out = response.getWriter();
//        if (request.getParameter("username") != null) {
//            username = request.getParameter("username"); // Get the username sent from the client (user)
//            out.print("Hello " + username); // In the output stream, add some magic
//        }
//        out.flush(); // Flush the output stream and send the data to the client side
//        out.close(); // Close this response for good
        
        /*
         *  TEST format
         */
//        response.setContentType("text/html"); // tell browser that server is returning a response in a format of html
//        PrintWriter out = response.getWriter(); // Create a PrintWriter from a response such that we can add data to response
//        out.println("<html><body>");
//        out.println("<h1>This is a HTML page</h1>");
//        out.println("</body></html>");
//        out.flush();
//        out.close();
        
        /*
         *  TEST return
         */
//        response.setContentType("application/json"); // Tell browser that server is returning a response in a format of json
//        response.addHeader("Access-Control-Allow-Origin", "*"); // Allow all viewers to view this response
//        // Create a PrintWriter from response such that we can add data to response
//        String username = "";
//        if (request.getParameter("username") != null) {
//            username = request.getParameter("username"); // Get the username sent from the client (user)
//        }
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("username", username);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        PrintWriter out = response.getWriter();
//        out.print(obj);
//        out.flush(); // Flush the output stream and send the data to the client side
//        out.close();
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
