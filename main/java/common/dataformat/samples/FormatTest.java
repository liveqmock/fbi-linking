package common.dataformat.samples;

import common.dataformat.SeperatedTextDataFormat;
import common.dataformat.samples.staringmodel.T1000.TIA1000;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-8
 * Time: 上午10:53
 */
public class FormatTest {

    public static void main(String... argv) throws Exception {
        //解包
        String tiaStr = "1000|20130910|111|aaa|3|99999,a|88888,b|77777,c";
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("common.dataformat.samples.staringmodel.T1000");
        //TIA1000 tia1000 = (TIA1000)((Map)dataFormat.fromMessage(tiaStr)).get("common.dataformat.samples.staringmodel.T1000.TIA1000");
        TIA1000 tia1000 = (TIA1000)dataFormat.fromMessage(tiaStr, "TIA1000");
        System.out.println(tia1000.getId());
        System.out.println(tia1000.getName());

        //打包
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(tia1000.getClass().getName(),  tia1000);
        modelObjectsMap.put(tia1000.getHeader().getClass().getName(),  tia1000.getHeader());
        String result = (String)dataFormat.toMessage(modelObjectsMap);
        System.out.println(result);
    }
}
