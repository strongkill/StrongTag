/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Lucene" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Lucene", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * $Id: WebLuceneServlet.java,v 1.8 2004/10/30 09:32:04 lhelper Exp $
 */

package net.strong.weblucene;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;

import net.strong.util.RequestParser;
import net.strong.weblucene.search.DOMSearcher;
import net.strong.weblucene.search.WebLuceneQuery;
import net.strong.xslt.XsltCache;


/**
 * <p>
 * interface for search, which parse keywords from request, search them from
 * index library, and send the transformed result to web client. 
 * </p>
 * <p>Note:
 * <li>the search result is in form of xml(see <a href="/doc/weblucene_results.dtd"> 
 * weblucene_results.dtd</a> for detail), inorder to get html, rss form of result 
 * we use xslt template(see <a href="/WEB-INF/var/blog/html.xsl"> html.xsl</a> 
 * for detail).</li>
 * </p>
 *
 */
public class WebLuceneServlet extends HttpServlet {
    //~ Instance fields --------------------------------------------------------

    /* Attributes declaration */

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The global logger, it will be configured when the servlet loaded */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    //~ Methods ----------------------------------------------------------------

    /**
     * initialize the servlet
     *
     * @param servletConfig the ServletConfig object that contains
     *        configutation information for this servlet Throws:
     *
     * @exception ServletException - if an exception occurs that interrupts the
     *            servlet's normal operation See Also:
     *
     * @see UnavailableException
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        /* load the servlet */
        super.init(servletConfig);

        logger.info("WebLuceneServlet Initialize Successfully!");
    }

    /**
     * @see destroy in interface Servlet
     */
    public void destroy() {
        super.destroy();
        logger.info("WebLuceneServlet Destroy Successfully!");
    }

    /**
     * <p>
     * <ol>
     * <li>parse the HTTP/GET request, construct WebLuceneQuery with the
     * parameters;</li>
     * <li>search the keywords from index library, return result in form of xml
     * document;</li>
     * <li>transform search result to desired form with xslt template</li>
     * </ol>
     * </p>
     *
     * @param request - an HttpServletRequest object that contains the request
     *        the client has made of the servlet
     * @param response - an HttpServletResponse object that contains the
     *        response the servlet sends to the client
     *
     * @throws IOException - if an input or output error is detected when the
     *         servlet handles the GET request
     * @throws ServletException - if the request for the GET could not be
     *         handled
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
                        ) throws IOException, ServletException {
        logger.info("doGet get a HTTP/GET request.");

        /*
         * 'dir': which index library you want to search from,
         * the 'dir' is so important that nothing is significant if it's invalidate.
         * if that happens, sendError(404) to the client.
         */
        String dir = RequestParser.getString(request, "dir", "");

        if ((dir == null) || (WebLucenePropertiesConsumer.getProperties(dir) == null)) {
            String error = "index of '" + dir
                           + "' is not found or it's under maintainning!";
            logger.error(error);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);

            return;
        }

        /*
         * parse the request, encapsulate the parameters into a <code>WebLuceneQuery</code>.
         * the <code>WebLuceneQuery</code> works like a bean.
         */
        WebLuceneQuery queryBean = initWebLuceneQuery(request);

        /*
         * 'keywords': what you want to search
         * the secondary important parameter, if it's empty, show just a blank page, like google
         */
        String keywords = queryBean.getQueryString();

        if ((keywords == null) || keywords.trim().equals("")) {
            logger.error("keywords should not be empty!");
            showBlankPage(response, queryBean);
            logger.error("show a friendly blank page");

            return;
        }

        /*
         * try to search the keywords from the library, send pretty result to the client,
         * or an error page when some controlable exception happens
         */
        try {
            /*
             * get the search result, the returned result is a xml document which
             * is converted from hits(the original type of search result)
             */
            Document result = DOMSearcher.search(queryBean);
            showNormalPage(response, result, queryBean);
        } catch (Exception e) {
            logger.error(e.toString());
            showErrorPage(response, queryBean, e);
            logger.error("show a friendly error page");

            return;
        }

        logger.info("doGet has handled the request successfully.");
    }

    /**
     * <p>
     * parse the parameters in the HTTP/GET request, the parameters, such as
     * queryString, offset, pageSize etc. are used to construct the queryBean
     * </p>
     *
     * @param request - an HttpServletRequest object that contains the request
     *        the client has made of the servlet
     *
     * @return parsed queryBean
     *
     * @throws ServletException - if the request for the GET could not be
     *         handled
     */
    private WebLuceneQuery initWebLuceneQuery(HttpServletRequest request)
                                       throws ServletException {
        /* construct the queryBean */
        WebLuceneQuery queryBean = new WebLuceneQuery();

        /*
         * dir: which index library to search from.
         * if dirName is null, assign "news" to it;
         * if getProperties(dirName) is null, that is, the index libary is not found,
         * or the library is under maintainning, so raise IndexNotFoundException
         */
        String dirName = RequestParser.getString(request, "dir", "news");
        queryBean.setDirName(dirName);

        /*
         * q: keywords to search.
         * if queryString is null, raise NullQueryException
         */
        /*
         * encoding: charset of the request
         */
        String[] validEncodings = WebLucenePropertiesConsumer.getValidEncodings(dirName);
        String defaultEncoding = WebLucenePropertiesConsumer.getDefaultEncoding(dirName);
        String encoding = RequestParser.getString(request, "encoding",
                                                  validEncodings,
                                                  defaultEncoding,
												  RequestParser.CASE_INSENSITIVE
                                                 );
        queryBean.setEncoding(encoding.toLowerCase());

        String queryString = RequestParser.getString(request, "q", "");

        try {
            queryString = new String(queryString.getBytes("iso-8859-1"),
                                     encoding
                                    );
        } catch (UnsupportedEncodingException uee) {
            queryString = RequestParser.getString(request, "q", "");
        }

        queryBean.setQueryString(queryString);

        /*
         * start: offset of the first record of the current page in the resultset.
         * offset must be between 0 and 99, default is 0
         */
        int totalResultLimit = WebLucenePropertiesConsumer.getTotalResultLimit(dirName);
        int offset = RequestParser.getInt(request, "start", 0, totalResultLimit, 0);
        queryBean.setStart(offset);

        /*
         * num: how many record should be included in the current page
         * num must be between 1 and 100, default is 10
         */
        int perPageResultLimit = WebLucenePropertiesConsumer.getPerPageResultLimit(dirName);
        int number = RequestParser.getInt(request, "num", 1, perPageResultLimit, 10);
        queryBean.setPageSize(number);

        /*
         * the following properties should be initialized correctly
         * read the property from the request first
         * if its null, read the property from config file
         * if its null too, set default value to the property
         */
        /*
         * index: index field
         */
        String[] validIndexes = WebLucenePropertiesConsumer.getValidIndexes(dirName);
        String defaultIndex = WebLucenePropertiesConsumer.getDefaultIndexName(dirName);
        String indexName = RequestParser.getString(request, "index",
                                                   validIndexes, defaultIndex,
                                                   RequestParser.CASE_SENSITIVE
                                                  );
        queryBean.setIndexName(indexName);

        /*
         * orderStyle: order style
         */
        String[] validOrderStyles = WebLucenePropertiesConsumer.getValidOrderStyles(dirName);
        String defaultOrderStyle = WebLucenePropertiesConsumer.getDefaultOrderStyle(dirName);
        String orderStyle = RequestParser.getString(request, "orderStyle",
                                                    validOrderStyles,
                                                    defaultOrderStyle,
													RequestParser.CASE_INSENSITIVE
                                                   );
        queryBean.setOrderStyle(orderStyle.toLowerCase());

        /*
         * outputFormat: output format, it's relavant to the remote ip address
         */
        String[] validOFs = WebLucenePropertiesConsumer.getValidOutputFormats(dirName);
        String defaultOF = WebLucenePropertiesConsumer.getDefaultOutputFormat(dirName);
        String outputFormat = RequestParser.getString(request, "outputFormat",
                                                      validOFs, defaultOF,
													  RequestParser.CASE_INSENSITIVE
                                                     );
        queryBean.setOutputFormat(outputFormat.toLowerCase());

        /*
         * highLightFields: which fields needs highlighted
         * if the output format was set to xml, disable the high light process,
         * in order to avoid garbled html tag
         */
        String[] highLightFields = WebLucenePropertiesConsumer.getHighlightFields(dirName);
        HashMap<String, String> highLightFieldsMap = new HashMap<String, String>();
        if (!outputFormat.toLowerCase().equals("xml")) {
            for (int i = 0; i < highLightFields.length; i++) {
                highLightFieldsMap.put(highLightFields[i], "true");
            }
        }

        queryBean.setHighlightFields(highLightFieldsMap);

        /* record the information of the request */
        logger.info("The content of the request is : " + queryBean);

        return queryBean;
    }

    /**
     * <p>
     * show a friendly blank page when the keywords is empty.
     * </p>
     *
     * @param response - output stream
     * @param queryBean - query parameter
     *
     * @throws IOException - uncontrolable i/o exception
     */
    public void showBlankPage(HttpServletResponse response,
                              WebLuceneQuery queryBean
                             ) throws IOException {
        /* assemble a xml document with some simple information */
        String dir = queryBean.getDirName();
        String encoding = queryBean.getEncoding();
        String index = queryBean.getIndexName();
        String order = queryBean.getOrderStyle();
        String format = queryBean.getOutputFormat().toUpperCase();

        Document document = new Document(new Element("WebLuceneResultProtocol"));
        Element wlrp = document.getRootElement();
        wlrp.setAttribute("ver", "1.0");
        wlrp.addContent(new Element("DirName").addContent(dir));
        wlrp.addContent(new Element("Encoding").addContent(encoding));
        wlrp.addContent(new Element("IndexName").addContent(index));
        wlrp.addContent(new Element("OrderStyle").addContent(order));
        wlrp.addContent(new Element("OutputFormat").addContent(format));
        wlrp.addContent(new Element("Exception").addContent("keywords should not be empty!"));

        /* set the expires time */
        long now = System.currentTimeMillis();
        response.addDateHeader("Expires", now);
        response.addDateHeader("Last-Modified", now);
        response.addHeader("Cache-control", "no-cache");

        /*
         * use showResult() transform the xml document to desired form (html, rss, etc.)
         */
        showResult(response, document, queryBean);
    }

    /**
     * <p>
     * show a friendly error page when some controlable exception happens.
     * </p>
     *
     * @param response - output stream
     * @param queryBean - query parameters
     * @param e - the exception
     *
     * @throws IOException - uncontrolable i/o exception
     */
    public void showErrorPage(HttpServletResponse response,
                              WebLuceneQuery queryBean, Exception e
                             ) throws IOException {
        /* prepare a document, which contains the exception information */
        String dir = queryBean.getDirName();
        String keywords = queryBean.getQueryString();
        String encoding = queryBean.getEncoding();
        String index = queryBean.getIndexName();
        String order = queryBean.getOrderStyle();
        String format = queryBean.getOutputFormat().toUpperCase();

        Document document = new Document(new Element("WebLuceneResultProtocol"));
        Element wlrp = document.getRootElement();
        wlrp.setAttribute("ver", "1.0");
        wlrp.addContent(new Element("Query").addContent(keywords));
        wlrp.addContent(new Element("DirName").addContent(dir));
        wlrp.addContent(new Element("Encoding").addContent(encoding));
        wlrp.addContent(new Element("IndexName").addContent(index));
        wlrp.addContent(new Element("OrderStyle").addContent(order));
        wlrp.addContent(new Element("OutputFormat").addContent(format));
        wlrp.addContent(new Element("When").addContent(new Date().toString()));

        String exception = e.getClass().getName();
        String message = "";

        if (e.getMessage() != null) {
            message = e.getMessage().replaceAll("'", "\"");
        }

        StackTraceElement[] stack = e.getStackTrace();
        StringBuffer errorContent = new StringBuffer();
        errorContent.append(e.toString());
        errorContent.append("\n");

        for (int i = 0; i < (stack.length - 1); i++) {
            errorContent.append(stack[i].toString());
            errorContent.append(" at\n");
        }

        if (stack.length > 0) {
            errorContent.append(stack[stack.length - 1].toString());
        }

        String stacktrace = errorContent.toString().replaceAll("'", "\"");
        wlrp.addContent(new Element("Exception").addContent(exception));
        wlrp.addContent(new Element("Message").addContent(message));
        wlrp.addContent(new Element("StackTrace").addContent(stacktrace));

        /* set the expires headers */
        long now = System.currentTimeMillis();
        response.addDateHeader("Expires", now);
        response.addDateHeader("Last-Modified", now);
        response.addHeader("Cache-control", "no-cache");

        /*
         * use showResult() transform the xml document to desired form
         */
        showResult(response, document, queryBean);
    }

    /**
     * <p>
     * show a normal result page in form of html(, xml, rss etc.)
     * </p>
     *
     * @param response - output stream
     * @param document - the search result
     * @param queryBean - query parameters
     *
     * @throws IOException - uncontrolable i/o exception
     */
    private void showNormalPage(HttpServletResponse response,
                                Document document, WebLuceneQuery queryBean
                               ) throws IOException {
        /* add Expires and Last-Modified for caching */
        long expiredMinutes = WebLucenePropertiesConsumer.getExpiresMinutes(queryBean
                                                                   .getDirName()
                                                                  );
        long now = System.currentTimeMillis();
        long expire = now + (1000 * 60 * expiredMinutes);
        response.addDateHeader("Expires", expire);
        response.addDateHeader("Last-Modified", now);
        response.addHeader("Cache-control", "public");

        /*
         * use showResult() transform the xml document to desired form
         */
        showResult(response, document, queryBean);
    }

    /**
     * <p>
     * transform the search result into desired type, such as Html, Xml, Rss or
     * Pdf etc.
     * </p>
     *
     * @param response - output stream
     * @param document - the xml document
     * @param queryBean - query parameters
     *
     * @throws IOException - uncontrolable i/o exception
     */
    private void showResult(HttpServletResponse response, Document document,
                            WebLuceneQuery queryBean
                           ) throws IOException {
        String format = queryBean.getOutputFormat().toUpperCase();
        String encoding = queryBean.getEncoding();
        String dir = queryBean.getDirName();
        OutputStream out = response.getOutputStream();

        if (format.equals("HTML")) {
            /* transform the xml document to Html */
            response.setContentType("text/html; charset=" + encoding);

            String xslType = WebLucenePropertiesConsumer.HTML_TEMPLATE;
            String xslName = WebLucenePropertiesConsumer.getXsltTemplate(dir, xslType);
            Properties outputProperties = new Properties();
            outputProperties.setProperty(OutputKeys.ENCODING, encoding);
            showResultInXSLT(out, document, xslName, outputProperties);
        } else if (format.equals("XML")) {
            /* transform the xml document to Xml */
            response.setContentType("text/xml; charset=" + encoding);

            showResultInXML(out, document, encoding);
        } else if (format.equals("RSS")) {
            /* transform the xml document to Rss */
            response.setContentType("text/rss; charset=" + encoding);

            String xslType = WebLucenePropertiesConsumer.RSS_TEMPLATE;
            String xslName = WebLucenePropertiesConsumer.getXsltTemplate(dir, xslType);
            Properties outputProperties = new Properties();
            outputProperties.setProperty(OutputKeys.ENCODING, encoding);
            showResultInXSLT(out, document, xslName, outputProperties);
        }
    }

    /**
     * <p>
     * output XML data to the client without transforming with xslt template.
     * </p>
     *
     * @param outputStream - an HttpServletResponse object that contains the
     *        response the servlet sends to the client
     * @param xmlDocument - the search result in form of org.w3c.dom.Document
     * @param encoding - an HttpServletRequest object that contains the request
     *        the client has made of the servlet
     */
    private void showResultInXML(OutputStream outputStream,
                                 Document xmlDocument, String encoding
                                ) {
        XMLOutputter xmlOutputter = new XMLOutputter("\t", true, encoding);

        try {
            xmlOutputter.output(xmlDocument, outputStream);
        } catch (IOException ignore) {
            logger.error(ignore.getMessage());
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException ignore) {
                logger.error(ignore.getMessage());
            }
        }
    }

    /**
     * <p>
     * transform the search result into HTML(, RSS or PDF etc.) with xslt
     * template
     * </p>
     *
     * @param outputStream - an HttpServletResponse object that contains the
     *        response the servlet sends to the client
     * @param xmlDocument - the search result in form of org.w3c.dom.Document
     * @param xslName - xsl template name
     * @param outputProperties - properties used to transform the xml document
     */
    private void showResultInXSLT(OutputStream outputStream,
                                  Document xmlDocument, String xslName,
                                  Properties outputProperties
                                 ) {
        /* set xml output to servlet response output stream */
        StreamResult result = new StreamResult(outputStream);

        /* prepare to transform the Document to html with XSLT */
        /* which source should be transformed */
        JDOMSource source = new JDOMSource(xmlDocument);

        try {
            /* initial a Transformer */
            Transformer transformer = XsltCache.newTransformer(xslName);

            if (outputProperties != null) {
                transformer.setOutputProperties(outputProperties);
            }

            /* transform source to result */
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            /* uncontrolable exception, nothing can be done to make up for it */
            logger.error("Caught error : " + tce.toString());
        } catch (TransformerException te) {
            /* uncontrolable exception, nothind can be done to make up for it */
            logger.error("Caught error : " + te.toString());
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException ignore) {
                logger.error(ignore.getMessage());
            }
        }
    }
}
