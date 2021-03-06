package apps.fis.online.action;

import apps.fis.domain.txn.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// 待补票作废信息申请 [final]
@Component
public class Txn1533051Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533051Action.class);
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");

        Tia3051 tia3051 = new Tia3051();
        tia3051.BODY.DATA.XZQH = fieldArray[0];
        tia3051.BODY.DATA.DBPYWXH = fieldArray[1];
        tia3051.BODY.DATA.SQRQ = date8ToDate10(fieldArray[2]);

        logger.info("[3051待补票作废信息申请] [网点号]" + msg.branchID + "[柜员号]" + msg.tellerID +
                "行政区划：" + fieldArray[0] + " 待补票业务序号：" + fieldArray[1] + " 日期：" + fieldArray[2]);

        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        Toa3051 toa3051 = (Toa3051) dataExchangeService.process(tia3051);
        if ("0".equals(toa3051.HEAD.STATUS)) {
            StringBuilder recordsBuilder = new StringBuilder(toa3051.BODY.dataList.size());
            recordsBuilder.append("|");
            for (Toa3051.Body.Data record : toa3051.BODY.dataList) {
                recordsBuilder.append(record.PJZL).append(",")
                        .append(record.DBPYWXH).append(",")
                        .append(record.XBPH).append("|");
            }
            msg.msgBody = recordsBuilder.toString().getBytes();
            for (Toa3051.Body.Data voucher : toa3051.BODY.dataList) {
                // 删除旧票
                fsqdfPaymentService.deletePaymentInfo(voucher.DBPYWXH);
                // 查询新票
                Tia3027 tia3027 = new Tia3027();
                tia3027.BODY.DATA.XZQH = fieldArray[0];
                tia3027.BODY.DATA.DBPYWXH = voucher.DBPYWXH;
                tia3027.BODY.DATA.SQRQ = "";

                Toa3027 toa3027 = (Toa3027) dataExchangeService.process(tia3027);
                if ("0".equals(toa3027.HEAD.STATUS)) {
                    logger.info("[待补票开票申请反馈文件]" + toa3027.BODY.DATA.BPXXWJM);
                    String fileData = dataExchangeService.getFtpfileData(toa3027.BODY.DATA.BPXXWJM);
                    logger.info("[待补票开票信息文件内容]" + fileData);
                    int rtncnt = assembleAndSavePayments(tia3027.BODY.DATA.SQRQ, tia3027.BODY.DATA.SQRQ, fileData);
//                    int rtncnt = assembleAndSavePayments(sysCtl.getTxnDate(), tia3027.BODY.DATA.SQRQ, fileData);
                    logger.info("获取到" + rtncnt + "笔补票作废后所补新票！");
                } else {
                    logger.error("[查询作废待补票后，获取新票信息异常。]" + toa3027.HEAD.ERRCODE + toa3027.HEAD.MESSAGE);
                    msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                    msg.msgBody = toa3027.HEAD.MESSAGE.getBytes();
                }
            }
        } else {
            logger.info(toa3051.HEAD.ERRCODE + toa3051.HEAD.MESSAGE);
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3051.HEAD.MESSAGE.getBytes();
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
                logger.info(line);
                String[] maininfo = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "@@@");
                String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(maininfo[0], "|||");
                FsQdfPaymentInfo info = new FsQdfPaymentInfo();
                info.setPendingDate(applyDate);
                info.setPjzl(fieldArray[0]);                       // 票据种类
                info.setJksbh(fieldArray[1]);                      // 缴款书编号
                info.setXzqh(fieldArray[2]);                       // 行政区划
                info.setZsfs(fieldArray[3]);                       // 征收方式
                info.setJkfs(fieldArray[4]);                       // 缴款方式
                info.setTzrq(fieldArray[5]);                       // 填制日期
                info.setWtzsdwbm(fieldArray[6]);                   // 委托执收单位编码
                info.setWtzsdwmc(fieldArray[7]);                   // 委托执收单位名称
                info.setWtdwzgbmbm(fieldArray[8]);                 // 委托单位主管部门编码
                info.setWtdwzgbmmc(fieldArray[9]);                 // 委托单位主管部门名称
                info.setWtzsbz(fieldArray[10]);                    // 委托征收标志
                info.setStzsdwbm(fieldArray[11]);                  // 受托执收单位编码
                info.setStzsdwmc(fieldArray[12]);                  // 受托执收单位名称
                info.setStdwzgbmbm(fieldArray[13]);
                info.setStdwzgbmmc(fieldArray[14]);
                info.setZsdwzzjg(fieldArray[15]);
                info.setFkrmc(fieldArray[16]);                     // 付款人名称
                info.setFkrkhh(fieldArray[17]);                    // 付款人开户行
                info.setFkrmc(fieldArray[18]);                     // 付款人账号
                info.setSkrmc(fieldArray[19]);                      // 收款人名称
                info.setSkrkhh(fieldArray[20]);                     // 收款人开户行
                info.setSkrzh(fieldArray[21]);                      // 收款人账号
                info.setZje(new BigDecimal(fieldArray[22]));        // 总金额
                info.setDbpywxh(fieldArray[23]);
                info.setBz(fieldArray[24]);                         // 备注
                info.setSgpbz(fieldArray[25]);                       // 手工票标志
                info.setFmyy(fieldArray[26]);                        // 罚没原因
                info.setFmly(fieldArray[27]);                        // 罚没理由
                info.setDsfy(fieldArray[28]);                        // 代收法院
                info.setBgrmc(fieldArray[29]);                       // 被告人
                info.setAy(fieldArray[30]);                          // 案由
                info.setAh(fieldArray[31]);                          // 案号
                info.setZdjh(fieldArray[32]);                        // 字第几号
                info.setBde(fieldArray[33]);                         // 标的额
                info.setByzd1(fieldArray[34]);                       // 备用地段
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
            }
            fsqdfPaymentService.insertPendingPaymentInfos(infos);
            fsqdfPaymentService.insertPaymentItems(items);
            return infos.size();
        }
    }

    private String date8ToDate10(String date) {
        if (StringUtils.isEmpty(date) || date.length() != 8) return date;
        else {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        }
    }
}
