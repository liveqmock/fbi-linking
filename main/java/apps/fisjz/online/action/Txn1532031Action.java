package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.staring.T2031Request.TIA2031;
import apps.fisjz.domain.staring.T2031Response.TOA2031;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.online.service.PaymentService;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1532031 �ɿ��˸�ȷ��
 * zhanrui  20130924
 */

@Component
public class Txn1532031Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532031Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Request");
        TIA2031 tia = (TIA2031)dataFormat.fromMessage(new String(msg.msgBody), "TIA2031");
        logger.info("[1532031�˸��ɿ�ȷ��] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " �˸��ɿ�����:" + tia.getPaynotesInfo().getRefundapplycode());

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532031�˸��ɿ�ȷ��] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateNontaxPayment(FISJZ_APPLICATIONID,FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //�жϲ�������Ӧ���
        if (!getResponseResult(rtnlist)) {
            throw  new RuntimeException(getResponseErrMsg(rtnlist));
        }

        //ҵ���߼�����
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        paymentService.processRefundPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);

        //����ɫƽ̨��Ӧ����
        TOA2031 toa = new TOA2031();
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Response");
        String toaMsg = (String)dataFormat.toMessage(modelObjectsMap);
        msg.msgBody = toaMsg.getBytes();
        return msg;
    }
}
