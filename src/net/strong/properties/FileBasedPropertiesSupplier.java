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
 * $Id: FileBasedPropertiesSupplier.java,v 1.2 2004/10/30 09:42:36 lhelper Exp $
 */

package net.strong.properties;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * File based Properties Supplier. This supplier picks-up properties from config files, and submit
 * the properties to <code>SimplePropertiesFactory</code>.
 *
 * @author <a href="mailto:lhelper@lhelper.org">lhelper</a>   
 */
public class FileBasedPropertiesSupplier extends AbstractPropertiesSupplier {
    //~ Static fields/initializers -----------------------------------------------------------------

    /** the global logger */
    private static Logger logger = Logger.getLogger(FileBasedPropertiesSupplier.class.getName());

    //~ Instance fields ----------------------------------------------------------------------------

    /**
     * a common property file filter, which can determine which file should be added into the
     * property files list
     */
    private FilenameFilter filter = null;

    /** directories or files that contains the properties */
    private List paths = null;

    /** default properties type digester */
    private AbstractPropertiesTypeDigester digester = null;

    /** properties pre-processor */
    private AbstractPropertiesPreprocessor preprocessor = null;

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Creates a new <code>FileBasedPropertiesSupplier</code> instance.
     *
     * @param paths a <code>List</code> value
     * @param filter a <code>PropertyFileFilenameFilter</code> value
     * @param digester an <code>AbstractPropertiesTypeDigester</code> value
     */
    public FileBasedPropertiesSupplier(List paths, PropertyFileFilenameFilter filter,
                                       AbstractPropertiesTypeDigester digester
                                       ) {
        this(paths, filter, digester, null);
    }

    /**
     * another constructor, the specified property type digester will be used.
     *
     * @param paths a <code>List</code> value
     * @param filter DOCUMENT ME!
     * @param digester an <code>AbstractPropertiesTypeDigester</code> value
     */
    public FileBasedPropertiesSupplier(List paths, PropertyFileFilenameFilter filter,
                                       AbstractPropertiesTypeDigester digester, 
									   AbstractPropertiesPreprocessor preprocessor
                                      ) {
        this.paths = paths;

        if (filter != null) {
            this.filter = filter;
        } else {
            this.filter = new PropertyFileFilenameFilter();
        }

        if (digester != null) {
            this.digester = digester;
        } else {
            this.digester = new SimpleFilenameDigester();
        }
        this.preprocessor = preprocessor;
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Describe <code>supplyFor</code> method here.
     *
     * @param factory a <code>SimplePropertiesFactory</code> value
     *
     * @exception IOException if an error occurs
     */
    public void supplyFor(SimplePropertiesFactory factory)
                   throws IOException {
        if (paths == null) {
            return;
        }

        /** build a list of property files */
        List<String> filelist = new ArrayList<String>();
        Iterator ite = paths.iterator();

        while (ite.hasNext()) {
            String filename = (String) ite.next();
            File file = new File(filename);

            if (file.isFile()) {
                filelist.add(filename);
            } else if (file.isDirectory()) {
                File[] files = file.listFiles(filter);

                for (int j = 0; j < files.length; j++) {
                    filelist.add(files[j].getAbsolutePath());
                }
            }
        }

        /** load properties from the files in the list */
        Iterator ite2 = filelist.iterator();

        while (ite2.hasNext()) {
            String filename = (String) ite2.next();
            Properties properties = load(filename);
            String type = digester.getType(filename);

            if (properties == null) {
                logger.error("can not load properties from " + filename);
            }

            if (preprocessor != null) {
                preprocessor.preprocess(type, properties);
            }

            logger.info("pick up configurations from file (" + filename
                         + ") successfully with the following properties: " + properties.toString()
                         );

            factory.acceptProperties(type, properties);
        }
    }

    /**
     * Describe <code>_load</code> method here.
     *
     * @param filename a<code>File</code> value
     *
     * @return Properties - the properties collection
     *
     * @exception IOException if an error occurs
     */
    private static synchronized Properties load(String filename)
                                          throws IOException {
        Properties properties = new Properties();

        try {
            FileInputStream in = new FileInputStream(filename);
            properties.load(in);
            in.close();
            Enumeration propertyNames = properties.propertyNames();
            while(propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                properties.setProperty(propertyName, properties.getProperty(propertyName).trim());
            }
        } catch (FileNotFoundException fnfe) {
            logger.error(fnfe.getMessage());
            throw (fnfe);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
            throw (ioe);
        }

        if (properties == null) {
            logger.error("can not load properties from " + filename);
        }

        return properties;
    }

    /**
     * Sets the digester.
     *
     * @param digester The digester to set
     */
    public void setDigester(AbstractPropertiesTypeDigester digester) {
        this.digester = digester;
    }
}

