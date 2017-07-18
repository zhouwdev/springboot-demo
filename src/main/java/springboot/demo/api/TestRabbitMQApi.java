package springboot.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.demo.common.ApiException;
import springboot.demo.common.JsonUtils;
import springboot.demo.service.RabbitMQService;

/**
 *
 * Created by AA on 2017/7/17.
 */
@RestController
@RequestMapping("api/test")
@Api(value = "testApi", description = "testApi")
public class TestRabbitMQApi {

    @Autowired
    private RabbitMQService rabbitMQService;

    @PostMapping("/msgSendTest")
    @ApiOperation(value = "post menthod msgSendTest", notes = "post msgSendTest")
    public Object testPost(@RequestBody Object object) throws Exception {

       rabbitMQService.sendMsg(JsonUtils.toJson(object));
       return "send success";
    }
}
