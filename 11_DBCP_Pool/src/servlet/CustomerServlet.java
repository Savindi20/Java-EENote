package servlet;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletContext;
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

@WebServlet(urlPatterns = "/Customer")
public class CustomerServlet extends HttpServlet {

//    query string
//    form Data (x-www-form-urlencoded)
//    JSON
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
//            // How to configure DBCP pool
//            BasicDataSource bds = new BasicDataSource();
//            bds.setDriverClassName("com.mysql.jdbc.Driver");
//            bds.setUrl("jdbc:mysql://localhost:3306/posdb");
//            bds.setPassword("1234");
//            bds.setUsername("root");
//            //how many connection
//            bds.setMaxTotal(2);
//            // how many connections should be initialize from created connections
//            bds.setInitialSize(2);
//
//            Connection connection = bds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer");
            ResultSet rst = pstm.executeQuery();
            JsonArrayBuilder allCustomers = Json.createArrayBuilder();

            while (rst.next()) {
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("id", rst.getString("id"));
                customer.add("name", rst.getString("name"));
                customer.add("address", rst.getString("address"));
                customer.add("salary", rst.getDouble("salary"));
                allCustomers.add(customer.build());
            }

            // release the connection back to the pool
            // connection.close();

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("state","OK");
            job.add("message","Successfully Loaded..!");
            job.add("data",allCustomers.build());
            resp.getWriter().print(job.build());

        }catch (SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }

//    query string
//    JSON
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");

        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
            pstm.setObject(1,id);
            pstm.setObject(2,name);
            pstm.setObject(3,address);
            pstm.setObject(4,salary);
            boolean b = pstm.executeUpdate() > 0;
            if (b){
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","Ok");
                responseObject.add("message","Successfully added..!");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());
            }
        }catch (SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
//            resp.setStatus(400);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());
        }
    }

//    query string
//    JSON
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            PreparedStatement pstm = connection.prepareStatement("delete from Customer where id=?");
            pstm.setObject(1,id);
            boolean b = pstm.executeUpdate() > 0;
            if (b) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Deleted..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());
            }else {
                throw new RuntimeException("There is no Customer for that ID..!");
            }
        } catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());
        }catch (SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }

//    query string
//    JSON
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        String salary = customer.getString("salary");

        try(Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            PreparedStatement pstm = connection.prepareStatement("update Customer set name=?,address=?,salary=? where id=?");
            pstm.setObject(4,id);
            pstm.setObject(1,name);
            pstm.setObject(2,address);
            pstm.setObject(3,salary);
            boolean b = pstm.executeUpdate() > 0;
            if (b){
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","Ok");
                responseObject.add("message","Successfully Updated..!");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());
            }else{
                throw new RuntimeException("Wrong ID, Please check the ID..!");
            }

        } catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());
        }catch (SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }
}
