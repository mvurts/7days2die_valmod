package com.zombie.common;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class DocumentUtil {

	public Document getXmlDoc(InputStream is) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder         = factory.newDocumentBuilder();
		Document xmlDoc                 = builder.parse(is);

		xmlDoc.getDocumentElement().normalize();

		return xmlDoc;
	}
}
