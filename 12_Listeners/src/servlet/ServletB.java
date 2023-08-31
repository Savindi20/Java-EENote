package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(urlPatterns = "/b")
public class ServletB extends HttpServlet {
    public ServletB(){
        System.out.println("Servlet B Instantiated");
    }
}
