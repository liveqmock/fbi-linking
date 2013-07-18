package apps.fis.online.action;

import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.CommonService;
import apps.fis.online.service.DataExchangeService;
import apps.fis.domain.txn.Tia3001;
import apps.fis.domain.txn.Toa3001;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.repository.model.FsQdfPaymentInfo;
import apps.fis.repository.model.FsQdfPaymentItem;
import common.utils.ObjectFieldsCopier;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//	Ӧ����Ϣ����
@Component
public class Txn1533001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533001Action.class);
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // Ʊ������
        String voucherType = fieldArray[0];
        // �ɿ�����
        String voucherNo = fieldArray[1];

        logger.info("[1533001�ɿ�����Ϣ��ѯ][�����]" + msg.branchID + "[��Ա��]" + msg.tellerID
                + "[Ʊ������] " + voucherType + "  [�ɿ�����] " + voucherNo);
        // ��ѯ�������ݿ�Ľɿ
        FsQdfPaymentInfo info = fsqdfPaymentService.qryPaymentInfo(voucherType, voucherNo);
        List<FsQdfPaymentItem> items = fsqdfPaymentService.qryPaymentItems(voucherType, voucherNo);

        if (info != null && items.size() > 0) {
            String rtnMsg = assembleStr(info, items);
            msg.msgBody = rtnMsg.getBytes();
            if ("1".equals(info.getHostBookFlag()) || "1".equals(info.getQdfBookFlag())) {
                msg.rtnCode = "0001";
            }
        } else {
            // �в��� 3001 ����
            Tia3001 tia3001 = new Tia3001();
            tia3001.BODY.DATA.PJZL = voucherType;
            tia3001.BODY.DATA.JKSBH = voucherNo;
            // ���������ĵ������֡���ȡ��Ӧ
            Toa3001 toa3001 = (Toa3001) dataExchangeService.process(tia3001);
            // ��ѯ���ɿ��Ϣ
            if ("0".equals(toa3001.HEAD.STATUS)) {
                // ����
                info = new FsQdfPaymentInfo();
                ObjectFieldsCopier.copyFields(toa3001.BODY.DATA, info);
                info.setYhwdbm(msg.branchID);
                info.setOperid(msg.tellerID);
                // �������˱�־ 0δ���� 1�Ѽ���
                info.setHostBookFlag("0");
                // �������˱�־ 0δ���� 1�Ѷ���
                info.setHostChkFlag("0");
                // �в������˱�־ 0δ���� 1�Ѽ���
                info.setQdfBookFlag("0");
                // �в������˱�־ 0δ���� 1�Ѷ���
                info.setQdfChkFlag("0");
                items = new ArrayList<FsQdfPaymentItem>();
                for (Toa3001.Body.Data.Xmmx xmmx : toa3001.BODY.DATA.xmmxes) {
                    FsQdfPaymentItem item = new FsQdfPaymentItem();
                    ObjectFieldsCopier.copyFields(xmmx, item);
                    item.setPjzl(toa3001.BODY.DATA.PJZL);
                    item.setJksbh(toa3001.BODY.DATA.JKSBH);
                    items.add(item);
                }
                fsqdfPaymentService.insertPaymentInfoAndItems(info, items);
                String rtnMsg = assembleStr(info, items);
                msg.msgBody = rtnMsg.getBytes();
            } else if ("2".equals(toa3001.HEAD.STATUS)) {
                msg.msgBody = ("|||||||||||||||||||||||||||||||||||||00|2|0|" + toa3001.HEAD.MESSAGE + "|").getBytes();
            } else {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = toa3001.HEAD.MESSAGE.getBytes();
            }
        }
        return msg;
    }

    private String assembleStr(FsQdfPaymentInfo info, List<FsQdfPaymentItem> items) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(info.getPjzl()).append("|")                            // Ʊ������
                .append(info.getJksbh()).append("|")                             // �ɿ�����
                .append(nullToEmpty(info.getXzqh())).append("|")                 // ��������
                .append(nullToEmpty(info.getZsfs())).append("|")                 // ���շ�ʽ
                .append(nullToEmpty(info.getJkfs())).append("|")                 // �ɿʽ
                .append(castToDate8(info.getTzrq())).append("|")                 // ��������
                .append(nullToEmpty(info.getWtzsdwbm())).append("|")             // ί��ִ�յ�λ����
                .append(nullToEmpty(info.getWtzsdwmc())).append("|")             // ί��ִ�յ�λ����
                .append(nullToEmpty(info.getWtdwzgbmbm())).append("|")           // ί�����ܲ��ŵ�λ����
                .append(nullToEmpty(info.getWtdwzgbmmc())).append("|")           // ί�����ܲ��ŵ�λ����
                .append(nullToEmpty(info.getWtzsbz())).append("|")                // ί�����ձ�־
                .append(nullToEmpty(info.getStzsdwbm())).append("|")              // ����ִ�յ�λ����
                .append(nullToEmpty(info.getStzsdwmc())).append("|")              // ����ִ�յ�λ����
                .append(nullToEmpty(info.getStdwzgbmbm())).append("|")            // ���е�λ���ܲ��ű���
                .append(nullToEmpty(info.getStdwzgbmmc())).append("|")            // ���е�λ���ܲ�������
                .append(nullToEmpty(info.getZsdwzzjg())).append("|")              // ִ�յ�λ��֯�ṹ
                .append(nullToEmpty(info.getFkrmc())).append("|")                 // ����������
                .append(nullToEmpty(info.getFkrkhh())).append("|")               // �����˿�����
                .append(nullToEmpty(info.getFkrzh())).append("|")                // �������˺�
                .append(nullToEmpty(info.getSkrmc())).append("|")                // �տ�������
                .append(nullToEmpty(info.getSkrkhh())).append("|")               // �տ��˿�����
                .append(nullToEmpty(info.getSkrzh())).append("|")                // �տ����˺�
                .append(info.getZje().toString()).append("|")                    // �ܽ��
                .append(nullToEmpty(info.getBz())).append("|")                    // ��ע
//                .append(nullToEmpty(info.getLxskbz())).append("|")               // �����տ��־
                .append(nullToEmpty(info.getSgpbz())).append("|")                // �ֹ�Ʊ��־
                .append(nullToEmpty(info.getFmyy())).append("|")                // ��ûԭ��
                .append(nullToEmpty(info.getFmly())).append("|")                // ��û����
                .append(nullToEmpty(info.getDsfy())).append("|")                // ���շ�Ժ
                .append(nullToEmpty(info.getBgrmc())).append("|")               // ������
                .append(nullToEmpty(info.getAy())).append("|")                 // ����
                .append(nullToEmpty(info.getAh())).append("|")                 // ����
                .append(nullToEmpty(info.getZdjh())).append("|")               // �ֵڼ���
                .append(nullToEmpty(info.getBde())).append("|")                // ��Ķ�
                .append(nullToEmpty(info.getXzspdtwym())).append("|")          // ������������Ψһ��
                .append(nullToEmpty(info.getByzd1())).append("|")          // �����ֶ�1
                .append(nullToEmpty(info.getByzd2())).append("|")          // �����ֶ�2
                .append(nullToEmpty(info.getByzd3())).append("|");          // �����ֶ�3
        if ("1".equals(info.getHostBookFlag()) || "1".equals(info.getQdfBookFlag())) {
            strBuilder.append("10").append("|");
        } else {
            strBuilder.append("00").append("|");                     // �ɿ��־
        }
        strBuilder.append("0").append("|")                           // status
                .append(String.valueOf(items.size())).append("|");   // ��¼��
        // ��ϸ
        for (FsQdfPaymentItem item : items) {
            strBuilder.append(nullToEmpty(item.getXmsx())).append(",")
                    .append(nullToEmpty(item.getSrxmbm())).append(",")
                    .append(nullToEmpty(item.getSrxmmc())).append(",")
                    .append(nullToEmpty(item.getZjxzbm())).append(",")
                    .append(nullToEmpty(item.getZjxzmc())).append(",")
                    .append(nullToEmpty(item.getYskmbm())).append(",")
                    .append(nullToEmpty(item.getYskmmc())).append(",")
                    .append(nullToEmpty(item.getSjbz())).append(",")
                    .append(nullToEmpty(item.getCfjdsbh())).append(",")
                    .append(nullToEmpty(item.getFzxmmc())).append(",")
                    .append(item.getJnshenggk() == null ? "" : String.valueOf(item.getJnshenggk())).append(",")
                    .append(item.getJnshigk() == null ? "" : String.valueOf(item.getJnshigk())).append(",")
                    .append(item.getJzmj() == null ? "" : String.valueOf(item.getJzmj())).append(",")
                    .append(nullToEmpty(item.getJldw())).append(",")
                    .append(item.getSl() == null ? "" : String.valueOf(item.getSl())).append(",")
                    .append(item.getJe() == null ? "" : String.valueOf(item.getJe())).append(",")
                    .append(nullToEmpty(item.getByzd1())).append(",")
                    .append(nullToEmpty(item.getByzd2())).append(",")
                    .append(nullToEmpty(item.getByzd3())).append("|");
        }
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private String castToDate8(String srcDate) {
        if (srcDate == null) {
            return "        ";
        } else if (srcDate.length() > 8) {
            return srcDate.substring(0, 4) + srcDate.substring(5, 7) + srcDate.substring(8, 10);
        } else {
            return srcDate;
        }
    }
}
