package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.staring.T2012Request.TIA2012;
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
 * 1532012�ɿ��鵽��ȷ��
 * zhanrui  20130923
 */
@Component
public class Txn1532012Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532012Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2012Request");
        TIA2012 tia = (TIA2012) dataFormat.fromMessage(new String(msg.msgBody), "TIA2012");
        logger.info("[1532012�ɿ��鵽��ȷ��] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " �ɿ�����:" + tia.getPaynotesInfo().getNotescode());

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532012�ɿ��鵽��ȷ��] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.accountNontaxPayment(
                FISJZ_APPLICATIONID,
                FISJZ_BANK,
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //�жϲ�������Ӧ���
        if (getResponseResult(rtnlist)) { //����ȷ�ϳɹ�
            //ҵ���߼�����
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
            paymentService.processPaymentPayAccount(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            msg.msgBody =  "����ȷ�ϳɹ�".getBytes("GBK");
        }else{ //����ȷ��ʧ��
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
