package common.dataformat.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-6
 */

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixedLengthRecord {

    String name() default "";

    char padchar() default ' ';
    
}
