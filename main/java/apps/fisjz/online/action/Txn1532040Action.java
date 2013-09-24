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
 * 1532040缴款书冲销
 * zhanrui  20130923
 */
@Component
public class Txn1532040Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532040Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2040Request");
        TIA2040 tia = (TIA2040)dataFormat.fromMessage(new String(msg.msgBody), "TIA2040");
        logger.info("[1532040缴款冲销] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 票据编号:" + tia.getPaynotesInfo().getNotescode());

        //业务逻辑处理(检查处理重复数据)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//重复缴款
            msg.rtnCode = "0000";
            msg.msgBody =  "缴款冲销成功(重复冲销)".getBytes("GBK");
            return msg;
        }

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Cancel> paramList = new ArrayList<FbPaynotesInfo4Cancel>();
        FbPaynotesInfo4Cancel fbPaynotesInfo = new FbPaynotesInfo4Cancel();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532040缴款冲销] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.cancelNontaxPayment(FISJZ_APPLICATIONID, FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //缴款成功
            msg.rtnCode = "0000";
            msg.msgBody =  "缴款冲销成功".getBytes("GBK");
        }else{ //缴款失败
            msg.rtnCode = "1000";
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
