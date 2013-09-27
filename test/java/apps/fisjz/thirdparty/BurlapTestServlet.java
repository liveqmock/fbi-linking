package apps.fisjz.thirdparty;

import com.caucho.burlap.server.BurlapServlet;

import java.io.UnsupportedEncodingException;
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

    public List queryNontaxPayment(String applicationid, String bank, String year, String finorg,
                                   String notescode, String checkcode, String billtype) {
        List elements = new ArrayList();

        Map resultContentMap = new HashMap();
/*
        resultContentMap.put("billtype", "1");
        resultContentMap.put("billid", "BILL001");
        resultContentMap.put("paynotescode", "JKSDH001");
        resultContentMap.put("notescode", "PJBH001");
        resultContentMap.put("checkcode", "YZM001");
        resultContentMap.put("amt", "123.45");
        resultContentMap.put("noteskindcode", "01");
        resultContentMap.put("noteskindname", null); //TODO

        List detailsList = new ArrayList();
        Map detailMap = new HashMap();
        detailMap.put("billid", "SUB_BILL001");
        detailMap.put("mainid", "BILL001");
        detailMap.put("amount", "1");
        detailMap.put("amt", "1.23");
        detailsList.add(detailMap);
        detailMap = new HashMap();
        detailMap.put("billid", "SUB_BILL002");
        detailMap.put("mainid", "BILL001");
        detailMap.put("amount", "2");
        detailMap.put("amt", "2.23");
        detailsList.add(detailMap);

*/

        resultContentMap.put("billid", "2075");
        resultContentMap.put("paynotescode", "2013000000001");
        resultContentMap.put("notescode", "130000010001");
        resultContentMap.put("checkcode", "FM5EH6");
        resultContentMap.put("amt", "1600");
        resultContentMap.put("noteskindcode", "00000022");
        resultContentMap.put("bankrecdate", "20130924");
        resultContentMap.put("bankacctdate", "20130924");
        resultContentMap.put("ispreaudit", "1");
        resultContentMap.put("recfeeflag", "0");
        resultContentMap.put("billtype", "0");
        resultContentMap.put("latefee", "0.00");

        List detailsList = new ArrayList();
        Map detailMap = new HashMap();
        detailMap.put("billid", "SUB_BILL001");
        detailMap.put("mainid", "2075");
        detailMap.put("amount", "1");
        detailMap.put("amt", "1000");
        detailsList.add(detailMap);
        detailMap = new HashMap();
        detailMap.put("billid", "SUB_BILL002");
        detailMap.put("mainid", "2075");
        detailMap.put("amount", "2");
        detailMap.put("amt", "600");
        detailsList.add(detailMap);

        resultContentMap.put("details", detailsList);

        elements.add(resultContentMap);

        //-
        Map resultMsgMap = new HashMap();
        resultMsgMap.put("RESULT", "SUCCESS");
        //resultMsgMap.put("MESSAGE", new String("成功信息.....".getBytes("GBK"),"UTF-8"));
        resultMsgMap.put("MESSAGE", "...");
        elements.add(resultMsgMap);

        return elements;
    }

    //缴款
    public List updateNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList) throws UnsupportedEncodingException {
        //返回成功信息
        List elements = new ArrayList();

        Map resultContentMap = new HashMap();
        resultContentMap.put("billid", "2075");
        resultContentMap.put("paynotescode", "2013000000001");
        resultContentMap.put("notescode", "130000010001");
        elements.add(resultContentMap);
        Map resultMsgMap = new HashMap();
        resultMsgMap.put("RESULT", "SUCCESS");
        resultMsgMap.put("MESSAGE", "成功信息...");
        elements.add(resultMsgMap);

//        resultMsgMap.put("RESULT", "FAIL");
//        resultMsgMap.put("MESSAGE", "缴款书130000010002已确认收款，不能重复操作!!！");
//        elements.add(resultMsgMap);

        return elements;
    }

    //到账确认
    public List accountNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList) throws UnsupportedEncodingException {
        //返回成功信息
        List elements = new ArrayList();

        Map resultContentMap = new HashMap();
        resultContentMap.put("billid", "2075");
        resultContentMap.put("paynotescode", "2013000000001");
        resultContentMap.put("notescode", "130000010001");
        elements.add(resultContentMap);

        Map resultMsgMap = new HashMap();
        resultMsgMap.put("RESULT", "SUCCESS");
        resultMsgMap.put("MESSAGE", "成功信息...");

//        resultMsgMap.put("RESULT", "FAIL");
//        resultMsgMap.put("MESSAGE", "缴款书130000010002已确认收款，不能重复操作！");

        elements.add(resultMsgMap);
        return elements;

        //返回失败信息 TODO
    }




    //冲销
    public List cancelNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList) throws UnsupportedEncodingException {
        //返回成功信息
        List elements = new ArrayList();

        Map resultContentMap = new HashMap();
        resultContentMap.put("billid", "2075");
        resultContentMap.put("paynotescode", "2013000000001");
        resultContentMap.put("notescode", "130000010001");
        elements.add(resultContentMap);

        Map resultMsgMap = new HashMap();
        resultMsgMap.put("RESULT", "SUCCESS");
        resultMsgMap.put("MESSAGE", "成功信息...");

//        resultMsgMap.put("RESULT", "FAIL");
//        resultMsgMap.put("MESSAGE", "缴款书130000010002已确认收款，不能重复操作！");

        elements.add(resultMsgMap);
        return elements;

        //返回失败信息 TODO
    }

}
