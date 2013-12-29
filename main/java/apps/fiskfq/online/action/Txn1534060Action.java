package apps.fiskfq.online.action;

import apps.fiskfq.enums.TxnRtnCode;
import apps.fiskfq.gateway.domain.base.ToaXml;
import apps.fiskfq.gateway.domain.txn.Tia2409;
import apps.fiskfq.gateway.domain.txn.Tia2425;
import apps.fiskfq.online.service.Txn1534040Service;
import apps.fiskfq.online.service.Txn1534060Service;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//	对账
@Component
public class Txn1534060Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1534060Action.class);
    private Txn1534060Service txn1534060Service = new Txn1534060Service();

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {


        // 解析报文体
        // TODO
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        /*

	年度
	企业号
	总金额
begin_date	收款确认起始日期	Date		对账时间范围	M
end_date	收款确认结束日期	Date		对账时间范围	M

         */
        String year = fieldArray[0];
        String en_code = fieldArray[1];
        String totalAmt = fieldArray[2];
        String begin_date = fieldArray[3];
        String end_date = fieldArray[4];


        logger.info("[1534060对账][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "  [总金额]" + totalAmt + "  [收款确认起始日期] " + begin_date + "[收款确认结束日期]" + end_date);

        int detailCnt = Integer.parseInt(fieldArray[5]);
        for (int i = 0; i < detailCnt; i++) {
            String[] detailFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(fieldArray[6 + i], ",");
            logger.info("流水号：" + detailFields[0]);
            logger.info("票据号：" + detailFields[1]);
            logger.info("交易金额：" + detailFields[2]);
        }

        try {


            // TODO 交款成功确认

        } catch (Exception e) {
            logger.error("[1534060][对账]失败", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                msg.msgBody = TxnRtnCode.TXN_EXECUTE_FAILED.getTitle().getBytes(THIRDPARTY_SERVER_CODING);
            } else
                msg.msgBody = e.getMessage().getBytes(THIRDPARTY_SERVER_CODING);
        }
        return msg;
    }

    private String assembleStr(ToaXml toa) {
        StringBuilder strBuilder = new StringBuilder();


        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
