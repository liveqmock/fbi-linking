package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfoExport;
import apps.fisjz.domain.staring.T2010Request.TIA2010;
import apps.fisjz.domain.staring.T2081Request.TIA2081;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2081Service;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1532010缴款书查询
 * zhanrui  20130922
 */

@Component
public class Txn1532081Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532081Action.class);

    @Autowired
    private T2081Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2081Request");
        TIA2081 tia = null;
        try {
            tia = (TIA2081) dataFormat.fromMessage(new String(msg.msgBody), "TIA2081");

        } catch (Exception e) {
            logger.error("报文解析错误:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "报文解析错误.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }

        Map paramMap = new HashMap();
        paramMap.put("tia", tia);

        //
        int recNum = service.selectTotalExportForStaring(paramMap);
        if (recNum == 0) {//本地未查到信息
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = ("当日没有缴款记录信息，无需统计！").getBytes(THIRDPARTY_SERVER_CODING);
        } else {
            service.processTxn(paramMap); //取本地信息
            msg.rtnCode = (String)paramMap.get("rtnCode");
            msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }
}
