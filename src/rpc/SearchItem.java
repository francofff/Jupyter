package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/search")
public class SearchItem extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // allow access only if session exists
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }

        // optional
        String userId = session.getAttribute("user_id").toString();

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));

        String term = request.getParameter("term");
        DBConnection connection = DBConnectionFactory.getConnection();
        try {
            List<Item> items = connection.searchItems(lat, lon, term);

            JSONArray array = new JSONArray();
            for (Item item : items) {
                array.put(item.toJSONObject());
            }
            RpcHelper.writeJsonArray(response, array);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}

