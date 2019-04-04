package com.tiaoxi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tiaoxi.dto.ApplyDTO;
import com.tiaoxi.dto.UserDTO;
import com.tiaoxi.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static com.tiaoxi.Utils.getCurrentTime;
import static com.tiaoxi.Utils.getFormatTime;
import static com.tiaoxi.Utils.getStatusDesc;
import static com.tiaoxi.service.MessageService.SEND_PARENT_TEMPLATE;
import static java.util.stream.Collectors.toList;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
@Controller
@RequestMapping("teacher")
public class TeacherController {

    public static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    MessageService messageService;

    /**
     * 检查老师身份
     * @param openId
     * @return
     */
    @RequestMapping(value = "check",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkTeacherRole(@RequestParam("openid")String openId){
        UserDTO userDTO = null;
        try{
            userDTO = jdbcTemplate.queryForObject("SELECT * FROM UserLogin WHERE openId=? AND type = 1 LIMIT 1",new BeanPropertyRowMapper<UserDTO>(UserDTO.class),openId);
        }catch (Exception exception){
            LOGGER.info("query UserDTO exception,openId:{}",openId,exception);
        }
        JSONObject response = new JSONObject();
        if(null != userDTO){
            response.put("result",true);
        }else{
            response.put("result",false);
        }
        return response.toJSONString();
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
        JSONObject response = new JSONObject();
        try{
            int updateCount = jdbcTemplate.update("UPDATE LeavingApply SET status = ?,updated = ? WHERE id = ?",
                    2,getCurrentTime(),applyId);
            if(updateCount == 1){
                ApplyDTO applyDTO = jdbcTemplate.queryForObject("SELECT * FROM LeavingApply WHERE id = ?",
                        new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                        applyId);
                if(!StringUtils.isEmpty(applyDTO.getTelephone())){
                    Map<String,Object> params = new HashMap<>();
                    params.put("name",applyDTO.getName());
                    messageService.sendMessage(applyDTO.getTelephone(), SEND_PARENT_TEMPLATE,params);
                }
            }
            response.put("result",true);
        }catch (Exception exception){
            response.put("result",false);
        }
        return response.toJSONString();
    }
}
