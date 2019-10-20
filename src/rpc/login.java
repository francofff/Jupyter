package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class login extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        try {
            HttpSession session = request.getSession(false);
            JSONObject result = new JSONObject();
            if(session != null){
                String userID = session.getAttribute("user_id").toString();
                result.put("status", "OK")
                        .put("user_id", userID)
                        .put("name", connection.getFullname(userID));
            }else{
                result.put("status", "invalid session");
                response.setStatus(403);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        try {
            JSONObject body = RpcHelper.readJSONObject(request);
            String userId = body.getString("user_id");
            String password = body.getString("password");

            JSONObject result = new JSONObject();
            if (connection.verifyLogin(userId, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                session.setMaxInactiveInterval(600);
                result.put("status", "OK")
                        .put("user_id", userId)
                        .put("name", connection.getFavoriteItems(userId));
            } else {
                result.put("status", "user do not exist");
                response.setStatus(401);
            }
            RpcHelper.writeJsonObject(response, result);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            connection.close();
        }
    }
}
