package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.staring.T2011Request.TIA2011;
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
 * 1532011�ɿ���ɿ�
 * zhanrui  20130923
 */
@Component
public class Txn1532011Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532011Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    @SuppressWarnings("unchecked")
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2011Request");
        TIA2011 tia = null;
        try {
            tia = (TIA2011) dataFormat.fromMessage(new String(msg.msgBody), "TIA2011");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "���Ľ�������.".getBytes("GBK");
            return msg;
        }
        logger.info("[1532011�ɿ���ɿ�] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " Ʊ�ݱ��:" + tia.getPaynotesInfo().getNotescode());

        //ҵ���߼�����(������ɫƽ̨�Զ������Ľ������鵵����)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(tia.getAreacode(), msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == -1) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "���Ȳ�ѯ�ɿ��Ϣ.".getBytes("GBK");
            return msg;
        }

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532011�ɿ���ɿ�] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateNontaxPayment(getApplicationidByAreaCode(tia.getAreacode()),
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
                msg.msgBody = "�ɿ��ʧ��!��ϸ�˶Բ���!".getBytes("GBK");
                return msg;
            }

            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            String rtnMsg = getResponseErrMsg(rtnlist);
            if (StringUtils.isEmpty(rtnMsg)) {
                msg.msgBody = "�ɿ�ɹ�".getBytes("GBK");
            } else {
                msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
            }
            //�Զ�����ɿ�ȷ�Ͻ���
            rtnlist = processPayConfirmTxn(msg, tia);
            if (!getResponseResult(rtnlist)) {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
                return msg;
            }
        } else { //�ɿ�ʧ��
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }

    //����ɿ�ȷ�Ͻ���
    private List processPayConfirmTxn(final LFixedLengthProtocol msg, final TIA2011 tia) throws Exception {
        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532012�ɿ��鵽��ȷ��] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist =  service.accountNontaxPayment(
                getApplicationidByAreaCode(tia.getAreacode()),
                getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //�жϲ�������Ӧ���
        if (getResponseResult(rtnlist)) { //����ȷ�ϳɹ�
            //ҵ���߼�����
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
            paymentService.processPaymentPayAccount(tia.getAreacode(),msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        }
        return rtnlist;
    }
}
