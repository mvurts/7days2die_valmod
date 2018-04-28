package com.zombie;

import com.zombie.common.ResourceUtil;
import com.zombie.infrastructure.XMLEngine;

/**
 * Created by mvurts on 28.04.2018.
 */
public class DebugApp {

    public static void main(String[] args) throws Exception {
        XMLEngine engine = new XMLEngine(ResourceUtil.getResourceAsString("/recipes.xml"));
        engine.parse();
    }

}
