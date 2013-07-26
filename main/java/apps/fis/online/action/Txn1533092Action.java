package apps.fis.online.action;

import apps.fis.domain.txn.Tia3007;
import apps.fis.domain.txn.Toa3007;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.*;
import apps.fis.repository.model.FsQdfChkTxn;
import apps.fis.repository.model.FsQdfPaymentInfo;
import apps.fis.repository.model.FsQdfSysCtl;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

// POS����
@Component
public class Txn1533092Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533092Action.class);
    @Autowired
    private OdsbService odsbService;
    @Autowired
    private FsqdfPaymentService paymentService;
    @Autowired
    private FsqdfChkTxnService chkTxnService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        logger.info("[1533092POS����] [�����]" + msg.branchID + "[��Ա��]" + msg.tellerID +
                "������:" + new String(msg.msgBody));
        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // ��������
        String areaCode = fieldArray[0];
        // POS��������
        String txnDate = fieldArray[1];
        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        sysCtl.setPosChkDt(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));

        List<Map<String, Object>> posTxns = odsbService.qryPosTxns(castToDate10(txnDate));
        List<FsQdfPaymentInfo> paymentInfos = paymentService.qryPosPaymentInfos(areaCode, txnDate);
        if ((posTxns == null && paymentInfos == null) ||
                (posTxns.size() == 0 && paymentInfos.size() == 0)) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
            sysCtl.setPosChkSts("0");
            fsqdfSysCtlService.update(sysCtl, "1");
            return msg;
        }
        // ����
        if (posTxns.size() != 0) {
            for (Map<String, Object> txn : posTxns) {
                FsQdfChkTxn chkTxn = new FsQdfChkTxn();
                chkTxn.setActno(String.valueOf(txn.get("posrefno")));
                chkTxn.setDcFlag("D");
                chkTxn.setMsgSn(String.valueOf(txn.get("postxsqno")));
                chkTxn.setTxnamt(new BigDecimal(String.valueOf(txn.get("tranamt"))));
                chkTxn.setTxnDate(txnDate);
                chkTxn.setSendSysId("2");
                chkTxnService.insert(chkTxn);
            }
        }
        // ����
        if (posTxns.size() == paymentInfos.size()) {
            boolean isChked = true;
            int i = 0;
            for (Map<String, Object> txn : posTxns) {
                FsQdfPaymentInfo info = paymentInfos.get(i);
                logger.info("[POS��ˮ]���ڣ�" + txnDate + "  �ն˺�" +  txn.get("posrefno") + "  �������" + txn.get("postxsqno") + "  ���" + txn.get("tranamt"));
                logger.info("[Ʊ��]��ţ�" + info.getJksbh() + "  �ն˺�" +  info.getByzd2() + "  �������" + info.getByzd3()+ "  ���" +info.getZje());
                // ��ˮ�š����
                if (!info.getByzd2().equals(txn.get("posrefno")) ||
                        !info.getByzd3().equals(txn.get("postxsqno")) ||
                        info.getZje().compareTo(new BigDecimal(String.valueOf(txn.get("tranamt")))) != 0) {
                    isChked = false;
                }
                i++;
            }
            if (!isChked) {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = "POS���˲�ƽ��".getBytes();
                sysCtl.setPosChkSts("1");
            } else {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_SECCESS.getCode();
                msg.msgBody = "POS���˳ɹ�".getBytes();
                sysCtl.setPosChkSts("0");
            }
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = "POS���˲�ƽ��".getBytes();
            sysCtl.setPosChkSts("1");
        }
        fsqdfSysCtlService.update(sysCtl, "1");
        return msg;
    }

    private String castToDate10(String srcDate) {
        if (srcDate.length() == 8) {
            return srcDate.substring(0, 4) + "-" + srcDate.substring(4, 6) + "-" + srcDate.substring(6, 8);
        } else {
            return srcDate;
        }
    }

}
