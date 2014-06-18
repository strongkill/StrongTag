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
 * $Id: DOMSearcher.java,v 1.8 2004/10/30 09:24:27 lhelper Exp $
 */

package net.strong.weblucene.search;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.strong.weblucene.WebLucenePropertiesConsumer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.jdom.Element;
import org.mira.lucene.analysis.MIK_CAnalyzer;


/**
 * <p>
 * The DOMSearcher is an universual index searcher, it can encapsulate the
 * search result in xml Document, with the Document you can yield various kind
 * of output (such as rss, html, xml and so on) use an appropriate xslt
 * template.
 * </p>
 * 
 * <p>
 * DOMSearcher fetch search result use WebLuceneSearcherBase(the result is in
 * form of WebLuceneResultSet), and transform the result into xml Document.
 * </p>
 */
public class DOMSearcher {
    //~ Static fields/initializers ---------------------------------------------

    /** get the global logger */
    private static final Logger logger = Logger.getLogger(DOMSearcher.class
                                                          .getName()
                                                         );

    /** token analyser */
    private static Analyzer analyzer = new MIK_CAnalyzer();

    //~ Constructors -----------------------------------------------------------

    /**
     * DOMSearcher is global singleton, prevent create new instance:
     */
    private DOMSearcher() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Return xml formed search result according to parameters in queryBean.
     * 
     * <ol>
     * <li>
     * fetch search result use <code>WebLuceneSearcherBase</code>
     * </li>
     * <li>
     * transform <code>WebLuceneResultSet</code> to xml document
     * </li>
     * </ol>
     * 
     *
     * @param queryBean - who knows the request information
     *
     * @return org.w3c.dom.Document - the search result converted from Hits
     *
     * @throws IOException - failed to read the index lib
     * @throws ParseException - failed to parse the keywords
     */
    public static final org.jdom.Document search(WebLuceneQuery queryBean)
                                          throws IOException, ParseException {
        String indexLib = queryBean.getDirName();
        String indexName = queryBean.getIndexName();
        String keywords = queryBean.getQueryString();
        String encoding = queryBean.getEncoding();
        String orderStyle = queryBean.getOrderStyle();
        String outputFormat = queryBean.getOutputFormat();
        int start = queryBean.getStart();
        int pagesize = queryBean.getPageSize();
        String indexPath = WebLucenePropertiesConsumer.getDirRealPath(indexLib);
        int totalResultLimit = WebLucenePropertiesConsumer.getTotalResultLimit(indexLib);

        logger.info("trying to get resultset for: " + keywords);

        long timeStart = System.currentTimeMillis();
        WebLuceneSearcherBase searcher;
        searcher = new WebLuceneSearcherBase(indexPath, indexName, orderStyle);

        WebLuceneResultSet resultSet;
        resultSet = searcher.search(keywords, start, pagesize, totalResultLimit);

        long timeUsed = System.currentTimeMillis() - timeStart;
        logger.info("get resultset successfully! (" + timeUsed
                    + " milliseconds used)"
                   );

        /* get docs's number and adjust the start, pagesize number */
        int total = resultSet.length();
        int end = resultSet.end();
        start = resultSet.start();

        logger.info("trying to transform resultset to document...");

        /* create a xml document */
        org.jdom.Document document = new org.jdom.Document(new Element("WebLuceneResultProtocol"));

        /* the root node of the output xml document */
        Element wlrp = document.getRootElement();
        wlrp.setAttribute("ver", "1.0");

        /* query (the child element of root): the query string */
        wlrp.addContent(new Element("Query").addContent(keywords));

        /* encoding (the child element of root): the encode method */
        wlrp.addContent(new Element("Encoding").addContent(encoding));

        /* query (the child element of root): the encoded query string */
        Element encodedQuery = new Element("UriEncodedQuery");

        try {
            String urlEncoded = URLEncoder.encode(keywords, encoding);
            encodedQuery.addContent(urlEncoded);
        } catch (UnsupportedEncodingException uee) {
            logger.error("Caught error :" + uee.toString());
            encodedQuery.addContent(keywords);
        }

        wlrp.addContent(encodedQuery);

        /* start (the child element of root): start offset */
        wlrp.addContent(new Element("Start").addContent(Integer.toString(start)));

        /* pageSize (the child element of root): page size */
        wlrp.addContent(new Element("Pagesize").addContent(Integer.toString(pagesize)));

        /* outputFormat (the child element of root): output format */
        wlrp.addContent(new Element("OutputFormat").addContent(outputFormat));

        /* dir (the child element of root): index dir name */
        wlrp.addContent(new Element("DirName").addContent(indexLib));

        /* index(the child element of root): index name */
        wlrp.addContent(new Element("IndexName").addContent(indexName));

        /* orderStyle(the child element of root): order method */
        wlrp.addContent(new Element("OrderStyle").addContent(orderStyle));

        /* timeUsed (the child element of root): how long does the search cost */
        wlrp.addContent(new Element("TimeUsed").addContent(Long.toString(timeUsed)));

        /* total (the child element of root): count of total result */
        wlrp.addContent(new Element("Total").addContent(Integer.toString(total)));

        /* totalResultLimie (the child element of root): total result limit */
        wlrp.addContent(new Element("TotalResultLimit").addContent(Integer.toString(totalResultLimit)));

        StringReader stringReader = new StringReader(keywords);
        TokenStream tokenStream = analyzer.tokenStream("", stringReader);
        ArrayList<String> tokens = new ArrayList<String>();

        for (Token token = tokenStream.next(); token != null;
                 token = tokenStream.next()
            ) {
            tokens.add(token.termText());
        }

        WebLuceneHighlighter highlighter = new WebLuceneHighlighter(tokens);
        String highlightTag = WebLucenePropertiesConsumer.getHighlightTag(indexLib);
        highlighter.setHighlightTag(highlightTag);

        int summaryLength = WebLucenePropertiesConsumer.getSummaryLength(indexLib);

        /* records(the child element of root): record set */
        Element records = new Element("RecordSet");

        for (int i = start; i < end; i++) {
            /* record(the child element of Records): record set */
            Element record = new Element("Record");
            record.setAttribute("score", String.valueOf(resultSet.score(i)));
            record.setAttribute("id", String.valueOf(resultSet.id(i)));

            //Enumeration fields = resultSet.doc(i).fields();
            
            List fields = resultSet.doc(i).getFields();
            //while (fields.hasMoreElements()) {
            while (fields!=null && fields.size()>0){
                /* field(the child element of Record): record set */
               // Field f = (Field) fields.nextElement();
            	Field f = (Field)fields.remove(0);
                String highlighted = f.stringValue();

                /* whether the field should be highlighted or not */
                if (queryBean.getHighlightFields().get(f.name()) != null) {
                    highlighted = highlighter.highlight(f.stringValue(),
                                                        summaryLength
                                                       );
                }

                record.addContent(new Element(f.name()).addContent(highlighted));
            }

            records.addContent(record);
        }

        wlrp.addContent(records);

        logger.info("resultset transformed successfully!");

        return document;
    }
}
