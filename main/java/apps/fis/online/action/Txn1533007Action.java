package apps.fis.online.action;

import apps.fis.domain.txn.Tia3007;
import apps.fis.domain.txn.Tia3008;
import apps.fis.domain.txn.Toa3007;
import apps.fis.domain.txn.Toa3008;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPaymentService;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// ������Ŀ��Ϣ��ѯ
@Component
public class Txn1533007Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533007Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        logger.info("[1533007������Ŀ��Ϣ��ѯ] [�����]" + msg.branchID + "[��Ա��]" + msg.tellerID +
                "������:" + new String(msg.msgBody));
        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // ��������
        String areaCode = fieldArray[0];
        // ִ�յ�λ����
        String unitCode = fieldArray[1];
        // ������Ŀ����
        String itemCode = fieldArray[2];

        Tia3007 tia3007 = new Tia3007();
        tia3007.BODY.DATA.XZQH = areaCode;
        tia3007.BODY.DATA.ZSDWBM = unitCode;
        tia3007.BODY.DATA.SRXMBM = itemCode;

        Toa3007 toa3007 = (Toa3007) dataExchangeService.process(tia3007);
        if ("0".equals(toa3007.HEAD.STATUS)) {
            msg.msgBody = new StringBuffer().append(toa3007.BODY.DATA.SRXMBM).append("|")
                    .append(toa3007.BODY.DATA.SRXMMC).append("|")
                    .append(toa3007.BODY.DATA.ZJXZBM).append("|")
                    .append(toa3007.BODY.DATA.ZJXZMC).append("|")
                    .append(toa3007.BODY.DATA.YSKMBM).append("|")
                    .append(toa3007.BODY.DATA.YSKMMC).append("|")
                    .append(StringUtils.isEmpty(toa3007.BODY.DATA.SRXMDW) ? "" : toa3007.BODY.DATA.SRXMDW).append("|")
                    .append(StringUtils.isEmpty(toa3007.BODY.DATA.SRXMBZ) ? "" : toa3007.BODY.DATA.SRXMBZ).append("|")
                    .toString().getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3007.HEAD.MESSAGE.getBytes();
        }

        return msg;
    }

}
