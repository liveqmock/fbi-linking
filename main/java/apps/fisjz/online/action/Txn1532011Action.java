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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 1532011缴款书缴款
 * zhanrui  20130923
 */
@Component
public class Txn1532011Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532011Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2011Request");
        TIA2011 tia = (TIA2011)dataFormat.fromMessage(new String(msg.msgBody), "TIA2011");
        logger.info("[1532011缴款书缴款] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 票据编号:" + tia.getPaynotesInfo().getNotescode());

        //业务逻辑处理(检查处理重复数据)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//重复缴款
            msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
            msg.msgBody =  "缴款成功(重复缴款)".getBytes("GBK");
            return msg;
        }

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532011缴款书缴款] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateNontaxPayment(FISJZ_APPLICATIONID,FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //缴款成功
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            String rtnMsg = getResponseErrMsg(rtnlist);
            if (rtnMsg == null) {
                msg.msgBody =  "缴款成功".getBytes("GBK");
            }else{
                msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            }
        }else{ //缴款失败
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
