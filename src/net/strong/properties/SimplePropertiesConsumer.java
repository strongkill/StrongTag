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
 * $Id: SimplePropertiesConsumer.java,v 1.2 2004/10/30 09:43:53 lhelper Exp $
 */

package net.strong.properties;

/**
 * a practical subclass of <code>AbstractPropertiesConsumer</code>, offers some useful function.
 *
 * @author <a href="mailto:lhelper@lhelper.org">lhelper</a>
 */
public class SimplePropertiesConsumer extends AbstractPropertiesConsumer {
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for SimplePropertiesConsumer.
     */
    private SimplePropertiesConsumer() {
        super();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * get desired <code>boolean</code> property.
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param defaultvalue a <code>boolean</code> value - the default value if the original value
     *        is null, etc.
     *
     * @return a <code>boolean</code> value - if the original value in the scope of 'true', 't',
     *         'yes', 'y' and 'on' (case insensitive), then return true, else flase; defaultvalue
     *         will be returned when the origianl value is invalid.
     */
    public static boolean getBoolean(String type, String propertyname, boolean defaultvalue) {
        String str = getProperty(type, propertyname);

        if (str == null) {
            return defaultvalue;
        }

        str = str.trim().toLowerCase();

        if (str.equals("true") || str.equals("t") || str.equals("yes") || str.equals("y")
                || str.equals("on")
           ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get desired <code>float</code> property
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param defaultvalue a <code>float</code> value - the default value if the original value is
     *        null, etc.
     *
     * @return a <code>float</code> value - defaultvalue will be returned when the origianl value
     *         is invalid.
     */
    public static float getFloat(String type, String propertyname, float defaultvalue) {
        String str = getProperty(type, propertyname);

        if (str == null) {
            return defaultvalue;
        }

        try {
            float res = Float.parseFloat(str.trim());

            return res;
        } catch (NumberFormatException nfe) {
            return defaultvalue;
        }
    }

    /**
     * get desired <code>int</code> property
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param defaultvalue a <code>int</code> value - the default value if the original value is
     *        null, etc.
     *
     * @return an <code>int</code> value - or defaultvalue if the origianl value is invalid.
     */
    public static int getInt(String type, String propertyname, int defaultvalue) {
        String str = getProperty(type, propertyname);

        if (str == null) {
            return defaultvalue;
        }

        try {
            int res = Integer.parseInt(str.trim());

            return res;
        } catch (NumberFormatException nfe) {
            return defaultvalue;
        }
    }

    /**
     * get desired <code>long</code> property
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param defaultvalue a <code>long</code> value - the default value if the original value is
     *        null, etc.
     *
     * @return an <code>long</code> value - or defaultvalue if the origianl value is invalid.
     */
    public static long getLong(String type, String propertyname, long defaultvalue) {
        String str = getProperty(type, propertyname);

        if (str == null) {
            return defaultvalue;
        }

        try {
            long res = Long.parseLong(str.trim());

            return res;
        } catch (NumberFormatException nfe) {
            return defaultvalue;
        }
    }

    /**
     * split the origianl value, add the valid items into a <code>fload</code> Array.
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param regexp a <code>String</code> value - the regular expression used to split the
     *        original value
     *
     * @return a <code>float[]</code> value - with valid float items(the invalid ones will not be
     *         included into the array), or float[0] if no such an attribute
     */
    public static float[] getFloatArray(String type, String propertyname, String regexp) {
        String[] arr = getStringArray(type, propertyname, regexp);
        float[] tmp = new float[arr.length];
        int j = 0;

        for (int i = 0; i < arr.length; i++, j++) {
            try {
                tmp[j] = Float.parseFloat(arr[i].trim());
            } catch (NumberFormatException nfe) {
                /* ommit the invalid item */
                j--;
            }
        }

        float[] res = new float[j];

        for (int i = 0; i < j; i++) {
            res[i] = tmp[i];
        }

        return res;
    }

    /**
     * split the origianl value, add the valid items into a <code>int</code> Array.
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param regexp a <code>String</code> value - the regular expression used to split the
     *        original value
     *
     * @return a <code>int[]</code> value - with valid int items(the invalid ones will not be
     *         included into the array), or int[0] if no such an attribute
     */
    public static int[] getIntArray(String type, String propertyname, String regexp) {
        String[] arr = getStringArray(type, propertyname, regexp);
        int[] tmp = new int[arr.length];
        int j = 0;

        for (int i = 0; i < arr.length; i++, j++) {
            try {
                tmp[j] = Integer.parseInt(arr[i].trim());
            } catch (NumberFormatException nfe) {
                /* ommit the invalid item */
                j--;
            }
        }

        int[] res = new int[j];

        for (int i = 0; i < j; i++) {
            res[i] = tmp[i];
        }

        return res;
    }

    /**
     * split the original value
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - the name of property
     * @param regexp a <code>String</code> value - the regular expression used to split the
     *        original value
     *
     * @return a <code>String[]</code> value - the split result
     */
    public static String[] getStringArray(String type, String propertyname, String regexp) {
        String str = getProperty(type, propertyname);

        if (str == null) {
            return new String[0];
        }

        String[] res = str.split(regexp);

        for (int i = 0; i < res.length; i++) {
            res[i] = res[i].trim();
        }

        return res;
    }
}
