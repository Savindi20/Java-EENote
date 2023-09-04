package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/b")
public class ServletB extends HttpServlet {
    public ServletB(){
        System.out.println("Servlet B Instantiated");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet B Init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servlet B doGet Method Invoked");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet B Destroy");
    }
}
