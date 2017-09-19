package api;

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

import db.DBConnection;
import db.DBConnectionFactory;
import db.mysql.MySQLDBConnection;

/**
 * Servlet implementation class VisitHistory
 * 
 * To handle the request that associates a user to a restaurant
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
     * Connect to MySQL
     */
    private static final DBConnection connection = DBConnectionFactory.getDBConnection();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // response.getWriter().append("Served at://").append(request.getContextPath());

        // Allow access only if session exists (remove below for ElasticSearch deployment)
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.setStatus(403);
            return;
        }
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

        try {
            DBConnection connection = new MySQLDBConnection();
            JSONArray array = null;
            if (request.getParameterMap().containsKey("user_id")) {
//                String userId = request.getParameter("user_id");
                String userId = (String) session.getAttribute("user");
                Set<String> visited_business_id = connection.getVisitedRestaurants(userId);
                array = new JSONArray();
                for (String id : visited_business_id) {
                    array.put(connection.getRestaurantsById(id, true));
                }
                RpcParser.writeOutput(response, array);
            } else {
                RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse  response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // doGet(request, response);
        
     // Allow access only if session exists (remove below for ElasticSearch deployment)
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.setStatus(403);
            return;
        }

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
                RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
            } else {
                RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
     // Allow access only if session exists (remove below for ElasticSearch deployment)
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.setStatus(403);
            return;
        }
        
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
                connection.unsetVisitedRestaurants(userId, visitedRestaurants);
                RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
            } else {
                RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
