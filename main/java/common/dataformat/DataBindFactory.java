package common.dataformat;

import java.util.List;
import java.util.Map;

/**
 * User: zhanrui
 * Date: 13-9-4
 * Time: ионГ9:35
 */
public interface DataBindFactory {

    void initModel() throws Exception;
    void bind(List<String> data, Map<String, Object> model) throws Exception;
    String unbind(Map<String, Object> model) throws Exception;
}
