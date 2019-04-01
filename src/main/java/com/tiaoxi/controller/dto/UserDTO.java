package com.tiaoxi.controller.dto;

import java.io.Serializable;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/1
 */
public class UserDTO implements Serializable{
    private static final long serialVersionUID = 4378319373494060063L;
    private int id;
    private String openId;
    private String nick;
    private int created;
    private int updated;

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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", nick='" + nick + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
