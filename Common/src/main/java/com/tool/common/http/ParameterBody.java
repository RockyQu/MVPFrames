package com.tool.common.http;

import java.io.File;

/**
 * ParameterBody
 */
public class ParameterBody {

    // ID
    private String id;
    // Key
    private String key;
    // Value
    private String value;
    // Type
    private int type;
    // File
    private File file;

    public ParameterBody() {

    }

    public ParameterBody(String value) {
        this.value = value;
    }

    public ParameterBody(String key, File file) {
        this.key = key;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}