package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2010Response.PaynotesInfo;
import apps.fisjz.domain.staring.T2010Response.PaynotesItem;
import apps.fisjz.domain.staring.T2010Request.TIA2010;
import apps.fisjz.domain.staring.T2010Response.TOA2010;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//1532010缴款书查询
@Component
public class Txn1532010Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1532010Action.class);

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Request");
        TIA2010 tia2010 = (TIA2010)dataFormat.fromMessage(new String(msg.msgBody), "TIA2010");

        logger.info("[1532010缴款书信息查询][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [缴款书编号] " + tia2010.getNotescode());

        //与第三方机构通讯


        //组响应报文
        TOA2010 toa2010 = new TOA2010();
        PaynotesInfo paynotesInfo = new PaynotesInfo();
        paynotesInfo.setAmt("123123.23");
        toa2010.setPaynotesInfo(paynotesInfo);

        List<PaynotesItem> paynotesItems = new ArrayList<PaynotesItem>();
        PaynotesItem item = new PaynotesItem();
        item.setAmt("1111.11");
        paynotesItems.add(item);
        item = new PaynotesItem();
        item.setAmt("2222.22");
        paynotesItems.add(item);

        toa2010.setPaynotesItems(paynotesItems);
        toa2010.setItemNum("2");

        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa2010.getClass().getName(),  toa2010);
        modelObjectsMap.put(toa2010.getPaynotesInfo().getClass().getName(),  toa2010.getPaynotesInfo());

        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Response");
        String result = (String)dataFormat.toMessage(modelObjectsMap);

        msg.msgBody = result.getBytes();
        return msg;
    }


    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private String castToDate8(String srcDate) {
        if (srcDate == null) {
            return "        ";
        } else if (srcDate.length() > 8) {
            return srcDate.substring(0, 4) + srcDate.substring(5, 7) + srcDate.substring(8, 10);
        } else {
            return srcDate;
        }
    }
}
