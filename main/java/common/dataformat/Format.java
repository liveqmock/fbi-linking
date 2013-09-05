
package common.dataformat;


/**
 * User: zhanrui
 * Date: 13-9-4
 * Time: ионГ9:30
 */
public interface Format<T> {
    String format(T object) throws Exception;
    T parse(String string) throws Exception;
}
