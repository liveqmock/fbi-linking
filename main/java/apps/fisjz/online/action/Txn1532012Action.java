package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2012Request.TIA2012;
import apps.fisjz.enums.TxnRtnCode;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 1532012�ɿ��鵽��ȷ�� (�������ĳ���linking�Զ�����)
 * zhanrui  20130923
 */
@Component
public class Txn1532012Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532012Action.class);

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2012Request");
        TIA2012 tia = null;
        try {
            tia = (TIA2012) dataFormat.fromMessage(new String(msg.msgBody), "TIA2012");
        } catch (Exception e) {
            logger.error("���Ľ�������:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "���Ľ�������.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        logger.info("[1532012�ɿ��鵽��ȷ��] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " �ɿ�����:" + tia.getPaynotesInfo().getNotescode());

        msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
        msg.msgBody =  "��֧�ִ˽���.".getBytes(THIRDPARTY_SERVER_CODING);
        return msg;
    }
}
