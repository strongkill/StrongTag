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
 * $Id: WebLuceneQuery.java,v 1.5 2004/10/30 09:21:21 lhelper Exp $
 */

package net.strong.weblucene.search;

import java.util.HashMap;


/**
 * <p>
 * The WebLuceneQuery class represents a set of parameters, which is related to
 * the query request, such as keywords, offset, result size, highlight fields
 * etc.
 * </p>
 */
public class WebLuceneQuery {
    //~ Instance fields --------------------------------------------------------

    /** parameters from the WebLuceneServlet */
    /** the offset of current result */
    private int start = 0;

    /** the pageSize of result per page */
    private int pageSize = 10;

    /** the query string */
    private String queryString = "";

    /** the encode method of the query string */
    private String encoding = "UTF-8";

    /** the index library's name */
    private String dirName = "index";

    /** the name of the index */
    private String indexName = "FullIndex";

    /** the search result should be sort by orderStyle */
    private String orderStyle = "DocID";

    /** the output format(xml/html/xhtml) */
    private String outputFormat = "HTML";

    /** the fields should be highlight */
    private HashMap highlightFields = new HashMap();

    //~ Constructors -----------------------------------------------------------

    /**
     * <p>
     * construct WebLuceneSearcher
     * </p>
     */
    public WebLuceneQuery() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns a string representation of this WebLuceneQuery object in the
     * form of a set of entries, enclosed in braces and separated by the ASCII
     * characters "; " (comma and space).  Each entry is rendered as the
     * field, an equals sign =, and the value
     *
     * @return the formated string
     */
    public String toString() {
        /* the seprator of the output string */
        String seprator = "; ";
        StringBuffer sb = new StringBuffer();

        sb.append("start = " + start);
        sb.append(seprator);

        sb.append("pageSize = " + pageSize);
        sb.append(seprator);

        sb.append("queryString = " + queryString);
        sb.append(seprator);

        sb.append("encoding = " + encoding);
        sb.append(seprator);

        sb.append("dirName = " + dirName);
        sb.append(seprator);

        sb.append("orderStyle = " + orderStyle);
        sb.append(seprator);

        sb.append("outputFormat = " + outputFormat);
        sb.append(seprator);

        sb.append("highlightFields = " + highlightFields);
        sb.append(seprator);

        return sb.toString();
    }

    /**
     * get the index libray's name
     *
     * @return a <code>String</code> value
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * set the index library's name
     *
     * @param dirName - the index library's name
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    /**
     * get the encoding method of the query string
     *
     * @return a <code>String</code> value
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * set the encoding method of the query string
     *
     * @param encoding - the encoding method of the query string
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * get a set of fields that need highlight
     *
     * @return a <code>HashMap</code> value
     */
    public HashMap getHighlightFields() {
        return highlightFields;
    }

    /**
     * set the highlighting fields
     *
     * @param highlightFields The highlightFields to set.
     */
    public void setHighlightFields(HashMap highlightFields) {
        this.highlightFields = highlightFields;
    }

    /**
     * get the index's name
     *
     * @return Returns the indexName.
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * set the index's name
     *
     * @param indexName The indexName to set.
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * get the desired order style
     *
     * @return Returns the orderStyle.
     */
    public String getOrderStyle() {
        return orderStyle;
    }

    /**
     * set the desired order style
     *
     * @param orderStyle The orderStyle to set.
     */
    public void setOrderStyle(String orderStyle) {
        this.orderStyle = orderStyle;
    }

    /**
     * get the desired output format
     *
     * @return Returns the outputFormat.
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * set the desired output format
     *
     * @param outputFormat The outputFormat to set.
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * get the desired result size
     *
     * @return Returns the pageSize.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * set the desired result size
     *
     * @param pageSize The pageSize to set.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * get the keywords to search
     *
     * @return Returns the queryString.
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * set the keywords to search
     *
     * @param queryString The queryString to set.
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * get the desired offset
     *
     * @return Returns the start.
     */
    public int getStart() {
        return start;
    }

    /**
     * set the desired offset
     *
     * @param start The start to set.
     */
    public void setStart(int start) {
        this.start = start;
    }
}
