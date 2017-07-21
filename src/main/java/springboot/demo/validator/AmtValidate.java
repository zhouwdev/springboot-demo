package springboot.demo.validator;

import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AmtValidateRule.class)
@Documented
public @interface AmtValidate {
    String message() default "金额不能为负数";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
