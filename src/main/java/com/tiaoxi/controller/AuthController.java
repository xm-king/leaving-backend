package com.tiaoxi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tiaoxi.controller.dto.RelatedDTO;
import com.tiaoxi.controller.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiaoxi.Utils.getCurrentTime;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/1
 */
@Controller
@RequestMapping("auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "login",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String authLogin(@RequestParam("code") String code){
        JSONObject response = new JSONObject();
        try{
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&grant_type={grant_type}&js_code={js_code}";
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("appid","wxfd930684c175ad0e");
            params.put("secret","0152eb9ec10cb8f9d7f9f77db86e3842");
            params.put("grant_type","authorization_code");
            params.put("js_code",code);
            String result = restTemplate.getForObject(url,String.class,params);
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("get api.weixin.qq.com/sns:{}",result);
            }
            JSONObject data = JSON.parseObject(result);
            String openId = data.getString("openid");

            doLogin(openId);
            RelatedDTO relatedDTO = getRelatedDTO(openId);
            response.put("openid",openId);
            if(null != relatedDTO){
                response.put("name",relatedDTO.getName());
            }
        }catch (Exception exception){
            LOGGER.error("authLogin exception",exception);
        }
        return response.toJSONString();
    }

    /**
     * 登陆
     * @param openId
     * @return
     */
    private boolean doLogin(String openId){
        UserDTO userDTO = null;
        try{
            userDTO = jdbcTemplate.queryForObject("SELECT * FROM UserLogin WHERE openId=? LIMIT 1",new BeanPropertyRowMapper<UserDTO>(UserDTO.class),openId);
        }catch (Exception exception){
            LOGGER.info("query UserDTO exception,openId:{}",openId,exception);
        }
        if(null == userDTO){
            //新增授权用户
            userDTO = new UserDTO();
            userDTO.setOpenId(openId);
            userDTO.setNick("");
            userDTO.setCreated(getCurrentTime());
            userDTO.setUpdated(getCurrentTime());
            jdbcTemplate.update("INSERT INTO `UserLogin` (`openId`, `nick`, `created`, `updated`)VALUES(?,?,?,?)",
                    userDTO.getOpenId(),userDTO.getNick(),userDTO.getCreated(),userDTO.getUpdated());
        }else{
            //更新最后一次登录时间
            jdbcTemplate.update("UPDATE UserLogin SET updated = ? WHERE openId = ? LIMIT 1",
                    getCurrentTime(),openId);
        }
        return true;
    }

    /**
     * 获取关联用户
     * @param openId
     * @return
     */
    private RelatedDTO getRelatedDTO(String openId){
        RelatedDTO relatedDTO = null;
        try{
            relatedDTO = jdbcTemplate.queryForObject("SELECT * FROM RelatedBaby WHERE openId = ? LIMIT 1",
                    new BeanPropertyRowMapper<RelatedDTO>(RelatedDTO.class),
                    openId);
        }catch (Exception exception){
            LOGGER.info("query RelatedDTO exception,openId:{}",openId,exception);
        }
        return relatedDTO;
    }

}
