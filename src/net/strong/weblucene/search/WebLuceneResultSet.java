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
 * $Id: WebLuceneResultSet.java,v 1.5 2004/10/30 09:19:46 lhelper Exp $
 */

package net.strong.weblucene.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;

import java.io.IOException;

import java.util.ArrayList;


/**
 * <p>
 * WebLuceneResultSet is a VALUE OBJECT that used to store the Hits object --
 * the  search result of Searcher.search(). Hits, which is just a handler,
 * takes up the file handler of the full text index, and is not convenient for
 * cache. in order to cache the result, WebLuceneResultSet is designed.
 * </p>
 */
public class WebLuceneResultSet {
    //~ Instance fields --------------------------------------------------------

    /** use ArrayList store the Document in the Hits result */
    private ArrayList<Document> docs = new ArrayList<Document>();

    /** use score[] store the score of every record */
    private float[] score;

    /** store id of every record */
    private int[] docids;

    /** index of first record */
    private int start = 0;

    /** index of last record */
    private int end = 10;

    /** the count of the records in the Hits result */
    private int length = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * <p>
     * Construct a WebLuceneResultSet entry with search result Hits(from
     * hits[offset] to hits[offset+pagesize]).
     * </p>
     * 
     * <p>
     * pick up the results from hits[offset] to hits[offset+pagesize], the
     * offset and pagesize need preprocess as follow:
     * </p>
     * <pre>
     *  length = hits.length();
     *  start = offset;
     *  end = (length > totalResultLimit) ? totalResultLimit : length;
     *  if (start > end) {
     *      start = end - pagesize;
     *  }
     *  if (start < 0) {
     *      start = 0;
     *  }
     *  if ((start + pagesize) > end) {
     *      pagesize = end - start;
     *  }
     *  end = start + pagesize;
     * </pre>
     *
     * @param hits - the hits result to be stored
     * @param offset - index of the first record
     * @param pagesize - result size
     * @param totalResultLimit - total result limit
     *
     * @throws IOException - throws the caught Exceptions to invokers
     */
    public WebLuceneResultSet(Hits hits, int offset, int pagesize,
                              int totalResultLimit
                             ) throws IOException {
        /* the count of the records in the hits result */
        length = hits.length();

        /* start at which record */
        start = offset;

        /* end at which record */
        end = (length > totalResultLimit) ? totalResultLimit : length;

        if (start > end) {
            start = end - pagesize;
        }

        if (start < 0) {
            start = 0;
        }

        if ((start + pagesize) > end) {
            pagesize = end - start;
        }

        end = start + pagesize;

        /* reconstruct the array to fit for the hits result */
        score = new float[pagesize];
        docids = new int[pagesize];

        /* store result from hits[start] to hits[end] */
        for (int i = start; i < end; i++) {
            docs.add(hits.doc(i));
            score[i - start] = hits.score(i);
            docids[i - start] = hits.id(i);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * get the docId of hits(i)
     *
     * @param i - index of the hits(i)
     *
     * @return the docId
     */
    public int id(int i) {
        return docids[i - start];
    }

    /**
     * get the score of hits(i)
     *
     * @param i - index of the hits
     *
     * @return the score
     */
    public float score(int i) {
        return score[i - start];
    }

    /**
     * get the Document of hits(i)
     *
     * @param i - index of the hits
     *
     * @return the Document
     */
    public Document doc(int i) {
        return (Document) docs.get(i - start);
    }

    /**
     * the count of total result
     *
     * @return the count of total result
     */
    public int length() {
        return length;
    }

    /**
     * get the index of the first record stored in the current entry
     *
     * @return the index
     */
    public int start() {
        return start;
    }

    /**
     * get the index of the last record stored in the current entry
     *
     * @return the index
     */
    public int end() {
        return end;
    }
}
