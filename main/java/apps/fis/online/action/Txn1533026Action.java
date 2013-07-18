package apps.fis.online.action;

import apps.fis.domain.txn.Tia3013;
import apps.fis.domain.txn.Tia3026;
import apps.fis.domain.txn.Toa3013;
import apps.fis.domain.txn.Toa3026;
import apps.fis.enums.PendingVchFlag;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.online.service.FsqdfPendingVchinfoService;
import apps.fis.repository.model.FsQdfPendingVchInfo;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// ��ѯ����Ʊȷ��[final]
@Component
public class Txn1533026Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533026Action.class);
    @Autowired
    private FsqdfPendingVchinfoService fsqdfPendingVchinfoService;
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // ��������
        String areaCode = fieldArray[0];
        // �ɿ�����
        String voucherNo = fieldArray[1];

        logger.info("[3026��ѯ����Ʊȷ����Ϣ][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID +
                "����������" + areaCode + " �ɿ����ţ�" + voucherNo);

        FsQdfPendingVchInfo vch = fsqdfPendingVchinfoService.qryVchInfo(areaCode, voucherNo);
        // ���Ѵ��ڣ�����ȷ����ֱ�ӷ���
        if (vch == null) {
            msg.rtnCode = "4000";
            msg.msgBody = "�ô���Ʊ��Ϣδ�ϴ�.".getBytes();
        } else {
            Tia3026 tia3026 = new Tia3026();
            tia3026.BODY.DATA.XZQH = fieldArray[0];
            tia3026.BODY.DATA.DBPYWXH = fieldArray[1];

            Toa3026 toa3026 = (Toa3026) dataExchangeService.process(tia3026);
            if ("0".equals(toa3026.HEAD.STATUS)) {
                //  0δȷ�ϣ�1ȷ����ȷ��2ȷ�ϴ���3�Ѳ�Ʊ
                vch.setQdfCfmFlag(toa3026.BODY.DATA.QRBZ);
                if(PendingVchFlag.CONFIRM_RIGHT.getCode().equals(toa3026.BODY.DATA.QRBZ)
                || PendingVchFlag.PENDED.getCode().equals(toa3026.BODY.DATA.QRBZ)) {
                    vch.setConfirmDate(msg.txnTime.substring(0, 8));
                }
                vch.setQdfCfmMsg(toa3026.BODY.DATA.QRXX);
                fsqdfPendingVchinfoService.update(vch);
                msg.msgBody = (toa3026.BODY.DATA.QRBZ + "|" + toa3026.BODY.DATA.QRXX).getBytes();
            } else {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = toa3026.HEAD.MESSAGE.getBytes();
            }
        }
        return msg;
    }

}
