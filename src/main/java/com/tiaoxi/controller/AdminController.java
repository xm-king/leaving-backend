package com.tiaoxi.controller;

import com.alibaba.fastjson.JSON;
import com.tiaoxi.controller.dto.ApplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "list",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getApplyList(){
        int nowTime = (int)(System.currentTimeMillis()/1000);
        List<ApplyDTO> list = jdbcTemplate.query(
                "SELECT * FROM LeavingApply WHERE startTime <= ? AND endTime >= ?",
                new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                nowTime,nowTime);
        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "lastMonthlist",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getLastMonthList(){
        int nowTime = (int)(System.currentTimeMillis()/1000) - 24 * 60 * 60;
        List<ApplyDTO> list = jdbcTemplate.query(
                "SELECT * FROM LeavingApply WHERE startTime <= ? AND endTime >= ?",
                new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                nowTime,nowTime);
        return JSON.toJSONString(list);
    }
}
