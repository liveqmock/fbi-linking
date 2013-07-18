package apps.fis.online.action;

import apps.fis.domain.txn.Tia3008;
import apps.fis.domain.txn.Tia3048;
import apps.fis.domain.txn.Toa3008;
import apps.fis.domain.txn.Toa3048;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 对账失败明细查询
@Component
public class Txn1533048Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533048Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 行政区划
        String areaCode = fieldArray[0];
        // 对账日期
        String chkDate = fieldArray[1];

        logger.info("[对账日期]" + chkDate);

        // TODO
        if(true) {
            msg.msgBody = "".getBytes();
            return msg;
        }

        Tia3048 tia3048 = new Tia3048();
        // 对账文件名
        tia3048.BODY.DATA.DZWJM = "jslfjslfjslfs.txt";

        Toa3048 toa3048 = (Toa3048) dataExchangeService.process(tia3048);
        if("0".equals(toa3048.HEAD.STATUS)) {

            logger.info("[对账日期]" + chkDate + " [对账失败文件名]" + toa3048.BODY.DATA.DZSBWJM);
//            msg.msgBody = new StringBuffer().append(toa3048.BODY.DATA.ZSDWBM).append("|")
//                    .append(toa3048.BODY.DATA.ZSDWMC).append("|")
//                    .append(toa3048.BODY.DATA.ZGBMBM).append("|")
//                    .append(toa3048.BODY.DATA.ZGBMMC).append("|").toString().getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = (toa3048.HEAD.ERRCODE + toa3048.HEAD.MESSAGE).getBytes();
        }

        return msg;
    }

}
