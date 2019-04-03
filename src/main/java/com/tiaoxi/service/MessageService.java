package com.tiaoxi.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/2
 */
@Service
public class MessageService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public static final String TEACHER_TEMPLATE = "SMS_162524078";

    public static final String PARENT_TEMPLATE = "SMS_162630625";

    public static final String REGION_ID = "default";

    private static final String ACCESS_KEY = "LTAI2LparMIUe1Hd";

    private static final String SECRET = "UckPg6kXo3nZZbL8cxeicXYsXQy0AC";

    public void sendMessage(String telphones,String templateCode,Map<String,Object> params){
        try{
            DefaultProfile profile = DefaultProfile.getProfile(REGION_ID,
                    ACCESS_KEY,
                    SECRET);
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            //request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("PhoneNumbers", telphones);
            request.putQueryParameter("SignName", "苕溪幼儿园小十班");
            request.putQueryParameter("TemplateCode", templateCode);
            request.putBodyParameter("TemplateParam", JSON.toJSON(params));

            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        }catch (Exception exception){
            LOGGER.error("sendMessage exception,templateCode:{}",templateCode,exception);
        }
    }

    public static void main(String[] args) {
        MessageService messageService = new MessageService();
        messageService.sendMessage("13989828440",PARENT_TEMPLATE,new HashMap<String,Object>(){{
            put("name","相家喻");
        }});
    }
}
