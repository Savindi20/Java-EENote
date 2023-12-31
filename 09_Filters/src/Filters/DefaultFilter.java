package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class DefaultFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //http://localhost:8081/filter/a
        //a?name=ijse ---> dispatch a
        //b?name=ijse ---> dispatch b
        String name = servletRequest.getParameter("name");

        //cast servletRequest to HttpServletRequest
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String servletPath = req.getServletPath();

        if(name.equals("ijse")&&servletPath.equals("/a")){
            //proceed to servlet
            filterChain.doFilter(servletRequest,servletResponse);
        }else if (name.equals("ijse")&&servletPath.equals("/b")){
            //proceed to servlet
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            //refuse
            servletResponse.getWriter().write("<h1>Case</h1>");
        }
    }

    @Override
    public void destroy() {

    }
}