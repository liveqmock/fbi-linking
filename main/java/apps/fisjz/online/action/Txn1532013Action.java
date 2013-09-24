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
 * 1532013 手工缴款书缴款
 * zhanrui  20130923
 */
@Component
public class Txn1532013Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532013Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2013Request");
        TIA2013 tia = (TIA2013) dataFormat.fromMessage(new String(msg.msgBody), "TIA2013");
        logger.info("[1532013手工缴款书缴款] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 票据编号:" + tia.getPaynotesInfo().getNotescode());

        //业务逻辑处理(检查处理重复数据)
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
        BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        int rtn = paymentService.processPaymentPay(msg.branchID, msg.tellerID, fsJzfPaymentInfo);
        if (rtn == 1) {//重复缴款
            msg.rtnCode = "0000";
            msg.msgBody =  "缴款成功(重复缴款)".getBytes("GBK");
            return msg;
        }

        //与财政局通讯
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

        logger.info("[1532013手工缴款书缴款] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.insertNontaxPayment(FISJZ_APPLICATIONID, FISJZ_BANK, tia.getYear(), tia.getFinorg(), paramList);

        //判断财政局响应结果
        if (getResponseResult(rtnlist)) { //缴款成功
            msg.rtnCode = "0000";
            msg.msgBody =  "手工缴款成功".getBytes("GBK");
        }else{ //缴款失败
            msg.rtnCode = "1002";
            msg.msgBody =  getResponseErrMsg(rtnlist).getBytes("GBK");
            return msg;
        }
        return msg;
    }
}
