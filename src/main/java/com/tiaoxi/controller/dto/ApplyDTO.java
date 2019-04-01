package com.tiaoxi.controller.dto;

import java.io.Serializable;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
public class ApplyDTO implements Serializable{

    private static final long serialVersionUID = -6128093549409827116L;

    private Integer number;

    private String name;

    private int startTime;

    private int endTime;

    private String applyer;

    private String telphone;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}
