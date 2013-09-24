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

        //业务逻辑处理(检查处理重复数据)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(tia.getAreacode(), msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//重复退付缴款
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            msg.msgBody =  "缴款退付确认成功(重复确认)".getBytes("GBK");
            return msg;
        }

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Refund> paramList = new ArrayList<FbPaynotesInfo4Refund>();
        FbPaynotesInfo4Refund fbPaynotesInfo = new FbPaynotesInfo4Refund();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532031退付缴款确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateRefundNontaxPayment(
                FISJZ_APPLICATIONID,
                FISJZ_BANK,
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //缴款退付成功
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            msg.msgBody =  "缴款退付成功".getBytes("GBK");
        }else{ //缴款失败
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
