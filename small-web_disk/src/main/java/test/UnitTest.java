package test;

import com.alibaba.fastjson.JSON;
import com.small.web.disk.Application;
import com.small.web.disk.evt.*;
import com.small.web.disk.service.AttachService;
import com.small.web.disk.service.IndexService;
import com.small.web.disk.service.UserService;
import com.small.web.disk.model.ServiceRespModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {Application.class})
public class UnitTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;
    @Autowired
    IndexService indexService;
    @Autowired
    AttachService attachService;


    @org.junit.Test
    public void t_login() {
        UserLoginEvt evt = new UserLoginEvt();
        evt.setUserAccount("1234567aA");
        evt.setUserPassword("123456");
        ServiceRespModel serviceRespModel = userService.login(evt);
        logger.info(JSON.toJSONString(serviceRespModel));

    }

    @org.junit.Test
    public void t_register() {
        UserRegisterEvt evt = new UserRegisterEvt();
        evt.setUserName("Test03");
        evt.setUserAccount("123224");
        evt.setUserPassword("0987654321");
        ServiceRespModel serviceRespModel = userService.register(evt);
        logger.info(JSON.toJSONString(serviceRespModel));
    }

    @Test
    public void t_updateIndex() {
        UpdateIndexNameEvt evt = new UpdateIndexNameEvt();
        evt.setIndexNo("8c5180b644c5478dab74e459ed7220b8");
        evt.setIndexName("修改目录名称");
        ServiceRespModel serviceRespModel = indexService.updateIndex(evt);
        logger.info(JSON.toJSONString(serviceRespModel));
    }

    @Test
    public void t_deleteIndex() {
        DeleteIndexEvt evt = new DeleteIndexEvt();
        evt.setIndexNo("8c5180b644c5478dab74e459ed7220b8");
        ServiceRespModel serviceRespModel = indexService.deleteIndex(evt);
        logger.info(JSON.toJSONString(serviceRespModel));
    }

    @Test
    public void t_queryIndexAttachList() {
        QueryIndexAttachEvt evt = new QueryIndexAttachEvt();
        evt.setIndexNo("045af01801724837ae6c53e645d12079");
        ServiceRespModel serviceRespModel = attachService.indexAttachList(evt);
        logger.info(JSON.toJSONString(serviceRespModel));
    }

    @Test
    public void t_updateAttach() {
        UpdateAttachNameEvt evt = new UpdateAttachNameEvt();
        evt.setAttachNo("a6ca2a2bee98444c9fef5900e195b27b");
        evt.setAttachName("修改附件名称");
        ServiceRespModel serviceRespModel = attachService.updateAttach(evt);
        logger.info(JSON.toJSONString(serviceRespModel));
    }
}
