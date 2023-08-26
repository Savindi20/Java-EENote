package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter(urlPatterns = "/a")
public class FilterFour implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Do Filter Invoked 1");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Do Filter Invoked 2");
    }

    @Override
    public void destroy() {

    }
}
