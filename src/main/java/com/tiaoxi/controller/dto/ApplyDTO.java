package com.tiaoxi.controller.dto;

import java.io.Serializable;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
public class ApplyDTO implements Serializable{

    private static final long serialVersionUID = -6128093549409827116L;

    private int id;

    private String openId;

    private String name;

    private String telephone;

    private Integer startTime;

    private Integer endTime;

    private Integer status;

    private Integer created;

    private Integer updated;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "ApplyDTO{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
