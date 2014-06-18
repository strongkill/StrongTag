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
 * $Id: WebLucenePropertiesConsumer.java,v 1.1 2004/10/30 09:29:48 lhelper Exp $
 */

package net.strong.weblucene;

import java.util.Properties;

import net.strong.properties.SimplePropertiesConsumer;


/**
 * <code>WebLucenePropertiesConsumer</code> is an utility, it get different
 * application's property via <code>SimplePropertiesConsumer</code>
 *
 * @author <a href="lhelper@gmail.org">lhelper</a>
 */
public class WebLucenePropertiesConsumer {
    //~ Static fields/initializers ---------------------------------------------

    /** html kind of xslt template */
    public static final String HTML_TEMPLATE = "HtmlTemplate";

    /** rss kind of xslt template */
    public static final String RSS_TEMPLATE = "RssTemplate";

    /**
     * the name we use to identify the global configuration, the configuration
     * is stored with all applications' in a hashmap
     */
    private static final String GLOBAL_APP_NAME = "weblucene";

    /** the global logger */
    //private static Logger logger = Logger.getLogger(WebLucenePropertiesConsumer.class.getName());

    //~ Constructors -----------------------------------------------------------

    /**
     * WebLucenePropertiesConsumer is global singleton prevent create new
     * instance:
     */
    private WebLucenePropertiesConsumer() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * get the properties collection of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>Properties</code> value - the properties collection
     */
    public static Properties getProperties(String dir) {
        return SimplePropertiesConsumer.getProperties(dir);
    }

    /**
     * get the specified property of the application
     *
     * @param dir a <code>String</code> value - which application
     * @param propertyName a <code>String</code> value - which property
     *
     * @return a <code>String</code> value - the value
     */
    public static String getProperty(String dir, String propertyName) {
        return SimplePropertiesConsumer.getProperty(dir, propertyName);
    }

    /**
     * get the default encoding of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String</code> value - the default encoding
     */
    public static String getDefaultEncoding(String dir) {
        String encoding = SimplePropertiesConsumer.getProperty(dir,
                                                               "DefaultEncoding"
                                                              );

        if (encoding == null) {
            encoding = SimplePropertiesConsumer.getProperty(GLOBAL_APP_NAME,
                                                            "DefaultEncoding"
                                                           );
        }

        return encoding;
    }

    /**
     * get the valid encodings property.
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String[]</code> value - the valid encodings
     */
    public static String[] getValidEncodings(String dir) {
        String[] validEncodings = SimplePropertiesConsumer.getStringArray(dir,
                                                                          "ValidEncodings",
                                                                          "[,]"
                                                                         );

        if ((validEncodings == null) || (validEncodings.length == 0)) {
            validEncodings = SimplePropertiesConsumer.getStringArray(GLOBAL_APP_NAME,
                                                                     "ValidEncodings",
                                                                     "[,]"
                                                                    );
        }

        return validEncodings;
    }

    /**
     * get the default index field name of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String</code> value - the default index field
     */
    public static String getDefaultIndexName(String dir) {
        String indexName = SimplePropertiesConsumer.getProperty(dir,
                                                                "DefaultIndex"
                                                               );

        if (indexName == null) {
            indexName = SimplePropertiesConsumer.getProperty(GLOBAL_APP_NAME,
                                                             "DefaultIndex"
                                                            );
        }

        return indexName;
    }

    /**
     * get the valid index field names
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String[]</code> value - the valid index field names
     */
    public static String[] getValidIndexes(String dir) {
        String[] validIndexes = SimplePropertiesConsumer.getStringArray(dir,
                                                                        "ValidIndexes",
                                                                        "[,]"
                                                                       );

        if ((validIndexes == null) || (validIndexes.length == 0)) {
            validIndexes = SimplePropertiesConsumer.getStringArray(GLOBAL_APP_NAME,
                                                                   "ValidIndexes",
                                                                   "[,]"
                                                                  );
        }

        return validIndexes;
    }

    /**
     * get the default output format of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String</code> value - the default output format
     */
    public static String getDefaultOutputFormat(String dir) {
        String outputFormat = SimplePropertiesConsumer.getProperty(dir,
                                                                   "DefaultOutputFormat"
                                                                  );

        if (outputFormat == null) {
            outputFormat = SimplePropertiesConsumer.getProperty(GLOBAL_APP_NAME,
                                                                "DefaultOutputFormat"
                                                               );
        }

        return outputFormat;
    }

    /**
     * get the valid output formats
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String[]</code> value - the valid output formats
     */
    public static String[] getValidOutputFormats(String dir) {
        String[] validOutputFormats = SimplePropertiesConsumer.getStringArray(dir,
                                                                              "ValidOutputFormats",
                                                                              "[,]"
                                                                             );

        if ((validOutputFormats == null) || (validOutputFormats.length == 0)) {
            validOutputFormats = SimplePropertiesConsumer.getStringArray(GLOBAL_APP_NAME,
                                                                         "ValidOutputFormats",
                                                                         "[,]"
                                                                        );
        }

        return validOutputFormats;
    }

    /**
     * get the default order style of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String</code> value - the default order style
     */
    public static String getDefaultOrderStyle(String dir) {
        String orderStyle = SimplePropertiesConsumer.getProperty(dir,
                                                                 "DefaultOrderStyle"
                                                                );

        if (orderStyle == null) {
            orderStyle = SimplePropertiesConsumer.getProperty(GLOBAL_APP_NAME,
                                                              "DefaultOrderStyle"
                                                             );
        }

        return orderStyle;
    }

    /**
     * get the valid order styles
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String[]</code> value - the valid order styles
     */
    public static String[] getValidOrderStyles(String dir) {
        String[] validOrderStyles = SimplePropertiesConsumer.getStringArray(dir,
                                                                            "ValidOrderStyles",
                                                                            "[,]"
                                                                           );

        if ((validOrderStyles == null) || (validOrderStyles.length == 0)) {
            validOrderStyles = SimplePropertiesConsumer.getStringArray(GLOBAL_APP_NAME,
                                                                       "ValidOrderStyles",
                                                                       "[,]"
                                                                      );
        }

        return validOrderStyles;
    }

    /**
     * get the showable fields of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String[]</code> value - the fields
     */
    public static String[] getShowFields(String dir) {
        String[] fields = SimplePropertiesConsumer.getStringArray(dir,
                                                                  "ShowFields",
                                                                  "[,]"
                                                                 );

        if ((fields == null) || (fields.length == 0)) {
            fields = SimplePropertiesConsumer.getStringArray(GLOBAL_APP_NAME,
                                                             "ShowFields", "[,]"
                                                            );
        }

        return fields;
    }

    /**
     * get the highlighting fields of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String[]</code> value - the highlighting fields
     */
    public static String[] getHighlightFields(String dir) {
        String[] fields = SimplePropertiesConsumer.getStringArray(dir,
                                                                  "HighlightFields",
                                                                  "[,]"
                                                                 );

        if ((fields == null) || (fields.length == 0)) {
            fields = SimplePropertiesConsumer.getStringArray(GLOBAL_APP_NAME,
                                                             "HighlightFields",
                                                             "[,]"
                                                            );
        }

        return fields;
    }

    /**
     * get the real path of the index library of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String</code> value - the real path
     */
    public static String getDirRealPath(String dir) {
        return SimplePropertiesConsumer.getProperty(dir, "DirRealPath");
    }

    /**
     * get the expires minutes of the specified application
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>long</code> value - the expires minutes
     */
    public static long getExpiresMinutes(String dir) {
        long expires = SimplePropertiesConsumer.getLong(dir, "ExpiredMinutes",
                                                        -1
                                                       );

        if (expires == -1) {
            expires = SimplePropertiesConsumer.getLong(GLOBAL_APP_NAME,
                                                       "ExpiredMinutes", 60
                                                      );
        }

        return expires;
    }

    /**
     * get the xslt template's full path
     *
     * @param dir a <code>String</code> value - which application
     * @param type a <code>String</code> value - which kind of template
     *
     * @return a <code>String</code> value - the real path of the xslt template
     */
    public static String getXsltTemplate(String dir, String type) {
        String xsltTemplate = SimplePropertiesConsumer.getProperty(dir, type);

        if (xsltTemplate == null) {
            xsltTemplate = SimplePropertiesConsumer.getProperty(GLOBAL_APP_NAME,
                                                                type
                                                               );
        }

        return xsltTemplate;
    }

    /**
     * get the customized highlight tag
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return a <code>String</code> value - the customized highlith tag
     */
    public static String getHighlightTag(String dir) {
        String highlightTag = SimplePropertiesConsumer.getProperty(dir,
                                                                   "HighlightTag"
                                                                  );

        if (highlightTag == null) {
            highlightTag = SimplePropertiesConsumer.getProperty(GLOBAL_APP_NAME,
                                                                "HighlightTag"
                                                               );
        }

        return highlightTag;
    }

    /**
     * get the count limit of the total result
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return an <code>int</code> value - the count limit
     */
    public static int getTotalResultLimit(String dir) {
        int totalResultLimit = SimplePropertiesConsumer.getInt(dir,
                                                               "TotalResultLimit",
                                                               -1
                                                              );

        if (totalResultLimit == -1) {
            totalResultLimit = SimplePropertiesConsumer.getInt(GLOBAL_APP_NAME,
                                                               "TotalResultLimit",
                                                               100
                                                              );
        }

        return totalResultLimit;
    }

    /**
     * get the count limit of every page
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return an <code>int</code> value - the count limit
     */
    public static int getPerPageResultLimit(String dir) {
        int perPageResultLimit = SimplePropertiesConsumer.getInt(dir,
                                                                 "PerPageResultLimit",
                                                                 -1
                                                                );

        if (perPageResultLimit == -1) {
            perPageResultLimit = SimplePropertiesConsumer.getInt(GLOBAL_APP_NAME,
                                                                 "PerPageResultLimit",
                                                                 100
                                                                );
        }

        return perPageResultLimit;
    }

    /**
     * get the length limit of the result's summary
     *
     * @param dir a <code>String</code> value - which application
     *
     * @return an <code>int</code> value - the length limit
     */
    public static int getSummaryLength(String dir) {
        int contentLength = SimplePropertiesConsumer.getInt(dir,
                                                            "SummaryLength", -1
                                                           );

        if (contentLength == -1) {
            contentLength = SimplePropertiesConsumer.getInt(GLOBAL_APP_NAME,
                                                            "SummaryLength", 256
                                                           );
        }

        return contentLength;
    }
}
