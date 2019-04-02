package com.tiaoxi;

import com.tiaoxi.controller.dto.ApplyDTO;
import com.tiaoxi.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import static com.tiaoxi.Utils.getCurrentTime;
import static com.tiaoxi.service.MessageService.TEACHER_TEMPLATE;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/2
 */
@Service("fixedSchdulerJob")
public class FixedSchdulerJob{

    private static final Logger LOGGER = LoggerFactory.getLogger(FixedSchdulerJob.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void checkApplyList(){
        int nowTime = getCurrentTime();
        LOGGER.info("checkApplyList started");
        List<ApplyDTO> list = new ArrayList<>();
        try {
            list = jdbcTemplate.query(
                    "SELECT * FROM LeavingApply WHERE endTime >= ?  AND status = ?",
                    new BeanPropertyRowMapper<ApplyDTO>(ApplyDTO.class),
                    nowTime,1);
        }catch (Exception e){
            LOGGER.error("getApplyList {},exception",nowTime,e);
        }
        if(!CollectionUtils.isEmpty(list)){
            messageService.sendMessage("13989828440",TEACHER_TEMPLATE);
        }
    }
}
