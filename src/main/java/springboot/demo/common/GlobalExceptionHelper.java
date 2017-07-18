package springboot.demo.common;

import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.demo.cache.ResCodeMessageCache;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice(annotations = RestController.class)
class GlobalExceptionHelper {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
   public Object Exception(Exception ex, HttpServletResponse response) throws Exception {
        List<String> message = new ArrayList();
        if (ex instanceof UndeclaredThrowableException) {
            ex = getRealException((UndeclaredThrowableException)ex);
        }
        if (ex instanceof ValidationException) {
            ValidationException ve = (ValidationException)ex;
            if (ve instanceof ConstraintViolationException) {
                ConstraintViolationException exception = (ConstraintViolationException)ve;
                if (exception.getConstraintViolations() != null) {
                    for (ConstraintViolation item : exception.getConstraintViolations()) {
                        message.add(item.getMessage());
                    }
                }
            } else {
                message.add(ex.getMessage());
            }
            ex = new ApiException("9000", message.toString());
        } else if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException missing = (MissingServletRequestParameterException)ex;
            ex = new ApiException("9202", missing.getParameterName() + "必填");
        } else if (ex instanceof TypeMismatchException) {
            ex = new ApiException("9202", "参数类型错误");
        } else if (ex instanceof HttpMessageNotReadableException) {
            ex = new ApiException("9202", "参数类型错误");
        } else if (ex instanceof RuntimeException) {
            if (ex.getMessage() != null && !"".equals(ex.getMessage())) {
                String desc = ResCodeMessageCache.getMessageDesc(ex.getMessage());
                if (desc != null && !desc.isEmpty()) {
                    ex = new ApiException(ex.getMessage());
                }
            }
        } else if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException)ex).getBindingResult();
            List<ObjectError> FieldErrors = bindingResult.getAllErrors();
            for (ObjectError oe:  FieldErrors) {
                FieldError fe = (FieldError)oe;
                Class tarClass = bindingResult.getTarget().getClass();
                int levelTar = 0;
                int tarClassIndex = -1;
                while (tarClassIndex == -1 && tarClass != null) {
                    for (int i = 0; i < tarClass.getDeclaredFields().length; i ++) {
                        Field field = tarClass.getDeclaredFields()[i];
                        if(field.getName().equals(fe.getField())) {
                            tarClassIndex = i;
                        }
                    }
                    if (tarClassIndex < 0) {
                        tarClass = tarClass.getSuperclass();
                    }
                    levelTar++;
                    if (levelTar == 5) {
                        break;
                    }
                }

                ApiModelProperty apiModelProperty = null;
                if (levelTar < 5 && tarClass != null) {
                    apiModelProperty = tarClass.getDeclaredField(fe.getField()).getAnnotation(ApiModelProperty.class);
                }
                if (apiModelProperty != null) {
                    String desc = apiModelProperty.value();
                    if (!desc.isEmpty()) {
                        message.add(desc + fe.getDefaultMessage());
                    }
                } else {
                    message.add(fe.getDefaultMessage());
                }
                if (message.size() == 0) message.add(fe.getField() + fe.getDefaultMessage());
            }

            ex = new ApiException("9202", message.toString());
        }
        logger.warn("error", ex);
        return ApiException.getResponse(ex);
    }

    Exception getRealException(UndeclaredThrowableException e) {
        if (e == null) {
            return null;
        }
        Throwable cause = e.getCause();
        if (cause instanceof UndeclaredThrowableException) {
            return getRealException((UndeclaredThrowableException)cause);
        }
        return (Exception)cause;
    }
}
