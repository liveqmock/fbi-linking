package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.staring.T2012Request.TIA2012;
import apps.fisjz.domain.staring.T2012Response.TOA2012;
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
        // ����������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2012Request");
        TIA2012 tia = (TIA2012)dataFormat.fromMessage(new String(msg.msgBody), "TIA2012");

        logger.info("[1532012�ɿ���ɿ�] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " �ɿ�����:" + tia.getPaynotesInfo().getNotescode());

        //�����������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();

        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        List rtnlist = service.updateNontaxPayment("appid", "bank", "" + 2013, "", paramList);
        logger.info("[1532012�ɿ���ɿ�] ��������Ϣ������������:" + fbPaynotesInfo.toString());

        Map responseMap = (Map) rtnlist.get(0);
        String rtnResult = (String)responseMap.get("RESULT");
        String rtnMsg = "";
        boolean isSuccess = false;
        if (rtnResult != null && !"".equals(rtnResult)) {
            if ("SUCCESS".equals(rtnResult)) {
                FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
                BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
                paymentService.processPaymentPayAccount(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
                isSuccess = true;
            }else{
                rtnMsg =  (String)responseMap.get("MESSAGE");
            }
        }

        //����Ӧ����
        TOA2012 toa = new TOA2012();
        if (isSuccess) {
            toa.setRtnCode("0000");
        } else {
            toa.setRtnCode("1000");
            toa.setRtnMsg(rtnMsg); //TODO ���ȴ���
        }

        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);

        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2012Response");
        String toaMsg = (String)dataFormat.toMessage(modelObjectsMap);
        msg.msgBody = toaMsg.getBytes();
        return msg;
    }
}
