package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection connection = DBConnectionFactory.getConnection();
        try{
            JSONObject input = RpcHelper.readJSONObject(request);
            String userId = input.getString("user_id");
            String password = input.getString("password");
            String firstName = input.getString("first_name");
            String lastName = input.getString("last_name");

            JSONObject result = new JSONObject();
            if(connection.registerUser(userId, password, firstName, lastName)){
                result.put("status", "ok");
            }else{
                result.put("status", "user exists");
            }
            RpcHelper.writeJsonObject(response, result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
