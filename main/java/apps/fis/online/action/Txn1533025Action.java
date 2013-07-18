package apps.fis.online.action;

import apps.fis.domain.txn.Tia3013;
import apps.fis.domain.txn.Tia3025;
import apps.fis.domain.txn.Toa3013;
import apps.fis.domain.txn.Toa3025;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.*;
import apps.fis.repository.model.FsQdfPendingVchInfo;
import apps.fis.repository.model.FsQdfSysCtl;
import common.utils.ObjectFieldsCopier;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 待补票信息上传[final]
@Component
public class Txn1533025Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533025Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfPendingVchinfoService fsqdfPendingVchinfoService;
    @Autowired
    private FsqdfPendingTxnService fsqdfPendingTxnService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;
    @Autowired
    private Txn1533008Action txn1533008Action;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        logger.info("[1533025待补票信息上传] [网点号]" + msg.branchID + "[柜员号]" + msg.tellerID +
                "报文体:" + new String(msg.msgBody));

        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        Tia3025 tia3025 = new Tia3025();
        tia3025.BODY.DATA.XZQH = fieldArray[0];
        tia3025.BODY.DATA.DBPYWXH = fieldArray[1];
//        tia3025.BODY.DATA.DBPYWXH = fsqdfSysCtlService.getDbpTxnSeq();
        tia3025.BODY.DATA.JYLX = fieldArray[2];
        tia3025.BODY.DATA.CZZHZH = StringUtils.isEmpty(fieldArray[3]) ? sysCtl.getCbsActno() : fieldArray[3];
        tia3025.BODY.DATA.ZSDWBM = fieldArray[4];
        tia3025.BODY.DATA.JKFS = fieldArray[5];
        tia3025.BODY.DATA.HKRQC = fieldArray[6];
        tia3025.BODY.DATA.HKRZH = fieldArray[7];
        tia3025.BODY.DATA.HKRKHYH = fieldArray[8];
        tia3025.BODY.DATA.YT = fieldArray[9];
        tia3025.BODY.DATA.FZQRXX = fieldArray[10];
        tia3025.BODY.DATA.BMKYWXH = fieldArray[11];
        tia3025.BODY.DATA.JYRQ = fieldArray[12];
        tia3025.BODY.DATA.JE = fieldArray[13];
        /*
        添加执收单位名称，单位主管部门编码，单位主管部门名称
         */
        if (!fsqdfPendingTxnService.isExist(tia3025.BODY.DATA.BMKYWXH)) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = TxnRtnCode.TXN_BMK_NOT_EXIST.getTitle().getBytes();
            return msg;
        }

        LFixedLengthProtocol msg3008 = new LFixedLengthProtocol();
        msg3008.txnCode = "1533008";
        msg3008.msgBody = (tia3025.BODY.DATA.XZQH + "|" + tia3025.BODY.DATA.ZSDWBM + "|").getBytes();
        LFixedLengthProtocol rtnmsg3008 = txn1533008Action.process(msg3008);
        if (!"0000".equals(rtnmsg3008.rtnCode)) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = rtnmsg3008.msgBody;
            return msg;
        }
        String[] zsdwxx = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(rtnmsg3008.msgBody), "|");
        FsQdfPendingVchInfo vch = new FsQdfPendingVchInfo();
        vch.setZsdwmc(zsdwxx[1]);
        vch.setDwzgbmbm(zsdwxx[2]);
        vch.setDwzgbmmc(zsdwxx[3]);
        vch.setOperDate(msg.txnTime.substring(0, 8));
        vch.setAddDate(msg.txnTime.substring(0, 8));
        vch.setOperTime(msg.txnTime.substring(8, 14));
        Toa3025 toa3025 = (Toa3025) dataExchangeService.process(tia3025);
        if ("0".equals(toa3025.HEAD.STATUS)) {
            ObjectFieldsCopier.copyFields(tia3025.BODY.DATA, vch);
            fsqdfPendingVchinfoService.insert(vch);
            msg.msgBody = vch.getDbpywxh().getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3025.HEAD.MESSAGE.getBytes();
        }
        return msg;
    }
}
