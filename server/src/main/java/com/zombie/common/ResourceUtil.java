package com.zombie.common;

import java.io.InputStream;

public class ResourceUtil {

    public static InputStream getResourceAsStream(String path) {
        return ResourceUtil.class.getResourceAsStream(path);
    }

    public static byte[] getResource(String path) {
        byte[] arr = null;
        InputStream input = ResourceUtil.class.getResourceAsStream(path);
        try {
            arr = new byte[input.available()];
            input.read(arr);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static String getResourceAsString(String path) {
        return new String(getResource(path));
    }

}
