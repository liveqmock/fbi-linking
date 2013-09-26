package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2010Request.TIA2010;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2010Service;
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
 * 1532010缴款书查询
 * zhanrui  20130922
 */

@Component
public class Txn1532010Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532010Action.class);

    @Autowired
    private T2010Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Request");
        TIA2010 tia = null;
        try {
            tia = (TIA2010) dataFormat.fromMessage(new String(msg.msgBody), "TIA2010");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "报文解析错误.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }

        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //查找本地记录
        FsJzfPaymentInfo fsJzfPaymentInfo = service.selectPaymentInfo(paramMap);
        if (fsJzfPaymentInfo == null) {//本地未查到信息
            service.processTxn(paramMap); //取财政局服务器信息
            msg.rtnCode = (String)paramMap.get("rtnCode");
            msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        } else {
            if ("0".equals(fsJzfPaymentInfo.getFbBookFlag())) {//本地已有初始信息，但未缴款
                service.processTxn_LocalInfo(paramMap); //取本地信息
                msg.rtnCode = (String)paramMap.get("rtnCode");
                msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
            } else { //重复缴款
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody =  "此缴款书已缴款".getBytes(THIRDPARTY_SERVER_CODING);
            }
        }
        return msg;
    }
}
