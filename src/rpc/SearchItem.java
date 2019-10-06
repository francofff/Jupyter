package rpc;

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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/search")
public class SearchItem extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));

        TicketMasterClient client = new TicketMasterClient();
//        RpcHelper.writeJsonArray(response, client.search(lat, lon, null));
        List<Item> items = client.search(lat, lon, null);
        JSONArray array = new JSONArray();
        for (Item item : items){
            array.put(item.toJSONObject());
        }
        RpcHelper.writeJsonArray(response, array);
    }
}

