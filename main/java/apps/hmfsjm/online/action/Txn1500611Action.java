package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Tia1001;
import apps.hmfsjm.gateway.domain.txn.Tia2001;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.gateway.domain.txn.Toa2001;
import apps.hmfsjm.gateway.service.ProtocolTxnService;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//	即墨房屋维修资金缴款确认
@Component
public class Txn1500611Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500611Action.class);
    @Autowired
    private ProtocolTxnService protocolTxnService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 缴款书编号
        String billNo = fieldArray[0];

        logger.info("[1500611][2001][hmfsjm缴款书缴款确认][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [缴款书编号] " + billNo);

        Tia2001 tia = new Tia2001();
        tia.BODY.PAY_BILLNO = billNo;
        try {
            Toa2001 toa = (Toa2001) protocolTxnService.execute("2001", tia);
            // TODO 本地保存
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
        } catch (Exception e) {
            logger.error("[1500611][2001][hmfsjm缴款单缴款确认]失败", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa2001 toa2001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa2001.BODY.PAY_BILLNO).append("|")                   // 缴款书编号
                .append(nullToEmpty(toa2001.BODY.BILL_STS_CODE)).append("|")     // 缴款单状态代码
                .append(nullToEmpty(toa2001.BODY.BILL_STS_TITLE)).append("|")    // 缴款单状态说明
                .append(nullToEmpty(toa2001.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
