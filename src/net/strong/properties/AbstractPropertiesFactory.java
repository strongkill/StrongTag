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
 * $Id: AbstractPropertiesFactory.java,v 1.1 2004/06/28 13:36:14 lhelper Exp $
 */

package net.strong.properties;

import java.util.Properties;
import java.util.Set;


/*
 * the abstract defination of SimplePropertiesFactory, which get properties from
 * <code>AbstractPropertisSupplier</code> and provide them to 
 * <code>AbstractPropertiesConsumer</code>, eg:
 *
 *<code>AbstractPropertiesSupplier</code>
 *            |
 *            |supply properties for
 *            v
 *<code>AbstractPropertiesFactory</code>
 *            ^
 *            |get properties from
 *            |
 *<code>AbstractPropertiesConsumer</code>
 *
 * Created: Sun May 16 18:54:36 2004
 *
 * @author <a href="mailto:lhelper@lhelper.org">lhelper</a>
 */
public abstract class AbstractPropertiesFactory {
    //~ Methods ------------------------------------------------------------------------------------

    /**
     * select an <code>AbstractPropertiesSupplier</code>, which digest the properties resource and
     * fill the properties into the properties map.
     *
     * @param supplier an <code>AbstractPropertiesSupplier</code> value - the properties supplier
     *
     * @exception Exception if an error occurs
     */
    public abstract void addPropertiesSupplier(AbstractPropertiesSupplier supplier)
                                        throws Exception;

    /**
     * <code>AbstractPropertiesSupplier</code> supply properties with it.
     *
     * @param type an <code>Object</code> value - the identifier of the properties collection.
     * @param properties a <code>Properties</code> value - the properties
     */
    public abstract void acceptProperties(Object type, Properties properties);

    /**
     * <code>AbstractPropertiesConsumer</code> get properties with it.
     *
     * @param type an <code>Object</code> value - the identifier of the properties collection
     *
     * @return a <code>Properties</code> value - the properties.
     */
    public abstract Properties getProperties(Object type);

    /**
     * get the key set of properties container
     *
     * @return <code>Set</code> - the key set
     */
    public abstract Set getPropertyTypes();
}
 // AbstractPropertiesFactory
