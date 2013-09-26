package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2013Request.TIA2013;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.online.service.T2013Service;
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
 * 1532013 �ֹ��ɿ���ɿ�
 * zhanrui  20130923
 */
@Component
public class Txn1532013Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532013Action.class);

    @Autowired
    private T2013Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2013Request");
        TIA2013 tia = null;
        try {
            tia = (TIA2013) dataFormat.fromMessage(new String(msg.msgBody), "TIA2013");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "���Ľ�������.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }
        logger.info("[1532013�ֹ��ɿ���ɿ�] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " Ʊ�ݱ��:" + tia.getPaynotesInfo().getNotescode());

        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //���ұ��ؼ�¼
        FsJzfPaymentInfo fsJzfPaymentInfo = service.selectPaymentInfo(paramMap);
        if (fsJzfPaymentInfo == null) {//����δ�鵽��Ϣ
            service.processTxn(paramMap);
            msg.rtnCode = (String)paramMap.get("rtnCode");
            msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        } else { //�ظ��ɿ�
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody =  "�˽ɿ����ѽɿ�".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }


        //ҵ���߼�����
        service.processTxn(paramMap);

        msg.rtnCode = (String)paramMap.get("rtnCode");
        msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        return msg;
    }
}
