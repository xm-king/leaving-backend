package com.tiaoxi.controller.dto;

import java.io.Serializable;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/1
 */
public class RelatedDTO implements Serializable{

    private static final long serialVersionUID = -1516207690153816000L;

    private int id;
    private String openId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RelatedDTO{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
