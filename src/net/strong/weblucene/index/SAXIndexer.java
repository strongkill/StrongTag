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
 * $Id: SAXIndexer.java,v 1.5 2004/05/29 20:23:40 chedong Exp $
 */

package net.strong.weblucene.index;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


/**
 * Use Sax reader read xml source and built lucene index. the xml source format
 * as weblucene_index.dtd:
 *
 * @author Che, Dong
 * @version $Id: SAXIndexer.java,v 1.5 2004/05/29 20:23:40 chedong Exp $
 */
public final class SAXIndexer implements ContentHandler, ErrorHandler {
    //~ Instance fields --------------------------------------------------------

    /** logger */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /** SAX XML reader */
    private XMLReader saxReader = null; //xml sax reader

    /** Lucene index writer */
    private IndexWriter luceneIndexWriter = null; //IndexWriter

    /** document total counter */
    private int docTotalCounter = 0;

    /** report counter constant */
    private int reportCounterCons = 1000;

    /**
     * optimize counter: if doc exceed max optiCounterCons the indexWriter need
     * force luceneIndexWriter.optimize(); avoid too much file open error
     */
    private int optiCounterCons = 100000;

    /** optimizer counter */
    private int optiCounter = optiCounterCons;

    /** report counter */
    private int reportCounter = reportCounterCons;

    /** start of indexing time */
    private long startTime = System.currentTimeMillis();

    /** end of indexing time */
    private long endTime = 0;

    /** field name */
    private String fieldName = "";

    /** field value */
    private StringBuffer fieldValue = new StringBuffer();

    /** field store tag */
    private boolean storeTag = true;

    /** field indexing tag */
    private boolean indexTag = true;

    /** index need token tag */
    private boolean tokenTag = true;

    /** current Luene field */
    private Field luceneField = null;

    /** current Lucene document */
    private Document currentDoc = null;

    /** current xml doc tree level */
    private byte currentLevel = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * create XMLIndexer: xml sax reader and lucene index writer
     *
     * @param xmlReader sax based xml reader
     * @param indexWriter lucene index writer
     */
    public SAXIndexer(XMLReader xmlReader, IndexWriter indexWriter) {
        saxReader = xmlReader;
        luceneIndexWriter = indexWriter;

        //set content handler
        saxReader.setContentHandler(this);

        //set error handler
        saxReader.setErrorHandler(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns whether lucene index build successful complete
     *
     * @param src the xml input source.
     *
     * @return boolean: if build successful complete return true else return
     *         false.
     *
     * @throws SAXException sax exceptions
     */
    public boolean buildIndex(InputSource src) throws SAXException {
        try {
            if ((luceneIndexWriter != null) && (src != null)) {
                saxReader.parse(src);
                luceneIndexWriter.optimize();
                luceneIndexWriter.close();
                endTime = System.currentTimeMillis();
                System.out.println(docTotalCounter
                                   + " rows added\tTotal time Use:"
                                   + ((endTime - startTime) / 1000) + " second"
                                  );
            } else {
                return false;
            }

            return true;
        } catch (SAXException se) {
            logger.error("Failed with SAX error: " + se.toString());

            try {
                luceneIndexWriter.close();
            } catch (IOException e) {
                logger.error("Close IndexWriter failed: " + e.toString());
            }

            return false;
        } catch (IOException ioe) {
            logger.error("Failed with I/O error: " + ioe.getMessage());

            try {
                luceneIndexWriter.close();
            } catch (IOException e) {
                logger.error("Close Index Writer failed: " + e.toString());
            }

            return false;
        }
    }

    /**
     * Returns whether lucene index build successful complete
     *
     * @param src the xml source.
     * @param counter index writer
     *
     * @return boolean: if build successful complete return true else return
     *         false.
     *
     * @throws SAXException sax exceptions
     */
    public boolean buildIndex(InputSource src, int counter)
                       throws SAXException {
        reportCounterCons = counter;

        boolean result = buildIndex(src);

        return result;
    }

    /**
     * Implementation of org.xml.sax.ContentHandler.
     *
     * @param locator document locator
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * init counter
     *
     * @throws SAXException sax exceptions
     */
    public void startDocument() throws SAXException {
        //init Counter
        docTotalCounter = 0;

        //start at root level
        currentLevel = 0;
    }

    /**
     * end of sax process
     *
     * @throws SAXException sax exceptions
     */
    public void endDocument() throws SAXException {
    }

    /**
     * start of prefix mapping
     *
     * @param prefix prefixe
     * @param uri uri
     *
     * @throws SAXException sax exceptions
     */
    public void startPrefixMapping(String prefix, String uri)
                            throws SAXException {
    }

    /**
     * end of prefix mapping
     *
     * @param prefix prefix
     *
     * @throws SAXException sax exceptions
     */
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    /**
     * start xml element: switch node level and read element to create lucene
     * document
     *
     * @param namespaceURI namespace
     * @param localName local name
     * @param qName qaulified name
     * @param atts attributes
     *
     * @throws SAXException sax exceptions
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts
                            ) throws SAXException {
        //to sub level
        currentLevel++;

        switch (currentLevel) {
            case 1: //table level
                break;

            case 2: //record level
                currentDoc = new Document();

                break;

            case 3: //field level

                try {
                    if (atts.getValue("name") != null) {
                        fieldName = new String(atts.getValue("name")).trim();

                        //default values
                        fieldValue = new StringBuffer();
                        storeTag = false;
                        indexTag = false;
                        tokenTag = false;

                        if (localName.equals("Field")) {
                            String store = atts.getValue("store");

                            if ((store != null) && store.equals("no")) {
                                storeTag = false;
                            } else {
                                //default store
                                storeTag = true;
                            }
                        } else if (localName.equals("Index")) {
                            indexTag = true;

                            String token = atts.getValue("token");

                            if ((token != null) && token.equals("no")) {
                                tokenTag = false;
                            } else {
                                tokenTag = true;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                }

                break;
        }
    }

    /**
     * end element handler: switch node level to write to lucene index
     *
     * @param namespaceURI uri
     * @param localName local name
     * @param qName qualified name
     *
     * @throws SAXException sax exceptions
     */
    public void endElement(String namespaceURI, String localName, String qName)
                    throws SAXException {
        switch (currentLevel) {
            case 1: //table level
                break;

            case 2: //record level

                //mapping field to index:
                try {
                    currentDoc = mapDoc(currentDoc);

                    //write to document
                    luceneIndexWriter.addDocument(currentDoc);

                    //counter ++
                    docTotalCounter++;
                    optiCounter--;

                    // force optimize after extend optiCounterCons
                    if (optiCounter == 0) {
                        luceneIndexWriter.optimize();

                        //reste optimize counter
                        optiCounter = optiCounterCons;
                    }

                    reportCounter--;

                    if (reportCounter == 0) {
                        //show status;
                        endTime = System.currentTimeMillis();
                        logger.info(docTotalCounter + " rows added\ttime Use:"
                                    + ((endTime - startTime) / 1000)
                                    + " second"
                                   );

                        //reset reportCounter
                        reportCounter = reportCounterCons;
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                }

                break;

            case 3: //field level

                if ((fieldName != null) && (fieldName.length() > 0)) {
                    luceneField = new Field(fieldName, fieldValue.toString(),
                                            Field.Store.YES, Field.Index.TOKENIZED,Field.TermVector.YES
                                           );
                    currentDoc.add(luceneField);
                }

                break;
        }

        //back to up level
        currentLevel--;
    }

    /**
     * append char array
     *
     * @param ch current content
     * @param start start offset
     * @param length content length
     *
     * @throws SAXException SAX parse exception
     */
    public void characters(char[] ch, int start, int length)
                    throws SAXException {
        //read field value
        if (currentLevel == 3) {
            /* NOTICE:
             * if use: fieldValue = new String(ch, start, length)
             * may cause xml data value broken during saxReader reaches buffer end
             * for example:
             *                      <SomeTag>my content</SomeTag>
             * privous sax buffer reached here---^
             * after next buffer read will invoke another characters() event
             * so the fieldValue will return broken value "ntent" only
             */
            fieldValue.append(ch, start, length);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ch DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @throws SAXException sax exceptions
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
                             throws SAXException {
    }

    /**
     * processing instruction
     *
     * @param target doc
     * @param data data
     *
     * @throws SAXException sax exceptions
     */
    public void processingInstruction(String target, String data)
                               throws SAXException {
    }

    /**
     * skip entitiy
     *
     * @param name name
     *
     * @throws SAXException sax exceptions
     */
    public void skippedEntity(String name) throws SAXException {
    }

    /**
     * Implementation of org.xml.sax.ErrorHandler.
     *
     * @param e sax parse exception
     *
     * @throws SAXException sax exceptions
     */
    public void warning(SAXParseException e) throws SAXException {
        logger.error("  EVENT: warning " + e.getMessage() + ' '
                     + e.getSystemId() + ' ' + e.getLineNumber() + ' '
                     + e.getColumnNumber()
                    );
    }

    /**
     * error log
     *
     * @param e sax parse exception
     *
     * @throws SAXException sax exceptions
     */
    public void error(SAXParseException e) throws SAXException {
        logger.error("  EVENT: error " + e.getMessage() + ' ' + e.getSystemId()
                     + ' ' + e.getLineNumber() + ' ' + e.getColumnNumber()
                    );
    }

    /**
     * fatal error log
     *
     * @param e sax exception
     *
     * @throws SAXException sax exceptions
     */
    public void fatalError(SAXParseException e) throws SAXException {
        logger.error("  EVENT: fatal error " + e.getMessage() + ' '
                     + e.getSystemId() + ' ' + e.getLineNumber() + ' '
                     + e.getColumnNumber()
                    );
    }

    /**
     * map original document to lucene index Field
     *
     * @param origDocument original lucene Document
     *
     * @return Document: parse original and make index fields
     */
    private Document mapDoc(Document origDocument) {
        //new Lucene Document
        Document newDoc = new Document();

        try {
            //Enumeration fieldEnum = origDocument.fields();
        	List fieldEnum = origDocument.getFields();
        	
            //while (fieldEnum.hasMoreElements()) {
        	while(fieldEnum!=null && fieldEnum.size()>0){
                //Lucene Document Field
                //Field fld = (Field) fieldEnum.nextElement();
        		Field fld = (Field)fieldEnum.remove(0);
        		
                //index map field with fields name list: 'field1,field2,field5....'
                if (fld.isIndexed()) {
                    String indexName = fld.name();
                    StringBuffer indexValue = new StringBuffer();

                    //split field list with ","
                    String fieldList = fld.stringValue();
                    StringTokenizer st = new StringTokenizer(fieldList, ",");

                    while (st.hasMoreTokens()) {
                        //add indexValue with mapped field value
                        String mapFieldName = new String();
                        mapFieldName = st.nextToken();

                        Field mapField = origDocument.getField(mapFieldName);
                        String mapValue = null;

                        if (mapField != null) {
                            mapValue = mapField.stringValue();
                        }

                        //add text field value to indexing field
                        if (mapValue != null) {
                            indexValue.append(mapValue);

                            //add space between fields avoid "field1field2"
                            indexValue.append(" ");
                        }
                    }

                    if (indexValue.length() > 0) {
                        Field newIndex = new Field(indexName,
                                                   indexValue.toString(),
                                                   Field.Store.NO, Field.Index.TOKENIZED,
                                                   Field.TermVector.YES
                                                  );
                        newDoc.add(newIndex);
                    }
                } else { //add a common field
                    newDoc.add(fld);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());

            return null;
        }

        return newDoc;
    }
}
