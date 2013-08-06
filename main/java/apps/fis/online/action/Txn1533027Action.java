package apps.fis.online.action;

import apps.fis.domain.txn.Tia3027;
import apps.fis.domain.txn.Toa3027;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.online.service.FsqdfSysCtlService;
import apps.fis.repository.model.FsQdfPaymentInfo;
import apps.fis.repository.model.FsQdfPaymentItem;
import apps.fis.repository.model.FsQdfSysCtl;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// ����Ʊ��Ʊ��Ϣ����[final]
@Component
public class Txn1533027Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533027Action.class);
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");

        logger.info("[3027����Ʊ��Ʊ��Ϣ��ѯ] [�����]" + msg.branchID + "[��Ա��]" + msg.tellerID +
                "����������" + fieldArray[0] + " ����Ʊҵ����ţ�" + fieldArray[1] + " ���ڣ�" + fieldArray[2]);

        Tia3027 tia3027 = new Tia3027();
        tia3027.BODY.DATA.XZQH = fieldArray[0];
        tia3027.BODY.DATA.DBPYWXH = fieldArray[1];
        tia3027.BODY.DATA.SQRQ = fieldArray[2];

        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        Toa3027 toa3027 = (Toa3027) dataExchangeService.process(tia3027);
        if ("0".equals(toa3027.HEAD.STATUS)) {
            logger.info("[����Ʊ��Ʊ���뷴���ļ�]" + toa3027.BODY.DATA.BPXXWJM);
            String fileData = dataExchangeService.getFtpfileData(toa3027.BODY.DATA.BPXXWJM);
            logger.info("[����Ʊ��Ʊ��Ϣ�ļ�����]" + fileData);
            int cnt = assembleAndSavePayments(tia3027.BODY.DATA.SQRQ, tia3027.BODY.DATA.SQRQ, fileData);
//            int cnt = assembleAndSavePayments(sysCtl.getTxnDate(), tia3027.BODY.DATA.SQRQ, fileData);
            // ��Ӧ
            fileData.substring(0, fileData.length() - 1);
            fileData.replace("|||", ",");
            fileData.replace("###", "|");
            String rtninfos = String.valueOf(cnt) + "|" + fileData.substring(12);
            msg.msgBody = rtninfos.getBytes();
        } else if ("EGOV-0003".equals(toa3027.HEAD.ERRCODE)) {
            msg.msgBody = toa3027.HEAD.MESSAGE.getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3027.HEAD.MESSAGE.getBytes();
        }
        return msg;
    }

    private int assembleAndSavePayments(String chkActDate, String applyDate, String fileData) {
        if (StringUtils.isEmpty(fileData)) {
            return 0;
        } else {
            fileData = fileData.substring(12);
            String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(fileData, "###");
            List<FsQdfPaymentInfo> infos = new ArrayList<FsQdfPaymentInfo>();
            List<FsQdfPaymentItem> items = new ArrayList<FsQdfPaymentItem>();
            for (String line : lines) {
                String[] maininfo = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "@@@");
                String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(maininfo[0], "|||");
                FsQdfPaymentInfo info = new FsQdfPaymentInfo();
                info.setPendingDate(applyDate);
                info.setPjzl(fieldArray[0]);                       // Ʊ������
                info.setJksbh(fieldArray[1]);                      // �ɿ�����
                info.setXzqh(fieldArray[2]);                       // ��������
                info.setZsfs(fieldArray[3]);                       // ���շ�ʽ
                info.setJkfs(fieldArray[4]);                       // �ɿʽ
                info.setTzrq(fieldArray[5]);                       // ��������
                info.setWtzsdwbm(fieldArray[6]);                   // ί��ִ�յ�λ����
                info.setWtzsdwmc(fieldArray[7]);                   // ί��ִ�յ�λ����
                info.setWtdwzgbmbm(fieldArray[8]);                 // ί�е�λ���ܲ��ű���
                info.setWtdwzgbmmc(fieldArray[9]);                 // ί�е�λ���ܲ�������
                info.setWtzsbz(fieldArray[10]);                    // ί�����ձ�־
                info.setStzsdwbm(fieldArray[11]);                  // ����ִ�յ�λ����
                info.setStzsdwmc(fieldArray[12]);                  // ����ִ�յ�λ����
                info.setStdwzgbmbm(fieldArray[13]);
                info.setStdwzgbmmc(fieldArray[14]);
                info.setZsdwzzjg(fieldArray[15]);
                info.setFkrmc(fieldArray[16]);                     // ����������
                info.setFkrkhh(fieldArray[17]);                    // �����˿�����
                info.setFkrmc(fieldArray[18]);                     // �������˺�
                info.setSkrmc(fieldArray[19]);                      // �տ�������
                info.setSkrkhh(fieldArray[20]);                     // �տ��˿�����
                info.setSkrzh(fieldArray[21]);                      // �տ����˺�
                info.setZje(new BigDecimal(fieldArray[22]));        // �ܽ��
                info.setDbpywxh(fieldArray[23]);
                info.setBz(fieldArray[24]);                         // ��ע
                info.setSgpbz(fieldArray[25]);                       // �ֹ�Ʊ��־
                info.setFmyy(fieldArray[26]);                        // ��ûԭ��
                info.setFmly(fieldArray[27]);                        // ��û����
                info.setDsfy(fieldArray[28]);                        // ���շ�Ժ
                info.setBgrmc(fieldArray[29]);                       // ������
                info.setAy(fieldArray[30]);                          // ����
                info.setAh(fieldArray[31]);                          // ����
                info.setZdjh(fieldArray[32]);                        // �ֵڼ���
                info.setBde(fieldArray[33]);                         // ��Ķ�
                info.setByzd1(fieldArray[34]);                       // ���õض�
                info.setByzd2(fieldArray[35]);
                info.setByzd3(fieldArray[36]);
                info.setChkActDt(chkActDate);
                infos.add(info);

                String[] details = StringUtils.splitByWholeSeparatorPreserveAllTokens(maininfo[1], "$$$");
                for (String detail : details) {
                    String[] detailField = StringUtils.splitByWholeSeparatorPreserveAllTokens(detail, "|||");
                    FsQdfPaymentItem item = new FsQdfPaymentItem();
                    item.setPjzl(info.getPjzl());
                    item.setJksbh(info.getJksbh());
                    item.setXmsx(detailField[0]);
                    item.setSrxmbm(detailField[1]);
                    item.setSrxmmc(detailField[2]);
                    item.setZjxzbm(detailField[3]);
                    item.setZjxzmc(detailField[4]);
                    item.setYskmbm(detailField[5]);
                    item.setYskmmc(detailField[6]);
                    item.setSjbz(detailField[7]);
                    item.setJldw(detailField[8]);
                    item.setCfjdsbh(detailField[9]);
                    item.setFzxmmc(detailField[10]);
                    item.setJnshenggk(new BigDecimal(detailField[11]));
                    item.setJnshigk(new BigDecimal(detailField[12]));
                    item.setJzmj(new BigDecimal(detailField[13]));
                    item.setJldw(detailField[14]);
                    item.setSl(new BigDecimal(detailField[15]));
                    item.setJe(new BigDecimal(detailField[16]));
                    item.setByzd1(detailField[17]);
                    item.setByzd2(detailField[18]);
                    item.setByzd3(detailField[19]);
                    items.add(item);
                }
                fsqdfPaymentService.insertPendingPaymentInfos(infos);
                fsqdfPaymentService.insertPaymentItems(items);
            }
            return infos.size();
        }
    }
}
