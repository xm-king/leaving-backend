package com.tiaoxi.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.tiaoxi.controller.dto.ApplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
@Controller
@RequestMapping("user")
public class ApplyController {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping(value = "apply",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String apply(ApplyDTO applyDTO){
        jdbcTemplate.update("INSERT INTO `LeavingApply` (`number`, `name`, `startTime`, `endTime`, `applyer`, `telphone`, `createTime`)VALUES(?,?, ?, ?, ?, ?, ?)",
                new Object[]{applyDTO.getNumber(),applyDTO.getName(),applyDTO.getStartTime(),applyDTO.getEndTime(),applyDTO.getApplyer(),applyDTO.getTelphone(),(int)(System.currentTimeMillis()/1000)});
        return JSON.toJSONString(applyDTO);
    }


}
