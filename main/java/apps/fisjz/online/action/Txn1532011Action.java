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
 * 1532011缴款书缴款
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
        // 解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2011Request");
        TIA2011 tia = null;
        try {
            tia = (TIA2011) dataFormat.fromMessage(new String(msg.msgBody), "TIA2011");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "报文解析错误.".getBytes("GBK");
            return msg;
        }
        logger.info("[1532011缴款书缴款] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 票据编号:" + tia.getPaynotesInfo().getNotescode());

        //业务逻辑处理(对于特色平台自动冲正的交易做归档处理)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(tia.getAreacode(), msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == -1) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "请先查询缴款单信息.".getBytes("GBK");
            return msg;
        }

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532011缴款书缴款] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateNontaxPayment(getApplicationidByAreaCode(tia.getAreacode()),
                getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //缴款成功
            //检查明细
            Map responseContentMap = (Map) rtnlist.get(0);
            FbPaynotesInfo respInfo = new FbPaynotesInfo();
            BeanUtils.populate(respInfo, responseContentMap);
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
//                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
//                msg.msgBody = "缴款交易失败!明细核对不符!".getBytes("GBK");
//                return msg;
                throw new RuntimeException("缴款交易失败!明细核对不符!");
            }

            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            String rtnMsg = getResponseErrMsg(rtnlist);
            if (StringUtils.isEmpty(rtnMsg)) {
                msg.msgBody = "缴款成功".getBytes("GBK");
            } else {
                msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
            }
            //自动发起缴款确认交易
            rtnlist = processPayConfirmTxn(msg, tia);
            if (!getResponseResult(rtnlist)) {
                rtnMsg = getResponseErrMsg(rtnlist);
                throw new RuntimeException("缴款到账确认交易失败!" + rtnMsg);
            }
        } else { //缴款失败
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }

    //发起缴款确认交易
    private List processPayConfirmTxn(final LFixedLengthProtocol msg, final TIA2011 tia) throws Exception {
        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        logger.info("[1532012缴款书到帐确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist =  service.accountNontaxPayment(
                getApplicationidByAreaCode(tia.getAreacode()),
                getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //到帐确认成功
            //业务逻辑处理
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
            paymentService.processPaymentPayAccount(tia.getAreacode(),msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        }
        return rtnlist;
    }
}
