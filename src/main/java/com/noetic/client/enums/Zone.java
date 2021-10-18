package com.noetic.client.enums;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum Zone {
    Outdoor(1, "trisfal", "/maps/trisfal_glades.tmx");

    private int id;
    private String name;
    private String path;

    Zone(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        try {
            return new File(getClass().getResource(path).toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger("client").log(Level.SEVERE, "Unable to read zone file: {0}", ex.getMessage());
        }
        return null;
    }
}
