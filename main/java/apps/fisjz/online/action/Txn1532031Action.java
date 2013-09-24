package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4Refund;
import apps.fisjz.domain.staring.T2031Request.TIA2031;
import apps.fisjz.enums.TxnRtnCode;
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

        //ҵ���߼�����(��鴦���ظ�����)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(tia.getAreacode(), msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//�ظ��˸��ɿ�
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            msg.msgBody =  "�ɿ��˸�ȷ�ϳɹ�(�ظ�ȷ��)".getBytes("GBK");
            return msg;
        }

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Refund> paramList = new ArrayList<FbPaynotesInfo4Refund>();
        FbPaynotesInfo4Refund fbPaynotesInfo = new FbPaynotesInfo4Refund();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532031�˸��ɿ�ȷ��] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateRefundNontaxPayment(
                FISJZ_APPLICATIONID,
                FISJZ_BANK,
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //�жϲ�������Ӧ���
        if (getResponseResult(rtnlist)) { //�ɿ��˸��ɹ�
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            msg.msgBody =  "�ɿ��˸��ɹ�".getBytes("GBK");
        }else{ //�ɿ�ʧ��
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
