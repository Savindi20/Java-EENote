package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter(urlPatterns = "/b")
public class FilterTwo implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter Two Initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(" Filter Two Do Method Invoked");
        String name = servletRequest.getParameter("name");
        if (name.equals("ijse")){
            servletResponse.getWriter().write("<h1>Authenticated User</h1>");
        }else {
            servletResponse.getWriter().write("<h1>Non Authenticated User</h1>");
        }
    }

    @Override
    public void destroy() {
        System.out.println("Filter Two Destroyed ");
    }
}
