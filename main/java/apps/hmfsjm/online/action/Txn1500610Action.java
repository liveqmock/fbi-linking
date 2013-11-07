package apps.hmfsjm.online.action;

import apps.hmfsjm.enums.TxnRtnCode;
import apps.hmfsjm.gateway.domain.txn.Tia1001;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.gateway.service.ProtocolTxnService;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//	即墨房屋维修资金缴款查询
@Component
public class Txn1500610Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1500610Action.class);
    @Autowired
    private ProtocolTxnService protocolTxnService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 缴款书编号
        String billNo = fieldArray[0];

        logger.info("[1500610缴款书信息查询][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [缴款书编号] " + billNo);

        Tia1001 tia = new Tia1001();
        tia.BODY.PAY_BILLNO = billNo;
        try {
            Toa1001 toa = (Toa1001) protocolTxnService.execute("1001", tia);
            msg.msgBody = assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING);
        } catch (Exception e) {
            logger.error("[1500610][1001][hmfsjm缴款单查询]失败", e);
            msg.rtnCode = TxnRtnCode.TXN_FAILED.getCode();
            msg.msgBody = TxnRtnCode.TXN_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(Toa1001 toa1001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa1001.BODY.PAY_BILLNO).append("|")                   // 缴款书编号
                .append(nullToEmpty(toa1001.BODY.BILL_STS_CODE)).append("|")     // 缴款单状态代码
                .append(nullToEmpty(toa1001.BODY.BILL_STS_TITLE)).append("|")    // 缴款单状态说明
                .append(nullToEmpty(toa1001.BODY.HOUSE_ID)).append("|")          // 房屋编号
                .append(nullToEmpty(toa1001.BODY.HOUSE_LOCATION)).append("|")    // 房屋坐落
                .append(nullToEmpty(toa1001.BODY.HOUSE_AREA)).append("|")        // 建筑面积
                .append(nullToEmpty(toa1001.BODY.STANDARD)).append("|")          // 缴存标准
                .append(nullToEmpty(toa1001.BODY.TXN_AMT)).append("|")           // 金额
                .append(nullToEmpty(toa1001.BODY.PAY_BANK)).append("|")          // 缴款银行
                .append(nullToEmpty(toa1001.BODY.AREA_ACCOUNT)).append("|")      // 专户账号
                .append(nullToEmpty(toa1001.BODY.HOUSE_ACCOUNT)).append("|")     // 分户账号
                .append(nullToEmpty(toa1001.BODY.CARD_TYPE)).append("|")         // 证件类型
                .append(nullToEmpty(toa1001.BODY.CARD_NO)).append("|")           // 证件号码
                .append(nullToEmpty(toa1001.BODY.OWNER)).append("|")             // 业主姓名
                .append(nullToEmpty(toa1001.BODY.TEL)).append("|")               // 联系电话
                .append(nullToEmpty(toa1001.BODY.RESERVE)).append("|");          // 保留域
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
