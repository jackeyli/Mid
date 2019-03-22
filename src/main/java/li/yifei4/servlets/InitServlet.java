package li.yifei4.servlets;

import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.data.threads.FlushDataJob;

import javax.servlet.*;
import java.io.IOException;

public class InitServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            DataManager.InitDataTable();
            Thread t = new Thread(new FlushDataJob());
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
