package springboot.demo.common;

import java.lang.annotation.*;

/**
 *
 * Created by AA on 2017/7/12.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnotationDemo {
    public String name()  default  "";
    public String like()  default  "";
    public String key ()  default  "";
}
