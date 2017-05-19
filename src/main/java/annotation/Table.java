package annotation;

import java.lang.annotation.*;

/**
 * Created by song on 2017/5/19.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    public int value() default 1;
}
