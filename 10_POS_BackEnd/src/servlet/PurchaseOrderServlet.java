package servlet;

import org.apache.commons.dbcp2.BasicDataSource;
import util.ResponseUtil;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/purchase_order"})
public class PurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String orderID = req.getParameter("oid");

            //Initializing connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "sanu1234");
            PreparedStatement pstm = connection.prepareStatement("select Orders.oid,Orders.date,Orders.customerID,OrderDetails.itemCode,OrderDetails.qty,OrderDetails.unitPrice from Orders inner join OrderDetails on Orders.oid = OrderDetails.oid where Orders.oid=?");
            pstm.setObject(1, orderID);

            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allOrders = Json.createArrayBuilder();
            while (rst.next()) {
                String oid = rst.getString(1);
                String date = rst.getString(2);
                String customerID = rst.getString(3);
                String itemCode = rst.getString(4);
                String qty = rst.getString(5);
                String unitPrice = rst.getString(6);

                JsonObjectBuilder orders = Json.createObjectBuilder();
                orders.add("oid", oid);
                orders.add("date", date);
                orders.add("customerID", customerID);
                orders.add("itemCode", itemCode);
                orders.add("qty", qty);
                orders.add("unitPrice", unitPrice);

                allOrders.add(orders.build());
            }
            //create the response Object
            resp.getWriter().print(ResponseUtil.genJson("Success", "Loaded", allOrders.build()));
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get order and order details
        //so here we have to use json data because to purchase an order we need a series of data

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject orderJsonOb = reader.readObject();
        String oid = orderJsonOb.getString("oid");
        String date = orderJsonOb.getString("date");
        String cusID = orderJsonOb.getString("cusID");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "sanu1234");
            connection.setAutoCommit(false);

            PreparedStatement pstm = connection.prepareStatement("insert into Orders values(?,?,?)");
            pstm.setObject(1, oid);
            pstm.setObject(2, date);
            pstm.setObject(3, cusID);

            if (!(pstm.executeUpdate() > 0)) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new SQLException("Order Not added.!");
            }

            JsonArray orderDetails = orderJsonOb.getJsonArray("orderDetails");
            for (JsonValue orderDetail : orderDetails) {
                JsonObject odObject = orderDetail.asJsonObject();
                String itemCode = odObject.getString("code");
                String qty = odObject.getString("qty");
                String avQty = odObject.getString("avQty");
                String unitPrice = odObject.getString("price");

                PreparedStatement pstm2 = connection.prepareStatement("insert into OrderDetails values(?,?,?,?)");
                pstm2.setObject(1, oid);
                pstm2.setObject(2, itemCode);
                pstm2.setObject(3, qty);
                pstm2.setObject(4, unitPrice);

                if (!(pstm2.executeUpdate() > 0)) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    throw new SQLException("Order Details Not added.!");
                }

                //update the item also
                PreparedStatement pstm3 = connection.prepareStatement("update Item set qtyOnHand=? where code=?");
                pstm3.setObject(2, itemCode);
                int availableQty = Integer.parseInt(avQty);
                int purchasingQty = Integer.parseInt(qty);
                pstm3.setObject(1, (availableQty - purchasingQty));

                if (!(pstm3.executeUpdate() > 0)) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    throw new SQLException("Item cannot be updated");
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));

        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }


}