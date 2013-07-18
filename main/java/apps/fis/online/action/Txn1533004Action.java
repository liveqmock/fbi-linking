package apps.fis.online.action;

import apps.fis.domain.txn.Tia3004;
import apps.fis.domain.txn.Tia3025;
import apps.fis.domain.txn.Toa3004;
import apps.fis.domain.txn.Toa3025;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPendingTxnService;
import apps.fis.online.service.FsqdfSysCtlService;
import apps.fis.repository.model.FsQdfPendingTxn;
import apps.fis.repository.model.FsQdfSysCtl;
import common.utils.ObjectFieldsCopier;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 不明款信息上传
@Component
public class Txn1533004Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533004Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfPendingTxnService fsqdfPendingTxnService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        logger.info("[1533004不明款信息上传] [网点号]" + msg.branchID + "[柜员号]" + msg.tellerID +
                "报文体:" + new String(msg.msgBody));

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");

        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        Tia3004 tia3004 = new Tia3004();
        tia3004.BODY.DATA.XZQH = StringUtils.isEmpty(fieldArray[0]) ? sysCtl.getAreaCode() : fieldArray[0];
        tia3004.BODY.DATA.BMKYWXH = fieldArray[1];
//        tia3004.BODY.DATA.BMKYWXH = fsqdfSysCtlService.getBmkTxnSeq();
        tia3004.BODY.DATA.CZZHZH = StringUtils.isEmpty(fieldArray[2]) ? sysCtl.getCbsActno() : fieldArray[2];
        tia3004.BODY.DATA.JKFS = fieldArray[3];
        tia3004.BODY.DATA.YT = fieldArray[4];
        tia3004.BODY.DATA.JYRQ = fieldArray[5];
        tia3004.BODY.DATA.JE = fieldArray[6];

        Toa3004 toa3004 = (Toa3004) dataExchangeService.process(tia3004);
        if ("0".equals(toa3004.HEAD.STATUS)) {
            FsQdfPendingTxn txn = new FsQdfPendingTxn();
            ObjectFieldsCopier.copyFields(tia3004.BODY.DATA, txn);
            txn.setBookOperid(msg.tellerID);
            txn.setTxacBrid(msg.branchID);
            txn.setOperTime(msg.txnTime);
            fsqdfPendingTxnService.insert(txn);
            msg.msgBody = txn.getBmkywxh().getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3004.HEAD.MESSAGE.getBytes();
        }
        return msg;
    }

}
