package springboot.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.demo.common.AnnotationDemo;
import springboot.demo.common.ExcelException;
import springboot.demo.common.ExcelUtils;
import springboot.demo.model.ApiReqModel;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


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

    //@PreAuthorize("hasRole('op:add2')")
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

    @GetMapping("/exportExc")
    @ApiOperation(value = "get menthod post", notes = "get menthod post")
    public void exportExc(HttpServletResponse response) {

        {
            OutputStream fos = null;
            try {

                List datas = new ArrayList();
                ApiReqModel d = new ApiReqModel();
                d.age = 20;
                d.amt = new BigDecimal("1.5");
                d.id = 2;
                datas.add(d);

                LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
                fieldMap.put("id", "序号");
                fieldMap.put("age", "age");
                fieldMap.put("amt", "amt");

                //设置监听

                ExcelUtils.listToExcel(datas, fieldMap, "a",  response);
            }  catch (Exception ex) {

            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException ex) {

                }
            }
        }

    }
}
