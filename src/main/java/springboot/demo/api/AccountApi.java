package springboot.demo.api;

import ch.qos.logback.core.net.SyslogOutputStream;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.demo.common.AnnotationDemo;
import springboot.demo.model.ApiReqModel;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.security.Principal;


/**
 * Created by zhouwei on 2017/5/18.
 */
@RestController
@RequestMapping("api/acct")
@Api(value = "acctApi", description = "acctApi")
public class AccountApi {

    @PreAuthorize("hasRole('op:add')")
    @GetMapping("/testGet")
    @ApiOperation(value = "get menthod test", notes = "get menthod test")
    @AnnotationDemo(name = "test", key = "#id")
    public Object testGet(@ApiParam(name = "id", defaultValue = "1", required = true) @RequestParam Integer id) {
        return "hello word for get menthod";
    }

    @PreAuthorize("hasRole('op:add2')")
    @PostMapping(value = "/testPost",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "post menthod test", notes = "post menthod test")
    public Object testPost(@RequestBody @Validated ApiReqModel apiReqModel) {
        return "hello word for post menthod";
    }

    @PreAuthorize("hasRole('op:add2')")
    @PostMapping("/testPost1")
    @ApiOperation(value = "get menthod post", notes = "get menthod post")
    @AnnotationDemo(name = "test", key = "#principal.name", like = "#id")
    public Object testPost1(Principal principal, @RequestParam Integer id) {
        System.out.println(principal.getName());
        return "hello word for get menthod";
    }
}
