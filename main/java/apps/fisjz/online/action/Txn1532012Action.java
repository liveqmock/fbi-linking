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
 * 1532012缴款书到帐确认
 * zhanrui  20130923
 */
@Component
public class Txn1532012Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532012Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2012Request");
        TIA2012 tia = (TIA2012) dataFormat.fromMessage(new String(msg.msgBody), "TIA2012");
        logger.info("[1532012缴款书到帐确认] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 缴款书编号:" + tia.getPaynotesInfo().getNotescode());

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532012缴款书到帐确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.accountNontaxPayment(
                FISJZ_APPLICATIONID,
                FISJZ_BANK,
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //到帐确认成功
            //业务逻辑处理
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
            paymentService.processPaymentPayAccount(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            msg.msgBody =  "到帐确认成功".getBytes("GBK");
        }else{ //到帐确认失败
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
