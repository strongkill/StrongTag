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
 * $Id: WebLuceneSearcherBase.java,v 1.1 2004/10/30 09:26:01 lhelper Exp $
 */

package net.strong.weblucene.search;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.TokenMgrError;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.mira.lucene.analysis.MIK_CAnalyzer;

/**
 * <p>
 * The WebLuceneSearcherBase class is a searcher based on IndexOrderSearcher,
 * it can transforms the search result to WebLuceneResultSet.
 * </p>
 * 
 * <p>
 * WebLuceneSearcherBase use IndexOrderSearcher fetch Hits (search result), and
 * transform Hits to WebLuceneResultSet.
 * </p>
 */
public class WebLuceneSearcherBase {
    //~ Static fields/initializers ---------------------------------------------

    /** get the global logger */
    private static final Logger logger = Logger.getLogger(WebLuceneSearcherBase.class
                                                          .getName()
                                                         );

    /*
     * StandardAnalyzer is used only for sigle byte char, it's not
     * fit of chinese, so we should use CJKTokenizer replace the
     * default StandardTokenizer
     */

    /** token analyser */
    private static Analyzer analyzer = new MIK_CAnalyzer();

    //~ Instance fields --------------------------------------------------------

    /** index library's real path */
    private String indexPath = "";

    /** index name */
    private String indexName = "";

    /** order mode */
    private String orderStyle = "";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WebLuceneSearcherBase object.
     *
     * @param indexPath - the real path of the index library
     * @param indexName - the index's name
     * @param orderStyle - order mode
     */
    public WebLuceneSearcherBase(String indexPath, String indexName,
                                 String orderStyle
                                ) {
        this.indexPath = indexPath;
        this.indexName = indexName;
        this.orderStyle = orderStyle;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Return a piece of search result in form of WebLuceneResultSet.
     * 
     * <ol>
     * <li>
     * create an IndexOrderSearcher according to orderStyle;
     * </li>
     * <li>
     * fetch Hits with IndexOrderSearcher;
     * </li>
     * <li>
     * transform hits[offset] to hits[offset+pagesize] to WebLuceneResultSet;
     * </li>
     * </ol>
     * 
     *
     * @param keywords - keywords or clause to search
     * @param offset - start point of the result
     * @param pagesize - result size
     * @param totalResultLimit - total result limit
     *
     * @return a piece of record from offset to offset+pagesize
     *
     * @throws IOException - failed to reade the index
     * @throws ParseException - failed to parse the keywords
     */
    public WebLuceneResultSet search(String keywords, int offset, int pagesize,
                                     int totalResultLimit
                                    ) throws IOException, ParseException {
        Searcher searcher = null;

        try {
            /* construct a searcher according to orderStyle */
            int sortType = IndexOrderSearcher.ORDER_BY_DOCID_DESC;

            if (orderStyle.toLowerCase().equals("docid")) {
                sortType = IndexOrderSearcher.ORDER_BY_DOCID_DESC;
            } else if (orderStyle.toLowerCase().equals("score")) {
                sortType = IndexOrderSearcher.ORDER_BY_SCORE;
            }

            searcher = new IndexSearcher(indexPath);
        } catch (IOException ioe) {
            logger.error("Failed to create searcher: " + ioe.toString());

            if (searcher != null) {
                searcher.close();
                searcher = null;
            }

            throw (ioe);
        }

        try {
            QueryParser queryParser = new QueryParser(indexName,
                                                                  analyzer
                                                                 );

            /* create an Query with the query string in "contents" default */
            Query query = queryParser.parse(keywords);

            /* Begin to search, get the search result Hits */
            logger.info("trying to get hits for: " + query.toString(indexName));

            Hits hits = searcher.search(query);

            logger.info("get hits successfully!");

            logger.info("trying to transform hits[" + offset + "-"
                        + (offset + pagesize) + "]"
                       );

            WebLuceneResultSet resultset = new WebLuceneResultSet(hits, offset,
                                                                  pagesize,
                                                                  totalResultLimit
                                                                 );
            logger.info("hits transformed successfully!");

            return resultset;
        } catch (ParseException pe) {
            logger.error("Failed to parse: " + keywords + "\n" + pe.toString());
            throw (pe);
        } catch (TokenMgrError tme) {
            logger.error("Failed to token: " + keywords + "\n" + tme.toString());

            ParseException pe = new ParseException(tme.getMessage());
            pe.setStackTrace(tme.getStackTrace());
            throw (pe);
        } catch (IOException ioe) {
            logger.error("Failed to search: " + keywords + "\n"
                         + ioe.toString()
                        );
            throw (ioe);
        } finally {
            if (searcher != null) {
                searcher.close();
                searcher = null;
            }
        }
    }
}
