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
 * $Id: SimplePropertiesFactory.java,v 1.2 2004/10/30 09:39:59 lhelper Exp $
 */

package net.strong.properties;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;


/**
 * a simple implementation of <code>AbstractPropertiesFactory</code>.
 * <code>SimplePropertiesFactory</code> is not realtimed, that is, the modification of the
 * property files can not take effect once <code>addPropertiesSupplier(AbstractPropertiesSupplier
 * supplier)</code> invoked, until <code>reactivateAllPropertiesSuppliers()</code> was broken up.
 *
 * @author <a href="mailto:lhelper@lhelper.org">lhelper</a>
 */
public class SimplePropertiesFactory extends AbstractPropertiesFactory {
    //~ Static fields/initializers -----------------------------------------------------------------

    /** the only instance of <code>SimplePropertiesFactory</code> */
    private static SimplePropertiesFactory singleton = null;

    /** the collections of properties */
    private static Map<Object, Properties> properties = null;

    /**
     * the container of <code>AbstractPropertiesSupplier</code>, use this container you can reload
     * the properties easily.
     */
    private static Vector<AbstractPropertiesSupplier> suppliers = null;

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * the inner constructor
     */
    private SimplePropertiesFactory() {
        properties = new HashMap<Object, Properties>();
        suppliers = new Vector<AbstractPropertiesSupplier>();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * the only interface that can be used to get the instance of
     * <code>SimplePropertiesFactory</code>, it's a singleton.
     *
     * @return a <code>SimplePropertiesFactory</code> value
     */
    public static synchronized SimplePropertiesFactory getInstance() {
        if (singleton == null) {
            singleton = new SimplePropertiesFactory();
        }

        return singleton;
    }

    /**
     * usually, this method is used by <code>AbstractPropertiesSupplier</code> to fill properties
     * into the factory.
     *
     * @param type an <code>Object</code> value - the identifier of the properties collection
     * @param properties a <code>Properties</code> value - the properties
     */
    public synchronized void acceptProperties(Object type, Properties properties) {
        singleton.properties.put(type, properties);
    }

    /**
     * <code>AbstractPropertiesConsumer</code> use this method to get desired properties
     *
     * @param type an <code>Object</code> value - the identifier of the properties collection
     *
     * @return a <code>Properties</code> value - the properties
     */
    public Properties getProperties(Object type) {
        return (Properties) singleton.properties.get((String) type);
    }

    /**
     * select an <code>AbstractPropertiesSupplier</code>, which digest the properties resource and
     * fill the properties into the properties map.
     *
     * @param supplier an <code>AbstractPropertiesSupplier</code> value - the properties supplier
     *
     * @exception IOException if an error occurs
     */
    public synchronized void addPropertiesSupplier(AbstractPropertiesSupplier supplier)
                                            throws IOException {
        suppliers.add(supplier);
        supplier.supplyFor(singleton);
    }

    /**
     * reload all the properties.
     *
     * @exception IOException if an error occurs
     */
    public synchronized void reactivateAllPropertiesSuppliers()
        throws IOException {
        singleton.properties.clear();

        for (int i = 0, n = suppliers.size(); i < n; i++) {
            AbstractPropertiesSupplier supplier = (AbstractPropertiesSupplier) suppliers.elementAt(i);
            supplier.supplyFor(singleton);
        }
    }

    /**
     * @see net.strong.properties.AbstractPropertiesFactory#getPropertyTypes()
     */
    public Set getPropertyTypes() {
        return properties.keySet();
    }
}
