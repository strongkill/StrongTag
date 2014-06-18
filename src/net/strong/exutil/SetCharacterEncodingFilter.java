package net.strong.exutil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 过滤器.对所有中文进行编码过滤
 * <p>Title: SetCharacterEncodingFilter.java</p>
 * <p>Description: 过滤器.对所有中文进行编码过滤</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  Strong Software International CO,.LTD  </p>
 * @Date 2007-3-20
 * @author Strong Yuan
 * @version 1.0
 */
public class SetCharacterEncodingFilter
implements Filter {

	Log log = LogFactory.getLog(this.getClass().getName());
	protected String encoding = null;
	protected FilterConfig filterConfig = null;
	protected boolean ignore = true;
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			if (ignore || (request.getCharacterEncoding() == null)) {
				String encoding = selectEncoding(request);

					if (encoding != null)
						request.setCharacterEncoding(encoding);

			}
			chain.doFilter(request, response);
		}catch(ServletException se){
			log.error("exception at setCharacterEncoding:" + se.getMessage());
			throw se;
		
		} catch (IOException e) {
			log.error("exception at setCharacterEncoding:" + e.getMessage());
			throw e;
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null) {
			this.ignore = true;
		} else if (value.equalsIgnoreCase("true")) {
			this.ignore = true;
		} else if (value.equalsIgnoreCase("yes")) {
			this.ignore = true;
		} else {
			this.ignore = false;
		}
	}

	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}
}
