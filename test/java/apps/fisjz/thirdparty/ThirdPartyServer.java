package apps.fisjz.thirdparty;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: ÉÏÎç10:24
 * To change this template use File | Settings | File Templates.
 */
public class ThirdPartyServer {
    public static void main(String[] args) throws Exception {
        Server server=new Server(2001);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        server.start();
        context.addServlet(new ServletHolder(new BurlapTestServlet()), "/remoting/nontaxBankService");
        System.out.println("The http server started...");
    }
}
