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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/Item")
public class ItemServlet extends HttpServlet {

    //    query string
//    form Data (x-www-form-urlencoded)
//    JSON
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item");
            ResultSet rst = pstm.executeQuery();
            JsonArrayBuilder allItems = Json.createArrayBuilder();

            while (rst.next()) {
                JsonObjectBuilder item = Json.createObjectBuilder();
                item.add("code", rst.getString("code"));
                item.add("description", rst.getString("description"));
                item.add("qtyOnHand", rst.getString("qtyOnHand"));
                item.add("unitPrice", rst.getDouble("unitPrice"));
                allItems.add(item.build());
            }

            resp.getWriter().print(ResponseUtil.genJson("Success", "Loaded", allItems.build()));
        } catch (RuntimeException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        }
    }

    //    query string
//    JSON
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        String qtyOnHand = req.getParameter("qtyOnHand");
        String unitPrice = req.getParameter("unitPrice");

        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            PreparedStatement pstm = connection.prepareStatement("insert into Item values(?,?,?,?)");
            pstm.setObject(1,code);
            pstm.setObject(2,description);
            pstm.setObject(3,qtyOnHand);
            pstm.setObject(4,unitPrice);

            if (pstm.executeUpdate() > 0) {
                resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
            }

        } catch (RuntimeException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        }
    }

    //    query string
//    JSON
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            PreparedStatement pstm = connection.prepareStatement("delete from Item where code=?");
            pstm.setObject(1,code);

            if (pstm.executeUpdate() > 0) {
                resp.getWriter().print(ResponseUtil.genJson("Success", "Item Deleted..!"));
            }else{
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Item Delete Failed..!"));
            }
        } catch (RuntimeException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        }
    }

    //    query string
//    JSON
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject item = reader.readObject();

        String code = item.getString("code");
        String description = item.getString("description");
        String qtyOnHand = item.getString("qtyOnHand");
        String unitPrice = item.getString("unitPrice");
        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            PreparedStatement pstm = connection.prepareStatement("update Item set description=?,qtyOnHand=?,unitPrice=? where code=?");
            pstm.setObject(4,code);
            pstm.setObject(1,description);
            pstm.setObject(2,qtyOnHand);
            pstm.setObject(3,unitPrice);

            if (pstm.executeUpdate() > 0) {
                resp.getWriter().print(ResponseUtil.genJson("Success", "Item Updated..!"));
            }else{
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Item Updated Failed..!"));
            }
        } catch (RuntimeException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        }
    }
}
