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
 * $Id: WebLuceneAdminServlet.java,v 1.5 2004/10/30 09:33:24 lhelper Exp $
 */

package net.strong.weblucene;

import net.strong.properties.AbstractPropertiesConsumer;
import net.strong.properties.AbstractPropertiesFactory;
import net.strong.properties.FileBasedPropertiesSupplier;
import net.strong.properties.PropertyFileFilenameFilter;
import net.strong.properties.SimpleFilenameDigester;
import net.strong.properties.SimplePropertiesFactory;

import net.strong.util.RequestParser;

import net.strong.weblucene.WebLucenePropertiesConsumer;

import net.strong.xslt.XsltCache;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.apache.lucene.index.IndexReader;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * WebLuceneAdminServlet is an online admin console, with which you can monitor
 * the configurations, reload them, reload the xslt templates, manage the
 * index, etc. and more function will be extended in the future.
 * </p>
 */
public class WebLuceneAdminServlet extends HttpServlet
    implements SingleThreadModel {
    //~ Static fields/initializers ---------------------------------------------

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The global logger, it will be configured when the servlet loaded */
    private static Logger logger = Logger.getLogger(WebLuceneAdminServlet.class
                                                    .getName()
                                                   );

    //~ Instance fields --------------------------------------------------------

    /** The time stamp for last modification */
    private Date lastModify = new Date();

    //~ Methods ----------------------------------------------------------------

    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being taken out of service.
     *
     * @see destroy in interface Servlet
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * <p>
     * Show configurations, reload them, manage the index and so on
     * </p>
     *
     * @param request - the request
     * @param response - the response
     *
     * @throws IOException - if an input or output error is detected when the
     *         servlet handles the GET request
     * @throws ServletException - if the request for the GET could not be
     *         handled
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
                        ) throws IOException, ServletException {
        SimplePropertiesFactory factory = SimplePropertiesFactory.getInstance();

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>WebLucene Admin Console</title>");
        out.println("</head>");

        out.println("<body>");
        out.println("<table width='100%'>");
        out.println("<tr>");
        out.println("<td valign='top' style='background-color:#e0e0e0; width:184px;' nowrap>");
        out.println("<h4>What Can I do?</h4>");
        out.println("<ol>");
        out.println("<li><a href='?action=show-status'>Show Configurations</a></li>");
        out.println("<li><a href='?action=reload'>Reload Configurations</a></li>");
        out.println("<li><a href='?action=delete'>Delete a Document</a></li>");
        out.println("<li>Optimize the Index</li>");

        /* out.println("<li><a href='?action=optimize'>Optimize the Index</a></li>"); */
        out.println("<li><a href='/'>Go to Home Pgae</a></li>");
        out.println("</ol>");
        out.println("</td>");
        out.println("<td width='4'>&nbsp;</td>");
        out.println("<td valign='top'>");

        String action = RequestParser.getString(request, "action", "");

        if (!action.equals("")) {
            if (action.equals("reload")) {
                /* reload the configurations */
                logger.info("begin to reload global configurations...");
                factory.reactivateAllPropertiesSuppliers();
                logger.info("configurations reloaded successfully!");
 
                /* reset log options */
                Properties logProperties = AbstractPropertiesConsumer.getProperties("log4j");
                PropertyConfigurator.configure(logProperties);
 
               /* clean the templates stored in the cache */
                logger.info("begin to release all xslt templates...");
                XsltCache.flushAll();
                logger.info("xslt template released successfully!");

                out.println("configurations and xslt templates reloaded sucessfully!");
                lastModify = new Date();
            } else if (action.equals("delete")) {
                String indexLib = RequestParser.getString(request, "lib", "");
                String indexDir = WebLucenePropertiesConsumer.getDirRealPath(indexLib);
                int docId = RequestParser.getInt(request, "docid", -1);

                if ((indexDir != null) && (docId != -1)) {
                    /* delete a document with the doc-id */
                    logger.info("trying to delete the document with docId="
                                + docId + " in " + indexLib
                               );

                    try {
                        if (!IndexReader.isLocked(indexDir)) {
                            IndexReader reader = IndexReader.open(indexDir);
                            reader.deleteDocument(docId);

                            if (reader.isDeleted(docId)) {
                                logger.info("document with docId=" + docId
                                            + " in " + indexLib
                                            + " was deleted successfully!"
                                           );
                                out.println("Document with docId=" + docId
                                            + " in " + indexLib
                                            + " was deleted successfully!"
                                           );
                            }

                            reader.close();
                        } else {
                            logger.info("unable to delete document with docId: "
                                        + docId + "--" + indexLib
                                        + " was locked!"
                                       );
                            out.println(indexLib + " WAS LOCKED! NOTHIND DONE!");
                        }
                    } catch (IOException ioe) {
                        logger.error("faild to delete the document with docId: "
                                     + docId + " in " + indexLib + ": "
                                     + ioe.getMessage()
                                    );
                    }
                } else {
                    if (indexDir == null) {
                        logger.info("failed to delete document from invalid index lib: "
                                    + indexLib
                                   );
                        out.println("INVALID INDEX LIB! NOTHING DONE!");
                    } else {
                        logger.info("failed to delete the document, the doc-id is invalid");
                        out.println("INVALID DOCID! NOTHING DONE!");
                    }
                    out.println("<br>Usage: admin?action=delete&lib=$LIB&docid=$DOCID");
                }
            }
            /**
             * not implemented now(may be it's dangerous to optimize index
             * online)  else if(action.equals("optimize")) { String
             * indexLib=RequestParser.getString(request, "lib", "");
             * if(!indexLib.equals("")) { logger.info("trying to optimize
             * index lib: " + indexLib); logger.info("index lib: " + indexLib
             * + " was optimized successfully!"); out.println("Index Lib: " +
             * indexLib + " was optimized successfully!"); } else {
             * logger.info("failed to optimize the invalid index lib: " +
             * indexLib ); out.println("INVALID INDEX LIB: " + indexLib + ",
             * NOTHING DONE"); }}
             */
            else if (!action.equals("show-status")) {
                out.println("INVALID OPTION: " + action);
            }
        }

        if (action.equals("show-status") || action.equals("reload")
                || action.equals("")
           ) {
            out.println("<h3>Configurations: (Last Modified:");
            out.println(lastModify.toString());
            out.println(")</h3>");
            out.println("<hr>");

            /* show the state of all appllications */
            Set types = factory.getPropertyTypes();
            Iterator iterator = types.iterator();

            while (iterator.hasNext()) {
                String type = (String) iterator.next();

                out.println("<h4>" + type + "</h4>");

                if (WebLucenePropertiesConsumer.getProperties(type) != null) {
                    Properties properties = WebLucenePropertiesConsumer
                                            .getProperties(type);
                    Enumeration proNames = properties.propertyNames();

                    while (proNames.hasMoreElements()) {
                        String name = (String) proNames.nextElement();
                        out.println(name + " : " + properties.getProperty(name)
                                    + "<br>"
                                   );
                    }
                }
            }
        }

        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

        /* flush the buffer */
        out.flush();
        out.close();
    }

    /**
     * <p>
     * Initialize the webapp on-start-up.
     * </p>
     * 
     * <ol>
     * <li>
     * Initialize the logger, log4j for example
     * </li>
     * <li>
     * Load configurations from config files
     * </li>
     * </ol>
     * 
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
        super.init(servletConfig);

        try {
            System.out.println("Beginning to initialize WebLuceneAdminServlet...");

            /* read the config file, initialize the logger */
            ArrayList pathList = new ArrayList();
            String file = getInitParameter("log4j.conf");
            pathList.add(file);

            String paths = getInitParameter("properties.path");
            StringTokenizer tokenizer = new StringTokenizer(paths);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                String realPath = getServletContext().getRealPath(token);
                pathList.add(realPath);
            }

            ArrayList suffixList = new ArrayList();
            suffixList.add(".properties");
            suffixList.add(".conf");

            PropertyFileFilenameFilter filter = new PropertyFileFilenameFilter(suffixList);

            SimpleFilenameDigester digester = new SimpleFilenameDigester();

            WebLucenePropertiesPreprocessor preprocessor = new WebLucenePropertiesPreprocessor();
            preprocessor.setServletContext(servletConfig.getServletContext());

            FileBasedPropertiesSupplier supplier = new FileBasedPropertiesSupplier(pathList,
                                                                                   filter,
                                                                                   digester,
                                                                                   preprocessor
                                                                                  );

            AbstractPropertiesFactory factory = SimplePropertiesFactory
                                                .getInstance();

            factory.addPropertiesSupplier(supplier);

            AbstractPropertiesConsumer.setPropertiesFactory(factory);

            /* initialize the logger */
            Properties logProperties = AbstractPropertiesConsumer.getProperties("log4j");
            PropertyConfigurator.configure(logProperties);

            /* clean the templates stored in the cache */
            XsltCache.flushAll();

            lastModify = new Date();

            System.out.println("WebLuceneAdminServlet initial successfully!");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            System.out.println("WebLuceneAdminServlet initial failed!");
        }
    }
}
