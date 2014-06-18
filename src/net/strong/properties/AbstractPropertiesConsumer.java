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
 * $Id: AbstractPropertiesConsumer.java,v 1.1 2004/06/28 13:36:14 lhelper Exp $
 */

package net.strong.properties;

import java.util.Properties;


/**
 * a simple properties consumer, which acquire data from SimplePropertiesFactory
 *
 * @author <a href="mailto:lhelper@lhelper.org">lhelper</a> 
 * 
 * @see <code>AbstractPropertiesFactory</code>, <code>AbstractPropertiesSupplier</code>
 */
public class AbstractPropertiesConsumer {
    //~ Static fields/initializers -----------------------------------------------------------------

    /** select a concrete implementation of <code>AbstractPropertiesFactory</code> */
    private static AbstractPropertiesFactory factory = null;

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for AbstractPropertiesConsumer.
     */
    protected AbstractPropertiesConsumer() {
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * set concrete implementation of <code>AbstractPropertiesFactory</code>.
     *
     * @param afactory a <code>AbstractPropertiesFactory</code> value
     */
    public static synchronized void setPropertiesFactory(AbstractPropertiesFactory afactory) {
        factory = afactory;
    }

    /**
     * get the original value of desired attribute
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - name of property
     *
     * @return a <code>String</code> value - the original value of the desired property
     */
    public static String getProperty(String type, String propertyname) {
        Properties properties = factory.getProperties(type);

        if (properties != null) {
            return properties.getProperty(propertyname);
        }

        return null;
    }

    /**
     * get the origianl value of desirec attribute
     *
     * @param type a <code>String</code> value - which properties collection
     * @param propertyname a <code>String</code> value - name of property
     * @param defaultvalue a <code>String</code> value
     *
     * @return a <code>String</code> value - the origianl value of the desired property, or
     *         defaultvalue if no such a attribute
     */
    public static String getProeprty(String type, String propertyname, String defaultvalue) {
        Properties properties = factory.getProperties(type);

        if (properties == null) {
            return defaultvalue;
        }

        String property = properties.getProperty(propertyname);

        if (property == null) {
            return defaultvalue;
        }

        return property;
    }

    /**
     * get the properties collection
     *
     * @param type - which properties collection
     *
     * @return Properties - a properties collection
     */
    public static Properties getProperties(String type) {
        return factory.getProperties(type);
    }
}
