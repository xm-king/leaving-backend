package com.tiaoxi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tiaoxi.controller.dto.ApplyDTO;
import com.tiaoxi.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.tiaoxi.Utils.getCurrentTime;
import static com.tiaoxi.Utils.getFormatTime;
import static com.tiaoxi.Utils.getStatusDesc;
import static com.tiaoxi.service.MessageService.PARENT_TEMPLATE;
import static java.util.stream.Collectors.toList;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
@Controller
@RequestMapping("teacher")
public class TeacherController {

    public static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    public static final List<String> OPENIDS = Arrays.asList("o3Kjy5IKTEnSTNWWOrJTL20Q34wo");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    MessageService messageService;

    @RequestMapping(value = "check",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkTeacherRole(@RequestParam("openid")String openId){
        if(OPENIDS.contains(openId)){
            return "YES";
        }else{
            return "NO";
        }
    }

    /**
     * 查询未审批的请假列表
     * @return
     */
    @RequestMapping(value = "list",produces = "application/json;charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public String getApplyList(){
        int nowTime = getCurrentTime();

        //截止到当前未审批的请假单
        List<ApplyDTO> list = new ArrayList<>();
        try {
           list = jdbcTemplate.query(
                    "SELECT * FROM LeavingApply WHERE endTime >= ?  AND status = ?",
                    new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                    nowTime,1);
        }catch (Exception e){
            LOGGER.error("getApplyList {},exception",nowTime,e);
        }
        return JSON.toJSONString(list.stream().map(applyDTO -> {
            JSONObject applyOjbect = new JSONObject();
            applyOjbect.put("applyId",applyDTO.getId());
            applyOjbect.put("name",applyDTO.getName());
            applyOjbect.put("startTime",getFormatTime(applyDTO.getStartTime()));
            applyOjbect.put("endTime",getFormatTime(applyDTO.getEndTime()));
            return applyOjbect;
        }).collect(toList()));
    }

    /**
     * 查询当日请假
     * @return
     */
    @RequestMapping(value = "lastList",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getLastList(){
        int nowTime = getCurrentTime();
        List<ApplyDTO> list = new ArrayList<>();
        try {
            list = jdbcTemplate.query(
                    "SELECT * FROM LeavingApply WHERE startTime <= ? AND endTime >= ?",
                    new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                    nowTime,nowTime);
        }catch (Exception e){
            LOGGER.error("getLastList {},exception",nowTime,e);
        }
        return JSON.toJSONString(list.stream().map(applyDTO -> {
            JSONObject applyOjbect = new JSONObject();
            applyOjbect.put("applyId",applyDTO.getId());
            applyOjbect.put("name",applyDTO.getName());
            applyOjbect.put("startTime",getFormatTime(applyDTO.getStartTime()));
            applyOjbect.put("endTime",getFormatTime(applyDTO.getEndTime()));
            applyOjbect.put("statusDesc",getStatusDesc(applyDTO.getStatus()));
            return applyOjbect;
        }).collect(toList()));
    }

    /**
     * 审批请假
     * @param applyId
     * @return
     */
    @RequestMapping(value = "audit",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String auditApply(@RequestParam("applyId") Integer applyId){
        try{
            int updateCount = jdbcTemplate.update("UPDATE LeavingApply SET status = ?,updated = ? WHERE id = ?",
                    2,getCurrentTime(),applyId);
            if(updateCount == 1){
                ApplyDTO applyDTO = jdbcTemplate.queryForObject("SELECT * FROM LeavingApply WHERE id = ?",
                        new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                        applyId);
                messageService.sendMessage(applyDTO.getTelephone(),PARENT_TEMPLATE);
            }
            return "SUCCESS";
        }catch (Exception exception){
            return "FAIL";
        }
    }
}
