package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @SuppressWarnings("unchecked")
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Request");
        TIA2031 tia = null;
        try {
            tia = (TIA2031) dataFormat.fromMessage(new String(msg.msgBody), "TIA2031");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "���Ľ�������.".getBytes("GBK");
            return msg;
        }
        logger.info("[1532031�ɿ��˸�] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " Ʊ�ݱ��:" + tia.getPaynotesInfo().getNotescode());

        //ҵ���߼�����(������ɫƽ̨�Զ������Ľ������鵵����)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(tia.getAreacode(), msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == -1) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "���Ȳ�ѯ�ɿ�˸���Ϣ.".getBytes("GBK");
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
                getApplicationidByAreaCode(tia.getAreacode()),
                getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);


        //�жϲ�������Ӧ���
        if (getResponseResult(rtnlist)) { //�ɿ�ɹ�
            //�����ϸ
            Map responseContentMap = (Map) rtnlist.get(0);
            FbPaynotesInfo respInfo = new FbPaynotesInfo();
            BeanUtils.populate(respInfo, responseContentMap);
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = "�ɿ��˸�����ʧ��!��ϸ�˶Բ���!".getBytes("GBK");
                return msg;
            }

            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            String rtnMsg = getResponseErrMsg(rtnlist);
            if (StringUtils.isEmpty(rtnMsg)) {
                msg.msgBody = "�ɿ��˸��ɹ�".getBytes("GBK");
            } else {
                msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
            }
        } else { //�ɿ�ʧ��
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
