package springboot.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.demo.common.AnnotationDemo;
import springboot.demo.model.ApiReqModel;

import javax.servlet.http.HttpSession;


/**
 * Created by zhouwei on 2017/5/18.
 */
@RestController
@RequestMapping(value = "api/acct")
@Api(value = "acctApi", description = "acctApi")
public class AccountApi {

    @PreAuthorize("hasRole('op:add')")
    @GetMapping("/testGet")
    @ApiOperation(value = "get menthod test", notes = "get menthod test")
    @AnnotationDemo(name = "test", key = "#id", like = "#id")
    public Object testGet(@ApiParam(name = "id", defaultValue = "1", required = true) @RequestParam Integer id) {
        return "hello word for get menthod";
    }

    @PreAuthorize("hasRole('op:add4')")
    @PostMapping(value = "/testPost")
    @ApiOperation(value = "post menthod test", notes = "post menthod test")
    public Object testPost(@RequestBody @Validated ApiReqModel apiReqModel) {
        return "hello word for get menthod";
    }

    @PreAuthorize("hasRole('op:add2')")
    @PostMapping("/testPost1")
    @ApiOperation(value = "get menthod post", notes = "get menthod post")
    @AnnotationDemo(name = "test", key = "#apiReqM.amt", like = "#id")
    public Object testPost1(@RequestBody @Validated ApiReqModel apiReqM,
                            @ApiParam(name = "id", defaultValue = "1", required = true) @RequestParam Integer id) {
        System.out.println("-----------------------------------");
        return apiReqM;
    }

    @GetMapping("/userInfo")
    @ApiOperation(value = "get menthod post", notes = "get menthod post")
    public Object userInfo(HttpSession session) {
        System.out.println("-----------------------------------");
        return session.getId();
    }
}
