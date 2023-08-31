package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(urlPatterns = "/a")
public class ServletA extends HttpServlet {
    public ServletA(){
        System.out.println("Servlet A Instantiated");
    }
}
