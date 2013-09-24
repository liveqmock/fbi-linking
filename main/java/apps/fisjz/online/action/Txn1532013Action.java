package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4Manual;
import apps.fisjz.domain.financebureau.FbPaynotesItem;
import apps.fisjz.domain.staring.T2013Request.TIA2013;
import apps.fisjz.domain.staring.T2013Request.TIA2013PaynotesItem;
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
 * 1532013 �ֹ��ɿ���ɿ�
 * zhanrui  20130923
 */
@Component
public class Txn1532013Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532013Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2013Request");
        TIA2013 tia = (TIA2013) dataFormat.fromMessage(new String(msg.msgBody), "TIA2013");
        logger.info("[1532013�ֹ��ɿ���ɿ�] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " Ʊ�ݱ��:" + tia.getPaynotesInfo().getNotescode());

        //ҵ���߼�����(��鴦���ظ�����)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//�ظ��ɿ�
            msg.rtnCode = "0000";
            msg.msgBody =  "�ɿ�ɹ�(�ظ��ɿ�)".getBytes("GBK");
            return msg;
        }

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Manual> paramList = new ArrayList<FbPaynotesInfo4Manual>();
        FbPaynotesInfo4Manual fbPaynotesInfo = new FbPaynotesInfo4Manual();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());

        List<TIA2013PaynotesItem> tiaItems = tia.getPaynotesItems();
        List<FbPaynotesItem> fbItems = new ArrayList<FbPaynotesItem>();
        for (TIA2013PaynotesItem tiaItem : tiaItems) {
            FbPaynotesItem  fbItem = new FbPaynotesItem();
            BeanUtils.copyProperties(fbItem, tiaItem);
            fbItems.add(fbItem);
        }
        fbPaynotesInfo.setDetails(fbItems);
        paramList.add(fbPaynotesInfo);

        logger.info("[1532013�ֹ��ɿ���ɿ�] ��������Ϣ������������:" + fbPaynotesInfo.toString());
        List rtnlist = service.insertNontaxPayment(FISJZ_APPLICATIONID, FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //�жϲ�������Ӧ���
        if (getResponseResult(rtnlist)) { //�ɿ�ɹ�
            msg.rtnCode = "0000";
            msg.msgBody =  "�ֹ��ɿ�ɹ�".getBytes("GBK");
        }else{ //�ɿ�ʧ��
            msg.rtnCode = "1002";
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
