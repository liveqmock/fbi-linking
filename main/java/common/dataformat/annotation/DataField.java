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
public @interface DataField {
    //顺序号 1，2，3...
    int seq();

    //字段名字
    String name() default "";

    //格式转换模式
    String pattern() default "";

    //定长格式时的长度
    int length() default 0;

    //定长格式时的对齐方式
    String align() default "R";

    //定长格式时的补齐方式
    char padchar() default ' ';

    //是否做trim处理
    boolean trim() default false;

    //BogDecimal 精度
    int precision() default 0;

    //明细循环输出标志
    //int loop() default 0;
}
