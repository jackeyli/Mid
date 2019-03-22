package li.yifei4.filter;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.transaction.TransactionManager;
import li.yifei4.logIn.UserAgent;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogInFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
        User user = (User)session.getAttribute("user");
        if(user == null && ((HttpServletRequest) servletRequest).getRequestURL().indexOf("/login/login") < 0
        && ((HttpServletRequest) servletRequest).getRequestURL().indexOf("/login/regist") < 0){
            servletResponse.getWriter().print("Please Login");
        } else {
            TransactionManager.getInstance().clear();
            if(user != null)
                UserAgent.INSTANCE.getInstance().setUser(user);
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
    @Override
    public void destroy() {

    }
}
