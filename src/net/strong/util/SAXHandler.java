package net.strong.util;

import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>Title: </p>
 * <p>Description:XML解析器，在InitDatabase中将用到这个类 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author jony
 * @version 1.0
 */

public class SAXHandler extends DefaultHandler {
  private Hashtable table = new Hashtable();
  private String currentElement = null;
  private String currentValue = null;
  public void setTable(Hashtable table)
  {
    this.table = table;
  }
  public Hashtable getTable()
  {
    return table;
  }
  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes)
                  throws SAXException
  {
    currentElement = qName;
  }
  public void characters(char[] ch,
                       int start,
                       int length)
                throws SAXException
  {
    currentValue = new String(ch,start,length);
  }
  public void endElement(java.lang.String uri,
                       java.lang.String localName,
                       java.lang.String qName)
                throws SAXException
  {
    if(currentElement.equals(qName))
      table.put(currentElement,currentValue);

  }
}
