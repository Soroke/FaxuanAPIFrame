package annotation;

import java.lang.annotation.*;

/**
 * Created by song on 2017/5/19.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Key {
    public int value() default 1;
}
