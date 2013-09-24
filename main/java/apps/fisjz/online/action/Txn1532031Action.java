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
 * 1532031 缴款退付确认
 * zhanrui  20130924
 */

@Component
public class Txn1532031Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532031Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Request");
        TIA2031 tia = (TIA2031)dataFormat.fromMessage(new String(msg.msgBody), "TIA2031");
        logger.info("[1532031退付缴款确认] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 退付缴款书编号:" + tia.getPaynotesInfo().getRefundapplycode());

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532031退付缴款确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateNontaxPayment(FISJZ_APPLICATIONID,FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //判断财政局响应结果
        if (!getResponseResult(rtnlist)) {
            throw  new RuntimeException(getResponseErrMsg(rtnlist));
        }

        //业务逻辑处理
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        paymentService.processRefundPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);

        //组特色平台响应报文
        TOA2031 toa = new TOA2031();
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2031Response");
        String toaMsg = (String)dataFormat.toMessage(modelObjectsMap);
        msg.msgBody = toaMsg.getBytes();
        return msg;
    }
}
