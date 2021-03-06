package net.strong.ioc.aop.config.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import net.strong.lang.Files;
import net.strong.lang.Lang;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * 通过Xml配置文件判断需要拦截哪些方法
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class XmlAopConfigration extends AbstractAopConfigration {

	public XmlAopConfigration(String... fileNames) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilder builder = Lang.xmls();
		Document document;
		List<AopConfigrationItem> aopList = new ArrayList<AopConfigrationItem>();
		for (String fileName : fileNames) {
			document = builder.parse(Files.findFile(fileName));
			document.normalizeDocument();
			NodeList nodeListZ = ((Element) document.getDocumentElement()).getElementsByTagName("class");
			for (int i = 0; i < nodeListZ.getLength(); i++)
				aopList.add(parse((Element) nodeListZ.item(i)));
		}
		setAopItemList(aopList);
	}

	private AopConfigrationItem parse(Element item) {
		AopConfigrationItem aopItem = new AopConfigrationItem();
		aopItem.setClassName(item.getAttribute("name"));
		aopItem.setMethodName(item.getAttribute("method"));
		aopItem.setInterceptor(item.getAttribute("interceptor"));
		if (item.hasAttribute("singleton"))
			aopItem.setSingleton(Boolean.parseBoolean(item.getAttribute("singleton")));
		return aopItem;
	}

}
