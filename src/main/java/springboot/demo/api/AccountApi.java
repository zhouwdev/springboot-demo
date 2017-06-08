package springboot.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhouwei on 2017/5/18.
 */
@RestController
@RequestMapping("api/acct")
@Api(value = "acctApi", description = "acctApi")
public class AccountApi {

    @GetMapping("/testGet")
    @ApiOperation(value = "get menthod test", notes = "get menthod test")
    public Object testGet(@ApiParam(name = "id", defaultValue = "1", required = true) @RequestParam Integer id) {
        return "hello word for get menthod";
    }

    @PostMapping("/testPost")
    @ApiOperation(value = "post menthod test", notes = "post menthod test")
    public Object testPost() {
        return "hello word for post menthod";
    }
}
