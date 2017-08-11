package api;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *  Autility class to handle rpc related parsing logics
 *
 */
public class RpcParser {
    
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
            response.setContentType("application/json"); // Tell the browser that server is return a response in a format of json
            response.addHeader("Access-Control-Allow-Origin", "*"); // Allow all viewers to view this response
            PrintWriter out = response.getWriter();
            out.print(obj);
            out.flush(); // Flush the output stream and send the data to the client side
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void writeOutput(HttpServletResponse response, JSONArray array) {
        try {
            response.setContentType("application/json"); // Tell the browser that server is return a response in a format of json
            response.addHeader("Access-Control-Allow-Origin", "*"); // Allow all viewers to view this response
            PrintWriter out = response.getWriter();
            out.print(array);
            out.flush(); // Flush the output stream and send the data to the client side
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
