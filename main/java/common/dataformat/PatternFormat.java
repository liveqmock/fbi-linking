package common.dataformat;
/**
 * User: zhanrui
 * Date: 13-9-7
 */
public interface PatternFormat<T> extends Format<T> {

    String getPattern();
}
