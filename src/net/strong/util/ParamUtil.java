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
 * $Id: ParamUtil.java,v 1.2 2004/10/30 09:51:00 lhelper Exp $
 */

package net.strong.util;

/**
 * Class ParamUtil is used to validate the validation of the parameter
 *
 * @author lhelper
 */
public class ParamUtil {
    //~ Constructors -----------------------------------------------------------

    /**
     * prevent the instance of this class
     */
    private ParamUtil() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * parse string into integer, if parse error return with default value
     *
     * @param stringNumber - string should be parsed
     * @param defaultValue - the default value
     *
     * @return int - the parsed integer number, or default integer if error
     *         happend
     */
    public static int getInt(String stringNumber, int defaultValue) {
        if (stringNumber == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(stringNumber);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * parse string into integer, return with default value if error happend
     * when parseing or the parsed number is le min
     *
     * @param stringNumber - string should be parsed
     * @param min - the lowest limit
     * @param defaultValue - the default value
     *
     * @return int - the parsed integer number, or default integer if error
     *         happend when  parseing or the parsed number is gt min
     */
    public static int getInt(String stringNumber, int min, int defaultValue) {
        try {
            int returnInt = getInt(stringNumber, defaultValue);

            if (returnInt >= min) {
                return returnInt;
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * parse string into integer, return with default value if error happend
     * when parseing, or if the parsed number is lt min or gt max
     *
     * @param stringNumber - string should be parsed
     * @param min - the lowest limit
     * @param max - the upper limit
     * @param defaultValue - the default value
     *
     * @return int - the parsed integer number, or default value if error
     *         happend when parseing, or if the parsed number is lt min or gt
     *         max
     */
    public static int getInt(String stringNumber, int min, int max,
                             int defaultValue
                            ) {
        try {
            int returnInt = getInt(stringNumber, min, defaultValue);

            if (returnInt <= max) {
                return returnInt;
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * validate the string, if the string is null then return the
     * defaultValue.trim() or return string.trim()
     *
     * @param input - the string should be validated
     * @param defaultValue - default value
     *
     * @return String - if the string is null then return the
     *         defaultValue.trim() or return string.trim()
     */
    public static String getString(String input, String defaultValue) {
        if (input == null) {
            return defaultValue.trim();
        }

        return input.trim();
    }

    /**
     * get a valid string
     *
     * @param input - the string should be validated
     * @param validValues - the valid category that the input should be
     * @param defaultValue - default value
     *
     * @return a valid string, when compare the parameter's value with the
     *         valid values, case is insensitive
     */
    public static String getString(String input, String[] validValues,
                                   String defaultValue
                                  ) {
        boolean caseSensitive = true;

        return getString(input, validValues, defaultValue, caseSensitive);
    }

    /**
     * get a valid string
     *
     * @param input - the string should be validate
     * @param validValues - the valid category that the input should be
     * @param defaultValue - a default value
     * @param caseSensitive -case sensitive or not when compare the
     *        paramerter's value with the valid values
     *
     * @return a valid string
     */
    public static String getString(String input, String[] validValues,
                                   String defaultValue, boolean caseSensitive
                                  ) {
        if (input == null) {
            return defaultValue;
        }

        if(validValues == null) {
        	return defaultValue;
        }

        if (caseSensitive == false) {
            for (int i = 0, n = validValues.length; i < n; i++) {
                if(input.equalsIgnoreCase(validValues[i])) {
                	return input;
                }
            }
        }

        for (int i = 0, n = validValues.length; i < n; i++) {
            if (input.equals(validValues[i])) {
                return input;
            }
        }

        return defaultValue;
    }
}
