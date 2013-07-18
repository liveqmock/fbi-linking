package apps.fis.online.action;

import apps.fis.domain.txn.Tia3013;
import apps.fis.domain.txn.Toa3013;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.repository.model.FsQdfPaymentInfo;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 取消缴款
@Component
public class Txn1533013Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533013Action.class);
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 票据种类
        String voucherType = fieldArray[0];
        // 缴款书编号
        String voucherNo = fieldArray[1];
        logger.info("[1533013取消缴款][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "[票据种类] " + voucherType + "  [缴款书编号] " + voucherNo);

        FsQdfPaymentInfo info = fsqdfPaymentService.qryPaymentInfo(voucherType, voucherNo);
        // 已对账则不可退款
        if ("1".equals(info.getHostChkFlag()) || "1".equals(info.getQdfChkFlag())) {
            msg.rtnCode = TxnRtnCode.TXN_REFUND_NOT_ALLOWED.getCode();
            msg.msgBody = (TxnRtnCode.TXN_REFUND_NOT_ALLOWED.getCode()).getBytes();
            return msg;
        }

        Tia3013 tia3013 = new Tia3013();
        tia3013.BODY.DATA.SKPJZL = voucherType;
        tia3013.BODY.DATA.SKPJBH = voucherNo;

        Toa3013 toa3013 = (Toa3013) dataExchangeService.process(tia3013);
        if ("0".equals(toa3013.HEAD.STATUS)) {
            // 状态设置为已退款
            info.setHostBookFlag("2");
            info.setQdfBookFlag("2");
            info.setOperid(msg.tellerID);
            fsqdfPaymentService.updatePaymentInfo(info);
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3013.HEAD.MESSAGE.getBytes();
        }
        return msg;
    }

}
