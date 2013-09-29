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
 * 1532010�ɿ����ѯ
 * zhanrui  20130922
 */

@Component
public class Txn1532010Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532010Action.class);

    @Autowired
    private T2010Service service;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Request");
        TIA2010 tia = null;
        try {
            tia = (TIA2010) dataFormat.fromMessage(new String(msg.msgBody), "TIA2010");
        } catch (Exception e) {
            logger.error("���Ľ�������:", e);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "���Ľ�������.".getBytes(THIRDPARTY_SERVER_CODING);
            return msg;
        }

        Map paramMap = new HashMap();
        paramMap.put("branchId", msg.branchID);
        paramMap.put("tellerId", msg.tellerID);
        paramMap.put("tia", tia);

        //��鱾�����м�¼��״̬
        FsJzfPaymentInfo fsJzfPaymentInfo = service.selectPaymentInfo(paramMap);
        if (fsJzfPaymentInfo == null) {//����δ�鵽��Ϣ
            service.processTxn(paramMap); //ȡ�����ַ�������Ϣ
            msg.rtnCode = (String)paramMap.get("rtnCode");
            msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
        } else {
            if ("1".equals(fsJzfPaymentInfo.getRecfeeflag())) { //�ѵ���
                //�ظ��ɿ�
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody = ("�˱ʽɿ�ѽɿ�.").getBytes(THIRDPARTY_SERVER_CODING);
            }else{  //δ���ˣ��������ѱ�����Ϣ
                service.processTxn_LocalInfo(paramMap); //ȡ������Ϣ
                msg.rtnCode = (String)paramMap.get("rtnCode");
                msg.msgBody = ((String)paramMap.get("rtnMsg")).getBytes(THIRDPARTY_SERVER_CODING);
            }
        }

        return msg;
    }
}
