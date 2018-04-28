package com.zombie;

import com.zombie.common.ResourceUtil;
import com.zombie.infrastructure.RecipesXmlParser;

/**
 * Created by mvurts on 28.04.2018.
 */
public class DebugApp {

    public static void main(String[] args) throws Exception {
        RecipesXmlParser engine = new RecipesXmlParser(ResourceUtil.getResourceAsStream("/recipes.xml"));
        System.out.println(engine.parse());
    }

}
