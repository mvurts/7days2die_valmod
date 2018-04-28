package com.zombie.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zombie.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ItemXmlParser {

    // names of properties
    private final static String PROP_ID = "id";
    private final static String PROP_NAME = "name";
    private final static String PROP_MATERIAL = "Material";
    private final static String PROP_WEIGHT = "Weight";
    private final static String PROP_MESHFILE = "Meshfile";
    private final static String PROP_DROP_MESHFILE = "DropMeshfile";
    private final static String PROP_EXTENDS = "Extends";

    private final ObjectMapper mapper;

    @Autowired
    public ItemXmlParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Parsing Item objects from items.xml
     *
     * @param in input stream with xml body
     * @return list of mapped Item objects
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public List<Item> parse(InputStream in) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDoc = builder.parse(in);
        xmlDoc.getDocumentElement().normalize();

        // map structure: <name, <itemPropName, itemPropValue>
        Map<String, Map<String, String>> rawItems = new HashMap<>();
        Map<String, Item> itemMap = new HashMap<>();

        Set<String> propNamesSet = Arrays.stream(new String[] {PROP_MATERIAL, PROP_WEIGHT, PROP_MESHFILE, PROP_DROP_MESHFILE, PROP_EXTENDS}).collect(Collectors.toSet());

        // extracting items raw map from XML
        NodeList items = xmlDoc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Node node = items.item(i);
            Map<String, String> propMap = new HashMap<>();
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element element = (Element) node;
            String id = element.getAttribute(PROP_ID);
            String name = element.getAttribute(PROP_NAME);
            propMap.put(PROP_ID, id);
            propMap.put(PROP_NAME, name);

            // reading property tags
            NodeList propNodeList = element.getChildNodes();
            for (int j = 0; j < propNodeList.getLength(); j++) {
                Node propNode = propNodeList.item(j);
                if (!"property".equals(propNode.getNodeName())) continue;
                if (propNode.getNodeType() != Node.ELEMENT_NODE) continue;
                Element propElement = (Element) propNode;
                if (!propElement.hasAttribute(PROP_NAME) || !propElement.hasAttribute("value")) {
                    continue;
                }
                String propName = propElement.getAttribute(PROP_NAME);
                String propValue = propElement.getAttribute("value");
                if (propNamesSet.contains(propName)) {
                    propMap.put(propName, propValue);
                }
            }
            rawItems.put(name, propMap);
        }

        // extracting Item objects without Extends
        for (Iterator<Map.Entry<String, Map<String, String>>> it = rawItems.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Map<String, String>> entry = it.next();
            Map<String, String> itemProps = entry.getValue();
            if (itemProps.get(PROP_EXTENDS) != null) continue;

            Item item = new Item();
            item.setId(Long.parseLong(itemProps.get(PROP_ID)));
            item.setName(itemProps.get(PROP_NAME));
            item.setMaterial(itemProps.get(PROP_MATERIAL));
            item.setWeight(parseDouble(itemProps.get(PROP_WEIGHT)));
            item.setMeshFile(itemProps.get(PROP_MESHFILE));
            item.setMeshFileDrop(itemProps.get(PROP_DROP_MESHFILE));
            itemMap.put(item.getName(), item);

            it.remove();
        }

        // Extracting Items with inherited properties
        int iterations = 0;
        while (!rawItems.keySet().isEmpty()) {
            iterations++;
            if (iterations > 1000) {
                log.error("Cannot complete Item mapping: too many loop iterations for hierarchy processing");
                break;
            }
            for (Iterator<Map.Entry<String, Map<String, String>>> it = rawItems.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Map<String, String>> entry = it.next();
                Map<String, String> itemProps = entry.getValue();
                String superClass = itemProps.get(PROP_EXTENDS);
                if (rawItems.get(superClass) != null) {
                    continue;
                }

                Item superItem = itemMap.get(superClass);
                if (superItem == null) {
                    log.error("Cannot map Item[" + itemProps.get(PROP_ID) + "]: superclass Item[" + superClass + "] not found");
                    continue;
                }
                Item item = superItem.clone();
                item.setId(Long.parseLong(itemProps.get(PROP_ID)));
                item.setName(itemProps.get(PROP_NAME));
                if (itemProps.get(PROP_WEIGHT) != null) {
                    item.setWeight(parseDouble(itemProps.get(PROP_WEIGHT)));
                }
                if (itemProps.get(PROP_MATERIAL) != null) {
                    item.setMaterial(itemProps.get(PROP_MATERIAL));
                }
                if (itemProps.get(PROP_MESHFILE) != null) {
                    item.setMeshFile(itemProps.get(PROP_MESHFILE));
                }
                if (itemProps.get(PROP_DROP_MESHFILE) != null) {
                    item.setMeshFileDrop(itemProps.get(PROP_DROP_MESHFILE));
                }

                itemMap.put(item.getName(), item);

                it.remove();
            }
        }

        return new ArrayList<>(itemMap.values());
    }

    private Double parseDouble(String data) {
        return data == null ? null : Double.parseDouble(data);
    }

}
