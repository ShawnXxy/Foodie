package servletAPI;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * Helper function that to help parse other RPC
 *	PRC is Remote Procesure Call
 *	A utility class to handle rpc related parsing logics.
 */
public class RPCparse {
	public static JSONObject parseInput(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			reader.close();
			return new JSONObject(jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeOutput(HttpServletResponse response, JSONObject obj) {
		try {			
			//Tell browser that server is returning a response in a format of JSON
			response.setContentType("application/json");
			//Allow all viewers to view this response
			response.addHeader("Access-Control-Allow-Origin", "*");
			//Create a PrintWriter from response such that we can add data to response
			PrintWriter out = response.getWriter();
			out.print(obj);
			//Flush the output stream and send the data to the client side
			out.flush();
			//Close the response
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void writeOutput(HttpServletResponse response, JSONArray array) {
		try {		
			//Tell browser that server is returning a response in a format of JSON
			response.setContentType("application/json");
			//Allow all viewers to view this response
			response.addHeader("Access-Control-Allow-Origin", "*");
			//Create a PrinterWriter from response such that we can add data to response
			PrintWriter out = response.getWriter();
			out.print(array);
			//Flush the output stream and send the data to the client side
			out.flush();
			//Close the response
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
