package com.zombie.infrastructure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLEngine {

	String xml;

	XMLEngine(String xml) {
		this.xml = xml;
	}

	public void parse() throws ParserConfigurationException, IOException, SAXException {

		File file                       = new File(this.xml);
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder         = factory.newDocumentBuilder();
		Document xmlDoc                 = builder.parse(file);

		xmlDoc.getDocumentElement().normalize();

		NodeList recipes = xmlDoc.getElementsByTagName("recipe");

		for (int i = 0; i < recipes.getLength(); i++) {
			Node node = recipes.item(i);

			System.out.println("\n");

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element     = (Element) node;
				NodeList components = element.getElementsByTagName("ingredient");

				System.out.println("name: " + element.getAttribute("name"));
				System.out.println("count: " + element.getAttribute("count"));
				System.out.println("tollArea: " + element.getAttribute("craft_area"));
				for (int y = 0; y < components.getLength(); y++) {
					Node _node = components.item(y);
					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						Element _element     = (Element) _node;
						System.out.println("\t"+_element.getAttribute("name") + " -> " + _element.getAttribute("count"));
					}
				}
			}
		}
	}

}
