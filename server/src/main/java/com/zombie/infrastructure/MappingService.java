package com.zombie.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 7days2die data mapper
 */
@Service
public class MappingService {

    private final ItemManager itemManager;

    @Autowired
    public MappingService(ItemManager itemManager) {
        this.itemManager = itemManager;
    }


    /**
     * Loading 7days2die data into database
     */
    @Transactional
    public void initSchemaData() {

    }

}
