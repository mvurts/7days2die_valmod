package com.zombie.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zombie.common.DocumentUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesXmlParser {

	private InputStream xmlBody;
	private DocumentUtil documentUtil = new DocumentUtil();

	public RecipesXmlParser(InputStream xmlBody) {
		this.xmlBody = xmlBody;
	}

	public String parse() throws ParserConfigurationException, IOException, SAXException {

		List<Map> result                = new ArrayList();
		ObjectMapper objectMapper       = new ObjectMapper();
		Document xmlDoc                 = documentUtil.getXmlDoc(this.xmlBody);

		NodeList recipes = xmlDoc.getElementsByTagName("recipe");

		for (int i = 0; i < recipes.getLength(); i++) {
			Map<String, Object> map     = new HashMap<>();
			List<Map> _result           = new ArrayList();
			Node node                   = recipes.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element     = (Element) node;
				NodeList components = element.getElementsByTagName("ingredient");

				map.put("name", element.getAttribute("name"));
				map.put("count", element.getAttribute("count"));
				map.put("tollArea", element.getAttribute("craft_area"));

				for (int y = 0; y < components.getLength(); y++) {
					Node _node = components.item(y);
					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						Map<String, Object> _map    = new HashMap<>();
						Element _element            = (Element) _node;

						_map.put("name", _element.getAttribute("name"));
						_map.put("count", _element.getAttribute("count"));
						_result.add(_map);
					}
				}

				map.put("ingredients",_result);
				result.add(map);
			}
		}

		return objectMapper.writeValueAsString(result);
	}

}
