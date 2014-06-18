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
 * $Id: PropertyFileFilenameFilter.java,v 1.1 2004/06/28 13:36:14 lhelper Exp $
 */

package net.strong.properties;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.List;


/**
 * determine what kind of file match the filter. this impelementation determines whether the file
 * match the filter by the suffix of the file name, ".properties", by default. Created: Thu May 13
 * 20:54:03 2004
 *
 * @author <a href="mailto:lhelper@lhelper.org">lhelper</a> 
 */
public class PropertyFileFilenameFilter implements FilenameFilter {
    //~ Static fields/initializers -----------------------------------------------------------------

    /** the default filter (suffix of config files) */
    public static final String DEFAULT_PROPERTYFILE_SUFFIX = ".properties";

    //~ Instance fields ----------------------------------------------------------------------------

    /** the acceptable filename suffixes */
    private List<String> suffixes = new ArrayList<String>();

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * accept files whose filename ends with <code>DEFAULT_PROPERTYFILE_SUFFIX</code>
     */
    public PropertyFileFilenameFilter() {
        suffixes.add(DEFAULT_PROPERTYFILE_SUFFIX);
    }

    /**
     * accept files whose filename ends with the specified suffixes
     *
     * @param suffixes - the allowed suffixes collection
     */
    public PropertyFileFilenameFilter(List<String> suffixes) {
        this.suffixes = suffixes;
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * is the filename is acceptable or not.
     *
     * @param file a <code>File</code> value
     * @param name a <code>String</code> value
     *
     * @return a <code>boolean</code> value - return true if the filename ends with one of the
     *         suffixes list, false else.
     */
    public boolean accept(File file, String name) {
        for (int i = suffixes.size(); i > 0; i--) {
            if (name.endsWith(((String) suffixes.get(i - 1)).trim())) {
                return true;
            }
        }

        return false;
    }
}
