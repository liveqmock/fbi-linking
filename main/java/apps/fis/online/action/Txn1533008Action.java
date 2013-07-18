package apps.fis.online.action;

import apps.fis.SystemParameter;
import apps.fis.domain.txn.Tia3002;
import apps.fis.domain.txn.Tia3008;
import apps.fis.domain.txn.Toa3002;
import apps.fis.domain.txn.Toa3008;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.repository.model.FsQdfPaymentInfo;
import apps.fis.repository.model.FsQdfPaymentItem;
import common.utils.ObjectFieldsCopier;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

// ��ѯִ�յ�λ��Ϣ
@Component
public class Txn1533008Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533008Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        logger.info("[1533008��ѯִ�յ�λ��Ϣ] [�����]" + msg.branchID + "[��Ա��]" + msg.tellerID +
                "������:" + new String(msg.msgBody));
        // ����������
        String[] fieldArray = org.apache.commons.lang.StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // ��������
        String areaCode = fieldArray[0];
        // ִ�յ�λ����
        String unitCode = fieldArray[1];

        Tia3008 tia3008 = new Tia3008();
        tia3008.BODY.DATA.XZQH = areaCode;
        tia3008.BODY.DATA.ZSDWBM = unitCode;

        Toa3008 toa3008 = (Toa3008) dataExchangeService.process(tia3008);
        if("0".equals(toa3008.HEAD.STATUS)) {
            msg.msgBody = new StringBuffer().append(toa3008.BODY.DATA.ZSDWBM).append("|")
                    .append(toa3008.BODY.DATA.ZSDWMC).append("|")
                    .append(toa3008.BODY.DATA.ZGBMBM).append("|")
                    .append(toa3008.BODY.DATA.ZGBMMC).append("|").toString().getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3008.HEAD.MESSAGE.getBytes();
        }

        return msg;
    }

}
