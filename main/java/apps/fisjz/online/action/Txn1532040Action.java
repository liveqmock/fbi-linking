package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4Cancel;
import apps.fisjz.domain.staring.T2040Request.TIA2040;
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
import java.util.List;

/**
 * 1532040�ɿ������
 * zhanrui  20130923
 */
@Component
public class Txn1532040Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532040Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2040Request");
        TIA2040 tia = (TIA2040)dataFormat.fromMessage(new String(msg.msgBody), "TIA2040");
        logger.info("[1532040�ɿ����] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " Ʊ�ݱ��:" + tia.getPaynotesInfo().getNotescode());

        //ҵ���߼�����(��鴦���ظ�����)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//�ظ��ɿ�
            msg.rtnCode = "0000";
            msg.msgBody =  "�ɿ�����ɹ�(�ظ�����)".getBytes("GBK");
            return msg;
        }

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Cancel> paramList = new ArrayList<FbPaynotesInfo4Cancel>();
        FbPaynotesInfo4Cancel fbPaynotesInfo = new FbPaynotesInfo4Cancel();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532040�ɿ����] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.cancelNontaxPayment(FISJZ_APPLICATIONID, FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //�жϲ�������Ӧ���
        if (getResponseResult(rtnlist)) { //�ɿ�ɹ�
            msg.rtnCode = "0000";
            msg.msgBody =  "�ɿ�����ɹ�".getBytes("GBK");
        }else{ //�ɿ�ʧ��
            msg.rtnCode = "1000";
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
