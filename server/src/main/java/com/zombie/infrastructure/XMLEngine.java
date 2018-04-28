package com.zombie.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLEngine {

	String xml;

	XMLEngine(String xml) {
		this.xml = xml;
	}

	public String parse() throws ParserConfigurationException, IOException, SAXException {

		List result                     = new ArrayList();
		ObjectMapper objectMapper       = new ObjectMapper();

		File file                       = new File(this.xml);
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder         = factory.newDocumentBuilder();
		Document xmlDoc                 = builder.parse(file);

		xmlDoc.getDocumentElement().normalize();

		NodeList recipes = xmlDoc.getElementsByTagName("recipe");

		for (int i = 0; i < recipes.getLength(); i++) {
			Map<String, Object> map     = new HashMap<>();
			List _result                = new ArrayList();
			Node node                   = recipes.item(i);

			System.out.println("\n");

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element     = (Element) node;
				NodeList components = element.getElementsByTagName("ingredient");

				map.put("name", element.getAttribute("name"));
				map.put("count", element.getAttribute("count"));
				map.put("tollArea", element.getAttribute("craft_area"));

				System.out.println("name: " + element.getAttribute("name"));
				System.out.println("count: " + element.getAttribute("count"));
				System.out.println("tollArea: " + element.getAttribute("craft_area"));
				for (int y = 0; y < components.getLength(); y++) {
					Node _node = components.item(y);
					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						Map<String, Object> _map    = new HashMap<>();
						Element _element            = (Element) _node;

						_map.put("name", _element.getAttribute("name"));
						_map.put("count", _element.getAttribute("count"));
						_result.add(_map);

						System.out.println("\t"+_element.getAttribute("name") + " -> " + _element.getAttribute("count"));
					}
				}

				map.put("ingredients",_result);
				result.add(map);
			}
		}

		String arrayToJson = objectMapper.writeValueAsString(result);
		System.out.println("JSON result...");
		System.out.println(arrayToJson);

		return arrayToJson;
	}

}
