package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2040Request.TIA2040;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2040Service;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 1532040缴款书冲销
 * zhanrui  20130923
 */
@Component
public class Txn1532040Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532040Action.class);

    @Autowired
    private T2040Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2040Request");
        TIA2040 tia = null;
        try {
            tia = (TIA2040)dataFormat.fromMessage(new String(msg.msgBody), "TIA2040");
        } catch (Exception e) {
            logger.error("报文解析错误:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "报文解析错误.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //本地数据检查
        FsJzfPaymentInfo fsJzfPaymentInfo = service.selectPaymentInfo(paramMap);
        if (fsJzfPaymentInfo == null) {//未查到记录
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "无待冲销缴款单.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }else {
            if ("0".equals(fsJzfPaymentInfo.getRecfeeflag())) { //未到账
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody = "此缴款单未缴款,不能做冲销.".getBytes(THIRDPARTY_SERVER_CODING);
                return msg;
            }
        }

        //业务逻辑处理
        service.processTxn(paramMap);

        msg.rtnCode = (String)paramMap.get("rtnCode");
        msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        return msg;
    }
}
