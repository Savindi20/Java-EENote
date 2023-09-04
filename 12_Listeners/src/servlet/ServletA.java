package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/a")
public class ServletA extends HttpServlet {
    public ServletA(){
        System.out.println("Servlet A Instantiated");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet A Init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servlet A doGet Method Invoked");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet A Destroy");
    }
}
