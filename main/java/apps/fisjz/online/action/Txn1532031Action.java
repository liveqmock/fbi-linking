package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2031Request.TIA2031;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2030Service;
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
 * 1532031 缴款退付确认
 * zhanrui  20130924
 */
@Component
public class Txn1532031Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532031Action.class);

    @Autowired
    private T2030Service service;

    @Override
    @SuppressWarnings("unchecked")
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Request");
        TIA2031 tia = null;
        try {
            tia = (TIA2031) dataFormat.fromMessage(new String(msg.msgBody), "TIA2031");
        } catch (Exception e) {
            logger.error("报文解析错误:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "报文解析错误.".getBytes(THIRDPARTY_SERVER_CODING);
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
            msg.msgBody = "请先查询退付缴款单信息.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }else {
            if ("1".equals(fsJzfPaymentInfo.getFbBookFlag())) {
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody = ("此缴款单已退付,日期:" + fsJzfPaymentInfo.getBankrecdate()).getBytes(THIRDPARTY_SERVER_CODING);
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
