package springboot.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class AmtValidateRule implements ConstraintValidator<AmtValidate, BigDecimal> {


    @Override
    public void initialize(AmtValidate constraintAnnotation) {

    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            return amtValidate(value);
        }
    }

    private boolean amtValidate(BigDecimal amt) {
        if (amt.compareTo(new BigDecimal("0")) < 0) {
            return false;
        }
        return true;
    }
}
