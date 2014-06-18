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
 * $Id: WebLucenePropertiesPreprocessor.java,v 1.1 2004/10/30 09:31:11 lhelper Exp $
 */

package net.strong.weblucene;

import net.strong.properties.AbstractPropertiesPreprocessor;

import org.apache.log4j.Logger;

import java.io.File;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;


/**
 * The WebLucenePropertiesPreprocessor class is a preprocessor for weblucene
 * properties, it can replace the virtual relative path to real path for
 * example.
 */
public class WebLucenePropertiesPreprocessor
    extends AbstractPropertiesPreprocessor {
    //~ Static fields/initializers ---------------------------------------------

    /** The global logger, it will be configured when the servlet loaded */
    private static Logger logger = Logger.getLogger(WebLucenePropertiesPreprocessor.class
                                                    .getName()
                                                   );

    //~ Instance fields --------------------------------------------------------

    /** the context of the servlet */
    private ServletContext servletContext;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WebLucenePropertiesPreprocessor object.
     */
    public WebLucenePropertiesPreprocessor() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * preprocess the properties
     *
     * @param type - the identifier of the properties
     * @param properties - the properties need preprocess
     */
    public void preprocess(String type, Properties properties) {
        logger.info("trying to preprocess properties: " + type);

        if (type.equals("weblucene")) {
        } else if (type.equals("log4j")) {
            Enumeration names = properties.propertyNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();

                if (name.matches("log4j.appender.*.File")) {
                    String file = properties.getProperty(name);
                    properties.setProperty(name, _getRealPath(file));
                }
            }
        } else if (!type.equals("log4j")) {
            String file = properties.getProperty("HtmlTemplate");

            if (file != null) {
                properties.setProperty("HtmlTemplate", _getRealPath(file));
            }

            file = properties.getProperty("RssTemplate");

            if (file != null) {
                properties.setProperty("RssTemplate", _getRealPath(file));
            }

            file = properties.getProperty("DirRealPath");

            if (file != null) {
                properties.setProperty("DirRealPath", _getRealPath(file));
            }
        }

        logger.info("finished preprocessing properties: " + type);
    }

    /**
     * get the real path of the file/directory represented by path
     *
     * @param path a <code>String</code> value - the virtual path
     *
     * @return the real path
     */
    private String _getRealPath(String path) {
        if (path == null) {
            return null;
        }

        if (File.separator.equals("/")) {
            if (!path.startsWith("/")) {
                return servletContext.getRealPath(path);
            }
        } else if (File.separator.equals("\\")) {
            if (!path.matches("^[a-zA-Z]+:.*")) {
                return servletContext.getRealPath(path);
            }
        }

        return path;
    }

    /**
     * Get the ServletContext value.
     *
     * @return the ServletContext value.
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Set the ServletContext value.
     *
     * @param servletContext The new ServletContext value.
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
