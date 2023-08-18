package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/Item")
public class ItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item");
            ResultSet rst = pstm.executeQuery();
            String jsonArray ="[";
            while (rst.next()) {
                String code = rst.getString("code");
                String description = rst.getString("description");
                int qtyOnHand = rst.getInt("qtyOnHand");
                double unitPrice = rst.getDouble("unitPrice");
                jsonArray+="{\"code\":\""+code+"\",\"description\":\""+description+"\",\"qtyOnHand\":\""+qtyOnHand+"\",\"unitPrice\":"+unitPrice+"},";
            }
            String finalArray = jsonArray.substring(0, jsonArray.length() - 1);
            finalArray+="]";

            resp.addHeader("Content-Type","application/json");
            resp.getWriter().write(finalArray);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        int qtyOnHand = Integer.parseInt(req.getParameter("qtyOnHand"));
        String option=req.getParameter("option");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");

            if(option.equals("add")){
                double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));
                PreparedStatement pstm = connection.prepareStatement("insert into Item values(?,?,?,?)");
                pstm.setObject(1,code);
                pstm.setObject(2,description);
                pstm.setObject(3,qtyOnHand);
                pstm.setObject(4,unitPrice);
                boolean b = pstm.executeUpdate() > 0;

            }else if(option.equals("remove")){
                PreparedStatement pstm = connection.prepareStatement("delete from Item where code=?");
                pstm.setObject(1,code);
                boolean b = pstm.executeUpdate() > 0;

            }else if(option.equals("update")){
                double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));
                PreparedStatement pstm = connection.prepareStatement("update Item set description=?,qtyOnHand=?,unitPrice=? where code=?");
                pstm.setObject(4,code);
                pstm.setObject(1,description);
                pstm.setObject(2,qtyOnHand);
                pstm.setObject(3,unitPrice);
                boolean b = pstm.executeUpdate() > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}