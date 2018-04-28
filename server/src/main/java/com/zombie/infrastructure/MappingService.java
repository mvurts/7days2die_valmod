package com.zombie.infrastructure;

import com.zombie.common.ResourceUtil;
import com.zombie.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 7days2die
 * Loading all configs into database
 */
@Service
public class MappingService {

    private final ItemManager itemManager;
    private final ItemXmlParser itemXmlParser;

    @Autowired
    public MappingService(
            ItemManager itemManager,
            ItemXmlParser itemXmlParser
    ) {
        this.itemManager = itemManager;
        this.itemXmlParser = itemXmlParser;
    }


    /**
     * Loading 7days2die data into database
     */
    @Transactional
    public void initSchemaData() throws Exception {
        // cleaning all entities
        itemManager.truncate();

        // loading data
        List<Item> itemList = itemXmlParser.parse(ResourceUtil.getResourceAsStream("/items.xml"));
        itemManager.saveAll(itemList);
    }

}
