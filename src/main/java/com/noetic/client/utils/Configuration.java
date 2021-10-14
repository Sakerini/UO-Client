package com.noetic.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class Configuration {
    private static final String FILE_RESOURCE = "config.cfg";

    public static String appTitle;
    public static String appVersion;
    public static int windowWidth;
    public static int windowHeight;

    public static void load() {
        Properties properties = new Properties();
        InputStream istream = null;

        try {
            istream = new FileInputStream(getResourceAsFile());

            properties.load(istream);

            appTitle = String.valueOf(properties.getProperty("app_title"));
            appVersion = String.valueOf(properties.getProperty("app_version"));
            windowWidth = Integer.parseInt(String.valueOf(properties.getProperty("window_width")));
            windowHeight = Integer.parseInt(String.valueOf(properties.getProperty("window_height")));

            istream.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private static File getResourceAsFile() {
        File file = null;
        try {
            file = loadFile(Configuration.FILE_RESOURCE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return file;
    }

    private static File loadFile(String filePath) throws URISyntaxException {
        ClassLoader classLoader = Configuration.class.getClassLoader();
        URL resource = classLoader.getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found" + filePath);
        } else {
            return new File(resource.toURI());
        }
    }
}
