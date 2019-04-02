package com.tiaoxi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tiaoxi.controller.dto.ApplyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tiaoxi.Utils.*;
import static java.util.stream.Collectors.toList;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/3/30
 */
@Controller
@RequestMapping("student")
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 微信和宝贝绑定
     * @param openId
     * @param name
     * @return
     */
    @RequestMapping("bind")
    @ResponseBody
    public String bind(@RequestParam("openid") String openId,
                       @RequestParam("name") String name){
        JSONObject response = new JSONObject();
        try{
            jdbcTemplate.update("INSERT INTO `RelatedBaby` ( `openId`, `name`, `created`)VALUES(?,?,?)",
                    openId,name,getCurrentTime());
            response.put("result",true);
        }catch (Exception exception){
            LOGGER.error("bind openId:{},name:{} exception",openId,name,exception);
            response.put("result",false);
        }
        return response.toJSONString();
    }

    /**
     * 微信和宝贝绑定
     * @param openId
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public String update(@RequestParam("openid") String openId,
                       @RequestParam("nick") String nick){
        JSONObject response = new JSONObject();
        try{
            jdbcTemplate.update("UPDATE UserLogin SET nick =? ,updated = ? WHERE openId = ? LIMIT 1",nick,getCurrentTime(),openId);
            response.put("result",true);
        }catch (Exception ex){
            response.put("result",false);
            LOGGER.error("update error,openId:{},nick:{}",openId,nick);
        }
        return response.toJSONString();
    }

    /**
     * 请假申请
     * @return
     */
    @RequestMapping(value = "apply",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String apply(@RequestParam("openid") String openId,
                        @RequestParam("name") String name,
                        @RequestParam(name = "telephone",defaultValue = "",required = false)String telephone,
                        @RequestParam("startTime") String startTime,
                        @RequestParam("endTime") String endTime){
        JSONObject response = new JSONObject();
        try{
            jdbcTemplate.update("INSERT INTO `LeavingApply` (`openid`, `name`, `telephone`, `startTime`, `endTime`, `created`, `updated`)" +
                            "VALUES(?,?,?,?,?,?,?)",
                    new Object[]{openId,name,telephone,getTimestamp(startTime),getTimestamp(endTime),getCurrentTime(),getCurrentTime()});
            response.put("result",true);
        }catch (Exception exception){
            response.put("result",false);
            LOGGER.error("apply exception,openId:{},name:{},startTime:{},endTime:{}",openId,name,startTime,endTime,exception);
        }
        return JSON.toJSONString(response);
    }

    /**
     * 我的请假列表
     * @return
     */
    @RequestMapping(value = "list",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String queryList(@RequestParam("name")String name){
        List<ApplyDTO> list = new ArrayList<ApplyDTO>();
        try{
            list = jdbcTemplate.query("SELECT * FROM LeavingApply WHERE name = ? ORDER BY id DESC LIMIT 10",
                    new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                    name);
        }catch (Exception exception){
            LOGGER.error("queryList exception:{}",name,exception);
        }
        JSONObject response = new JSONObject();
        response.put("data",list.stream().parallel().map(applyDTO -> {
            JSONObject applyObject = new JSONObject();
            applyObject.put("id",applyDTO.getId());
            applyObject.put("name",applyDTO.getName());
            applyObject.put("startTime",getFormatTime(applyDTO.getStartTime()));
            applyObject.put("endTime",getFormatTime(applyDTO.getEndTime()));
            applyObject.put("statusDesc",getStatusDesc(applyDTO.getStatus()));
            return applyObject;
        }).collect(toList()));
        response.put("result",true);
        return JSON.toJSONString(response);
    }

}
