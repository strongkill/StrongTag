package net.strong.exutil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * 
 * <p>Title: Log4JInit</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  Strong Software International CO,.LTD qt </p>
 * @since 2007-6-15
 * @author Strong Yuan
 * @version 1.0
 */

public class Log4JInit
    extends HttpServlet {
	private static final long serialVersionUID = -6165857493505615913L;

public void init() throws ServletException {
    String prefix = getServletContext().getRealPath("/");
    String file = getServletConfig().getInitParameter("log4j-config-file");

    if (file != null) {
      PropertyConfigurator.configure(prefix + file);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      IOException, ServletException {}

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      IOException, ServletException {}

  public void destroy() {
    super.destroy();
  }
}