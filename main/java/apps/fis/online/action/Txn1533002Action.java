package apps.fis.online.action;

import apps.fis.domain.txn.Tia3002;
import apps.fis.domain.txn.Toa3002;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.online.service.FsqdfSysCtlService;
import apps.fis.repository.model.FsQdfPaymentInfo;
import apps.fis.repository.model.FsQdfPaymentItem;
import apps.fis.repository.model.FsQdfSysCtl;
import common.utils.ObjectFieldsCopier;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

// �ɿ���ɿ�
@Component
public class Txn1533002Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533002Action.class);
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;
    @Autowired
    private Txn1533007Action txn1533007Action;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        String voucherType = fieldArray[0];
        String voucherNo = fieldArray[1];
        logger.info("[1533002�ɿ���ɿ�][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "[Ʊ������] " + voucherType + "  [�ɿ�����] " + voucherNo);
        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        FsQdfPaymentInfo info = fsqdfPaymentService.qryPaymentInfo(voucherType, voucherNo);
        List<FsQdfPaymentItem> items = fsqdfPaymentService.qryPaymentItems(voucherType, voucherNo);
        boolean existInDB = false;

        // �����ݿ��Ѵ��ڸýɿ(����Ʊ)�������
        if (info != null && items.size() > 0) {
            existInDB = true;
            info.setJym(fieldArray[21]);
            if (new BigDecimal(fieldArray[19]).compareTo(info.getZje()) != 0) {
                msg.rtnCode = TxnRtnCode.MSG_AMT_ERROR.getCode();
                msg.msgBody = TxnRtnCode.MSG_AMT_ERROR.getTitle().getBytes();
                return msg;
            }
            info.setJkfs(fieldArray[4]);                       // �ɿʽ
            info.setFkrzh(fieldArray[14]);                     // �������˺�
            info.setSkrmc(StringUtils.isEmpty(fieldArray[15]) ? sysCtl.getCbsActnam() : fieldArray[15]);                    // �տ�������
            info.setSkrkhh(StringUtils.isEmpty(fieldArray[16]) ? sysCtl.getBankName() : fieldArray[16]);                    // �տ��˿�����
            info.setSkrzh(StringUtils.isEmpty(fieldArray[17]) ? sysCtl.getCbsActno() : fieldArray[17]);                     // �տ����˺�
            info.setLxskbz(StringUtils.isEmpty(fieldArray[22]) ? "0" : fieldArray[22]);                    // �����տ��־
            info.setYhskrq(fieldArray[24]);
            // ����Ϊ��������
            info.setYhwdbm(msg.branchID);                      // �������
            info.setOperid(msg.tellerID);                      // ��Ա
            info.setCbsActSerial(msg.serialNo);
            if (!StringUtils.isEmpty(fieldArray[34])) {
                info.setBmkywxh(fieldArray[34]);                   // ������ҵ�����
                info.setPendingFlag("3");                          // ������ֱ�Ӳ�Ʊ
                info.setHostBookFlag("0");
            }
            if ("06".equals(fieldArray[4])) {         // POS�ɿʽ
                info.setHostBookFlag("0");             // ����������
            } else {
                info.setHostBookFlag("1");
            }
            info.setByzd1(fieldArray[35]);
            info.setByzd2(fieldArray[36]);         // pos �ն˺�
            info.setByzd3(fieldArray[37]);                 // pos��ˮ��
        } else {
            // ���沢�����ֹ�Ʊ
            info = new FsQdfPaymentInfo();
            info.setPjzl(fieldArray[0]);                       // Ʊ������
            info.setJksbh(fieldArray[1]);                      // �ɿ�����
            info.setXzqh(fieldArray[2]);                       // ��������
            info.setZsfs(fieldArray[3]);                       // ���շ�ʽ
            info.setJkfs(fieldArray[4]);                       // �ɿʽ
            info.setTzrq(fieldArray[5]);                       // ��������
            info.setWtzsdwbm(fieldArray[6]);                   // ί��ִ�յ�λ����
            info.setWtzsdwmc(fieldArray[7]);                   // ί��ִ�յ�λ����
            info.setWtzsbz(fieldArray[8]);                     // ί�����ձ�־
            info.setStzsdwbm(fieldArray[9]);                   // ����ִ�յ�λ����
            info.setStzsdwmc(fieldArray[10]);                  // ����ִ�յ�λ����
            info.setZsdwzzjg(fieldArray[11]);                  // ִ�յ�λ��֯����
            info.setFkrmc(fieldArray[12]);                     // ����������
            info.setFkrkhh(fieldArray[13]);                     // �����˿�����
            info.setFkrzh(fieldArray[14]);                     // �������˺�
            info.setSkrmc(StringUtils.isEmpty(fieldArray[15]) ? sysCtl.getCbsActnam() : fieldArray[15]);                    // �տ�������
            info.setSkrkhh(StringUtils.isEmpty(fieldArray[16]) ? sysCtl.getBankName() : fieldArray[16]);                    // �տ��˿�����
            info.setSkrzh(StringUtils.isEmpty(fieldArray[17]) ? sysCtl.getCbsActno() : fieldArray[17]);                     // �տ����˺�
//            info.setYhwdbm(fieldArray[18]);                     // ����������
            info.setZje(StringUtils.isEmpty(fieldArray[19]) ? null : new BigDecimal(fieldArray[19]));       // �ܽ��
            info.setBz(fieldArray[20]);
            info.setLxskbz(fieldArray[22]);                    // �����տ��־
            info.setSgpbz(fieldArray[23]);                     // �ֹ�Ʊ��־
            info.setJym(fieldArray[21]);                       // У����
            info.setFmyy(fieldArray[25]);                       // ��ûԭ��
            info.setFmly(fieldArray[26]);                       // ��û����
            info.setDsfy(fieldArray[27]);                       // ���շ�Ժ
            info.setBgrmc(fieldArray[28]);                       // ������
            info.setAy(fieldArray[29]);                       // ����
            info.setAh(fieldArray[30]);                       // ����
            info.setZdjh(fieldArray[31]);                       // �ֵڼ���
            info.setBde(fieldArray[32]);                       // ��Ķ�
            info.setXzspdtwym(fieldArray[33]);                 // ������������Ψһ��
            info.setBmkywxh(fieldArray[34]);                   // ������ҵ�����
            info.setByzd1(fieldArray[35]);                     // �����ֶ�1
            info.setByzd2(fieldArray[36]);                 // �����ֶ�2
            info.setByzd3(fieldArray[37]);                 // �����ֶ�3
            info.setYhwdbm(msg.branchID);                      // �������
            info.setOperid(msg.tellerID);                      // ��Ա
            info.setHostBookFlag("1");
            info.setHostChkFlag("0");
            info.setCbsActSerial(msg.serialNo);
            info.setYhskrq(fieldArray[24]);

            int itemCnt = Integer.parseInt(fieldArray[38]);
            items = new ArrayList<FsQdfPaymentItem>();
            for (int i = 1; i <= itemCnt; i++) {
                String strItem = fieldArray[38 + i];
                String[] itemFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(strItem, ",");
                FsQdfPaymentItem item = new FsQdfPaymentItem();
                item.setPjzl(voucherType);
                item.setJksbh(voucherNo);
                item.setXmsx(itemFields[0]);
                item.setSrxmbm(itemFields[1]);
                item.setSrxmmc(itemFields[2]);
                item.setSjbz(itemFields[3]);
                item.setCfjdsbh(itemFields[4]);
                item.setFzxmmc(itemFields[5]);
                item.setJnshenggk(StringUtils.isEmpty(itemFields[6]) ? null : new BigDecimal(itemFields[6]));
                item.setJnshigk(StringUtils.isEmpty(itemFields[7]) ? null : new BigDecimal(itemFields[7]));
                item.setJzmj(StringUtils.isEmpty(itemFields[8]) ? null : new BigDecimal(itemFields[8]));
                item.setJldw(itemFields[9]);
                item.setSl(StringUtils.isEmpty(itemFields[10]) ? null : new BigDecimal(itemFields[10]));
                item.setJe(StringUtils.isEmpty(itemFields[11]) ? null : new BigDecimal(itemFields[11]));
                item.setByzd1(itemFields[12]);
                item.setByzd2(itemFields[13]);
                item.setByzd3(itemFields[14]);
                // ���ʽ����ʱ���Ϊ�գ����ѯ�ñ�������Ŀ��Ϣ
                if (StringUtils.isEmpty(item.getZjxzbm())) {
                    LFixedLengthProtocol msg3007 = new LFixedLengthProtocol();
                    msg3007.txnCode = "1533007";
                    msg3007.msgBody = (info.getXzqh() + "|" + info.getWtzsdwbm() + "|" + item.getSrxmbm() + "|").getBytes();
                    LFixedLengthProtocol rtnmsg3007 = txn1533007Action.process(msg3007);
                    if ("0000".equals(rtnmsg3007.rtnCode)) {
                        String[] rtnmsg = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(rtnmsg3007.msgBody), "|");
                        item.setZjxzbm(rtnmsg[2]);
                        item.setZjxzmc(rtnmsg[3]);
                        item.setYskmbm(rtnmsg[4]);
                        item.setYskmmc(rtnmsg[5]);
                        item.setSjbz(rtnmsg[7]);
                    }
                }
                items.add(item);
            }
        }

        // ���͵�������
        Tia3002 tia3002 = new Tia3002();
        ObjectFieldsCopier.copyFields(info, tia3002.BODY.DATA);
        for (FsQdfPaymentItem item : items) {
            Tia3002.Body.Data.Xmmx xmmx = new Tia3002.Body.Data.Xmmx();
            ObjectFieldsCopier.copyFields(item, xmmx);
            tia3002.BODY.DATA.xmmxes.add(xmmx);
        }
        Toa3002 toa3002 = (Toa3002) dataExchangeService.process(tia3002);
        if ("0".equals(toa3002.HEAD.STATUS)) {
            // ���ν��׽ɿ�ɹ�
            info.setQdfBookFlag("1");
            info.setQdfChkFlag("0");
            info.setChkActDt(sysCtl.getTxnDate());             // ���˻�׼����
            if (existInDB) {
                fsqdfPaymentService.updatePaymentInfo(info);
            } else {
                fsqdfPaymentService.insertPaymentInfoAndItems(info, items);
            }
            // ���ؽ��Ļ������
            StringBuffer stringBuffer = new StringBuffer(toa3002.BODY.DATA.JKSBH).append("|");
            for (Toa3002.Body.Data.Xmmx xmmx : toa3002.BODY.DATA.xmmxes) {
                stringBuffer.append(xmmx.SRHLX).append(",")
                        .append(xmmx.SRHMC).append(",")
                        .append(xmmx.SRZHMC).append(",")
                        .append(xmmx.SRZH).append(",")
                        .append(xmmx.JE).append("|");
            }
            msg.msgBody = stringBuffer.toString().getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3002.HEAD.MESSAGE.getBytes();
        }
        return msg;
    }
}
