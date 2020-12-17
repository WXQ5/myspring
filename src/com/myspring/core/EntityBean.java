package com.myspring.core;

import java.util.HashMap;
import java.util.Map;

public class EntityBean {
    private String id;
    private String className;
    private Map<String,String> props;

    public EntityBean() {
        props = new HashMap<String,String >();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    @Override
    public String toString() {
        return "EntityBean{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", props=" + props +
                '}';
    }
}
