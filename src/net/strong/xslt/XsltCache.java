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
 * $Id: XsltCache.java,v 1.2 2004/05/29 20:23:41 chedong Exp $
 */

package net.strong.xslt;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;


/**
 * A utility class that caches XSLT stylesheets in memory.  Simplified from
 * O'Reilly online book:
 * http://www.onjava.com/pub/a/onjava/excerpt/java_xslt_ch5/index.html?page=9#ss_cache
 *
 * @author Che, Dong
 */
public class XsltCache {
    //~ Static fields/initializers ---------------------------------------------

    /** map xslt file names to Templates instances */
    private static Map<String, Templates> cache = new HashMap<String, Templates>();

    //~ Constructors -----------------------------------------------------------

    /**
     * prevent instantiation of this class
     */
    private XsltCache() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Flush all cached stylesheets from memory, emptying the cache.
     */
    public static synchronized void flushAll() {
        cache.clear();
    }

    /**
     * Flush a specific cached stylesheet from memory.
     *
     * @param xsltFileName the file name of the stylesheet to remove.
     */
    public static synchronized void flush(String xsltFileName) {
        cache.remove(xsltFileName);
    }

    /**
     * Obtain a new Transformer instance for the specified XSLT file name. A
     * new entry will be added to the cache if this is the first request for
     * the specified file name.
     *
     * @param xsltFileName the file name of an XSLT stylesheet.
     *
     * @return Transformer a transformation context for the given stylesheet.
     *
     * @throws TransformerConfigurationException transformer exception
     */
    public static synchronized Transformer newTransformer(String xsltFileName)
        throws TransformerConfigurationException {
        Templates tpl = null;

        if (cache.containsKey(xsltFileName)) {
            tpl = (Templates) cache.get(xsltFileName);
        } else {
            // create a new template in the cache if necessary
            Source xslSource = new StreamSource(xsltFileName);
            TransformerFactory transFact = TransformerFactory.newInstance();
            tpl = transFact.newTemplates(xslSource);
            cache.put(xsltFileName, tpl);
        }

        return tpl.newTransformer();
    }
}
