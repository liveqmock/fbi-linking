package apps.fisjz.thirdparty;

import com.caucho.burlap.server.BurlapServlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
public class BurlapTestServlet extends BurlapServlet {
    public List queryAllElementCode(String applicationid, String elementCode, int year) {
        List elements = new ArrayList();
        elements.add("e1");
        elements.add("e2");
        elements.add("e3");
        return elements;
    }

    public List queryNontaxPayment(String applicationid, String bank, String year,
                                   String finorg, String notescode, String checkcode, String billtype) {
        List elements = new ArrayList();

        Map resultContentMap = new HashMap();
        resultContentMap.put("billid", "main1111");
        resultContentMap.put("paynotescode", "123456");
        resultContentMap.put("notescode", "222");
        resultContentMap.put("checkcode", "qwer");
        resultContentMap.put("amt", "123.45");
        resultContentMap.put("noteskindcode", "01");
        resultContentMap.put("noteskindname", null); //TODO

        List detailsList = new ArrayList();
        Map detailMap = new HashMap();
        detailMap.put("billid", "sub111");
        detailMap.put("mainid", "main1111");
        detailsList.add(detailMap);
        detailMap = new HashMap();
        detailMap.put("billid", "sub222");
        detailMap.put("mainid", "main1111");
        detailsList.add(detailMap);

        resultContentMap.put("details", detailsList);

        elements.add(resultContentMap);

        //-
        Map resultMsgMap = new HashMap();
        resultMsgMap.put("RESULT", "SUCCESS");
        resultMsgMap.put("MESSAGE", "成功信息...");
        elements.add(resultMsgMap);

        return elements;
    }

    public List updateNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList){
        //返回成功信息
        List elements = new ArrayList();
        Map resultMsgMap = new HashMap();
        resultMsgMap.put("RESULT", "SUCCESS");
        resultMsgMap.put("MESSAGE", "成功信息...");
        elements.add(resultMsgMap);
        return elements;

        //返回失败信息 TODO
    }

}
