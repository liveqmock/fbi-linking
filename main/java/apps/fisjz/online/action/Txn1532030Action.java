package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2030Request.TIA2030;
import apps.fisjz.domain.staring.T2030Response.TOA2030;
import apps.fisjz.domain.staring.T2030Response.TOA2030PaynotesInfo;
import apps.fisjz.domain.staring.T2030Response.TOA2030PaynotesItem;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1532030 缴款退付查询
 * zhanrui  20130924
 */

@Component
public class Txn1532030Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532030Action.class);

    @Autowired
    private PaymentService paymentService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2030Request");
        TIA2030 tia = null;
        try {
            tia = (TIA2030) dataFormat.fromMessage(new String(msg.msgBody), "TIA2030");
        } catch (Exception e) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody =  "报文解析错误.".getBytes("GBK");
            return msg;
        }

        String areacode = tia.getAreacode();
        String notescode = tia.getNotescode();
        String refundapplycode  = tia.getRefundapplycode();
        logger.info("[1532030缴款书信息查询] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 票据编号:" + notescode);

        //查找本地记录
        FsJzfPaymentInfo fsJzfPaymentInfo = paymentService.findLocalDbPaymentInfo_refund(areacode, refundapplycode, notescode);
        if (fsJzfPaymentInfo == null) {//本地未查到信息
            processThiredPartyTxn(msg, tia);
        } else {
            if ("0".equals(fsJzfPaymentInfo.getFbBookFlag())) {//本地已有初始信息，但未记账
                //TODO 优化处理：只取本地数据
                processThiredPartyTxn(msg, tia);
            } else { //重复退付
                msg.rtnCode = TxnRtnCode.TXN_PAY_REPEATED.getCode();
                msg.msgBody =  "重复退付".getBytes("GBK");
            }
        }
        return msg;
    }

    private void processThiredPartyTxn(LFixedLengthProtocol msg, TIA2030 tia) throws Exception {
        SeperatedTextDataFormat dataFormat;//与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = null;
        try {
            rtnlist = service.queryRefundNontaxPayment(
                    getApplicationidByAreaCode(tia.getAreacode()),
                    getBankCodeByAreaCode(tia.getAreacode()),
                    tia.getYear(),
                    getFinorgByAreaCode(tia.getAreacode()),
                    tia.getRefundapplycode(),
                    tia.getNotescode());
        } catch (Exception e) {
            throw new RuntimeException("与财政局连接失败.", e);
        }

        //判断财政局响应结果
        if (!getResponseResult(rtnlist)) {
            throw new RuntimeException(getResponseErrMsg(rtnlist));
        }

        //处理财政局响应报文 1：处理财政局返回的缴款书主信息
        TOA2030 toa = new TOA2030();
        TOA2030PaynotesInfo paynotesInfo = new TOA2030PaynotesInfo();
        Map responseContentMap = (Map) rtnlist.get(0);
        BeanUtils.populate(paynotesInfo, responseContentMap);
        toa.setPaynotesInfo(paynotesInfo);

        //处理财政局响应报文 2：处理财政局返回的缴款书明细子项目信息
        List<TOA2030PaynotesItem> paynotesItems = new ArrayList<TOA2030PaynotesItem>();
        List details = (List) responseContentMap.get("details");
        for (Object detail : details) {
            TOA2030PaynotesItem item = new TOA2030PaynotesItem();
            BeanUtils.populate(item, (Map) detail);
            paynotesItems.add(item);
        }

        //处理财政局响应报文 3：计算明细子项目条数
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //处理财政局响应报文 4：本地数据库处理（若表中不存在则先保存）
        paymentService.initPaymentInfoAndPaymentItem_refund(tia.getAreacode(), msg.branchID, msg.ueserID, paynotesInfo, paynotesItems);

        //组特色平台响应报文
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(), toa.getPaynotesInfo());
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2030Response");
        String result = (String) dataFormat.toMessage(modelObjectsMap);
        msg.msgBody = result.getBytes();
    }
}
