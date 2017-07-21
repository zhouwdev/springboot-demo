package springboot.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springboot.demo.validator.AmtValidate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * Created by AA on 2017/7/18.
 */

@ApiModel("测试请求model")
public class ApiReqModel {
    @ApiModelProperty(value = "id", example = "1", required = true)
    @Max(value = 3, message = "最大值3")
    public int id;

    @ApiModelProperty(value = "age", example = "1")
    @Max(value = 20, message = "最大值20")
    public int age;

    @ApiModelProperty(value = "amt", example = "1")
    @AmtValidate
    public BigDecimal amt;
}
