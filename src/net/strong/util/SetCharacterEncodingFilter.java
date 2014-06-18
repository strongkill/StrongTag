package net.strong.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 本类已不用，请使用net.strong.exutil.SetCharacterEncodingFilter
 * 
 * <p>Title: SetCharacterEncodingFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  Strong Software International CO,.LTD qt </p>
 * @since 2007-6-15
 * @author Strong Yuan
 * @version 1.0
 * @deprecated
 */
public class SetCharacterEncodingFilter implements Filter {

  Log log = LogFactory.getLog(this.getClass().getName());

    // ----------------------------------------------------- Instance Variables


    /**
     * The default character encoding to set for requests that pass through
     * this filter.
     */
    protected String encoding = null;


    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    /**
     * 上传文件的最大大小
     */
    protected int fileMaxLength = 200*1024; //200K

    /**
     * Should a character encoding specified by the client be ignored?
     */
    protected boolean ignore = true;


    // --------------------------------------------------------- Public Methods


    /**
     * Take this filter out of service.
     */
    public void destroy() {

        this.encoding = null;
        this.filterConfig = null;

    }


    /**
     * Select and set (if specified) the character encoding to be used to
     * interpret request parameters for this request.
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException {

        // Conditionally select and set the character encoding to be used

        HttpServletRequest t_re = (HttpServletRequest) request;

        String re_uri = trimString(t_re.getRequestURI());
        String re_host = trimString(t_re.getServerName());
        String qu_str = trimString(t_re.getQueryString());


        if (re_uri.indexOf(".do") > 0)
        {
          if (log.isDebugEnabled())
            log.debug(re_host + re_uri + "?" + qu_str);
            /*
          try {
            int c_length = request.getContentLength();
            String str_length = (String) t_re.getSession().getAttribute(
                "file_max_length");
            if (str_length != null && str_length.length() > 0) {
              //            System.out.println("session getted at filter");
              fileMaxLength = net.strong.util.MyUtil.getStringToInt(str_length, 200) *
                  1024; //即str_length K
            }

            if (c_length > fileMaxLength) { //12k
              HttpServletResponse t_res = (HttpServletResponse) response;
              String str_error = "";
              str_error = createErrorMessage("文件太大啦", null);
              writeErrorMessage(t_res, str_error);
              log.debug("当前文件大小:" +
                        c_length);
              return;
            }
          }
          catch (Exception e) {
            log.debug("处理文件大小判断时出错：" + e.getMessage());
//            System.out.println("exception at file,处理文件大小判断时出错：" + e.getMessage());
          }
*/
        }

/*

//       HttpServletRequest t_re = (HttpServletRequest) request;
        if (re_uri.indexOf(".do") > 0)
        {
          log.info("开始过滤");
        }
*/
       try
        {

          if (ignore || (request.getCharacterEncoding() == null)) {
            String encoding = selectEncoding(request);
            if (encoding != null)
              request.setCharacterEncoding(encoding);
          }
          // Pass control on to the next filter
          chain.doFilter(request, response);
        }
        catch(Exception e)
        {
//          e.printStackTrace();
          String error_mes = e.getMessage();
          String strServerName = null;
          String strQueryString = null;
          String requestUrl = null;
          String remoteAddr = null;
          if(error_mes.indexOf("reset")<0 )
          {
            String referer = "";
            try {
              strServerName = t_re.getServerName();
              strQueryString = t_re.getQueryString();
              requestUrl = t_re.getRequestURI();
 //             strQueryString = CodeTransfer.ISOToUnicode(strQueryString);
              referer = t_re.getHeader("referer");
              remoteAddr = t_re.getRemoteAddr();
            }
            catch (Exception e1) {}
            if (error_mes != null && !"null".equals(error_mes)&& error_mes.length() > 0) {
              log.error(remoteAddr + ",exception:" +
                                 error_mes);
//              System.out.println("exception at filter:" +
//                                 error_mes);
            }
            if (referer != null && referer.length() > 0) {
              log.error("referer:"+referer);
//              System.out.println("referer at filter:" + referer);
            }
            if(strServerName!=null && strQueryString!=null)
            {
              log.error("filter ," + strServerName +
                                 " , " + strQueryString+" , "+requestUrl);
//              System.out.println("filter ," + strServerName +
//                                 " , " + strQueryString+" , "+requestUrl);
            }
          }
        }

    }


    /**
     * Place this filter into service.
     *
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        String file_max_length = filterConfig.getInitParameter("file_max_length");
        if(file_max_length!=null && file_max_length.length() >0)
        {
          fileMaxLength = net.strong.util.MyUtil.getStringToInt(file_max_length,200)*1024;
        }

        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Select an appropriate character encoding to be used, based on the
     * characteristics of the current request and/or filter initialization
     * parameters.  If no character encoding should be set, return
     * <code>null</code>.
     * <p>
     * The default implementation unconditionally returns the value configured
     * by the <strong>encoding</strong> initialization parameter for this
     * filter.
     *
     * @param request The servlet request we are processing
     */
    protected String selectEncoding(ServletRequest request) {

        return (this.encoding);

    }
    private String trimString(String value)
    {
      String result = "";
      if(value !=null)
        result = value.trim();

      return result;
    }

    String createErrorMessage(String message,String referer)
    {
      if(message==null)
        message = "";
      message = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><BODY>\n" +
          "<SCRIPT LANGUAGE=\"JavaScript\">\n" +
          "<!--\n" +
          "alert(\"" + message + "\");\n" ;
//      if(referer==null || referer.length() <1)
        message += "history.go(-1);\n";
//      else
//        message += "location.href=\"" + referer + "\";\n" +
        message+=   "//-->" +
            "</SCRIPT>\n" +
            "</BODY>";
/*
          "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body leftmargin=0 topmargin=0>&nbsp;&nbsp;<font style='font-size:12px'>" +
          message + "</font> <input type=button value='返回上传界面' onclick='javascript:history.go(-1)' style='font-size:12px'></body>";
*/
      return message;
    }

    //向客户端打印出错信息
    protected void writeErrorMessage( HttpServletResponse httpServletResponse,String message)
    {
      try
      {
        httpServletResponse.setHeader("accept-language","UTF-8");
         PrintWriter write = httpServletResponse.getWriter();
         String str_mes = message;//"您不能向自己购买！";
         str_mes = net.strong.util.CodeTransfer.UnicodeToISO(str_mes);
//       String str = "<body leftmargin=0 topmargin=0>&nbsp;&nbsp;<font style='font-size:12px'>" +str_mes+ "</font> <input type=button value='返回上传界面' onclick='javascript:history.go(-1)' style='font-size:12px'></body>";
         write.write(str_mes );
         write.flush();
         write.close();
      }
      catch(Exception e)
      {
        log.error(e.getMessage());
//        System.out.println("exception at buypostAction :打印信息时出错");
      }
    }

}

