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
 * $Id: RequestParser.java,v 1.2 2004/10/30 09:50:06 lhelper Exp $
 */

package net.strong.util;

import javax.servlet.http.HttpServletRequest;


/**
 * <code>RequestParser</code> is a utility that can used to parser the http
 * request.
 *
 * @author lhelper
 */
public class RequestParser {
	public static boolean CASE_SENSITIVE = true;

	public static boolean CASE_INSENSITIVE = false;

	//~ Constructors -----------------------------------------------------------

    /**
     * Constructor for RequestParser.
     */
    private RequestParser() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * get a simple parameter
     *
     * @param request - http request
     * @param key - the name of the parameter
     *
     * @return the simple parameter's value
     */
    public static String getParameter(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }

    /**
     * get a alternate parameter
     *
     * @param request - http request
     * @param key - the name of the parameter
     * @param defaultValue - the default value
     *
     * @return the parameter's value or default value
     */
    public static int getInt(HttpServletRequest request, String key,
                             int defaultValue
                            ) {
        String str = request.getParameter(key);

        return ParamUtil.getInt(str, defaultValue);
    }

    /**
     * get a int value
     *
     * @param request - http request
     * @param key - the name of the parameter
     * @param min - the lower limit
     * @param defaultValue - a default value
     *
     * @return a int value
     */
    public static int getInt(HttpServletRequest request, String key, int min,
                             int defaultValue
                            ) {
        String str = request.getParameter(key);

        return ParamUtil.getInt(str, min, defaultValue);
    }

    /**
     * get a int value
     *
     * @param request - http request
     * @param key - the name of the parameter
     * @param min - the lower limit
     * @param max - the upper limit
     * @param defaultValue - a default value
     *
     * @return a int value
     */
    public static int getInt(HttpServletRequest request, String key, int min,
                             int max, int defaultValue
                            ) {
        String str = request.getParameter(key);

        return ParamUtil.getInt(str, min, max, defaultValue);
    }

    /**
     * get a string
     *
     * @param request - http request
     * @param key - the name of the parameter
     * @param defaultValue - a default value
     *
     * @return a string
     */
    public static String getString(HttpServletRequest request, String key,
                                   String defaultValue
                                  ) {
        String str = request.getParameter(key);

        return ParamUtil.getString(str, defaultValue);
    }

    /**
     * get a valid string
     *
     * @param request - http request
     * @param key - the name of the parameter
     * @param validValues - the category that the desired parameter can be
     * @param defaultValue - a default value
     *
     * @return a valid string, when compare the parameter's value with the
     *         valid values, case is insensitive
     */
    public static String getString(HttpServletRequest request, String key,
                                   String[] validValues, String defaultValue
                                  ) {
        boolean caseSensitive = false;

        return getString(request, key, validValues, defaultValue, caseSensitive);
    }

    /**
     * get a valid string
     *
     * @param request - http request
     * @param key - the name of the parameter
     * @param validValues - the category that the desird parameter can be
     * @param defaultValue - a default value
     * @param caseSensitive - case sensitive or not when compare the
     *        paramerter's value with the valid values
     *
     * @return a valid string
     */
    public static String getString(HttpServletRequest request, String key,
                                   String[] validValues, String defaultValue,
                                   boolean caseSensitive
                                  ) {
        String str = request.getParameter(key);

        return ParamUtil.getString(str, validValues, defaultValue, caseSensitive);
    }
}
