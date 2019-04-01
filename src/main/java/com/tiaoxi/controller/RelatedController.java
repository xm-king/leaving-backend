package com.tiaoxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/1
 */
@Controller
@RequestMapping("related")
public class RelatedController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("bind")
    @ResponseBody
    public String bind(@RequestParam("openid") String openId,
                       @RequestParam("name") String name){
        jdbcTemplate.update("INSERT INTO `RelatedBaby` ( `openId`, `name`, `created`)VALUES(?,?,?)",
                openId,name,(int)(System.currentTimeMillis()/1000));
        return "success";
    }
}
