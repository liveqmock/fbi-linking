package apps.fis.online.action;

import apps.fis.SystemParameter;
import apps.fis.domain.txn.*;
import apps.fis.enums.PendingVchFlag;
import apps.fis.enums.TxnRtnCode;
import apps.fis.ftp.FtpClient;
import apps.fis.online.service.*;
import apps.fis.repository.model.*;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// 对账
@Component
public class Txn1533003Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533003Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private FsqdfSysCtlService fsqdfSysCtlService;
    @Autowired
    private Txn1533027Action txn1533027Action;
    @Autowired
    private Txn1533026Action txn1533026Action;
    @Autowired
    private FsqdfChkVchService fsqdfChkVchService;
    @Autowired
    private FsqdfPendingVchinfoService pendingVchinfoService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private FsqdfChkTxnService chkTxnService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        /* 行政区划|对账日期|对公账户|账号当日余额|4明细数|5交易流水号,交易金额,记账方向()*/
        String areaCode = fieldArray[0];
        String chkActDate = fieldArray[1];

        logger.info("[1533003发起对账] [网点号]" + msg.branchID + "[柜员号]" + msg.tellerID +
                "行政区划:" + areaCode + " 对账日期: " + chkActDate);
        // 与主机对账
        if ("FIS153".equalsIgnoreCase(msg.ueserID)) {
            // logger.info("[特色平台]对帐报文体：" + new String(msg.msgBody));
            String cbsActno = fieldArray[2];
            BigDecimal actbal = null;
            if (!StringUtils.isEmpty(fieldArray[3])) {
                actbal = new BigDecimal(fieldArray[3]);
            }

            logger.info("[与主机对账]笔数：" + fieldArray[4]);
            int cbsCnt = Integer.parseInt(fieldArray[4]);
            BigDecimal totalCbsAmt = new BigDecimal("0.00");
            for (int i = 1; i <= cbsCnt; i++) {
                String detailsStr = fieldArray[4 + i];
                String[] details = StringUtils.splitByWholeSeparatorPreserveAllTokens(detailsStr, ",");
                totalCbsAmt = totalCbsAmt.add(new BigDecimal(details[1].trim()));
                FsQdfChkTxn cbsTxn = new FsQdfChkTxn();
                cbsTxn.setActno(cbsActno);
                cbsTxn.setActbal(actbal);
                cbsTxn.setDcFlag(details[2]);
                cbsTxn.setMsgSn(details[0]);
                cbsTxn.setTxnamt(new BigDecimal(details[1].trim()));
                cbsTxn.setTxnDate(msg.txnTime.substring(0, 8));
                cbsTxn.setSendSysId("0");
                chkTxnService.insert(cbsTxn);
            }
            BigDecimal totalVchAmt = commonService.qryTotalAmtByDate(chkActDate);
            if (totalVchAmt == null) totalVchAmt = new BigDecimal("0.00");
            int fsCnt = commonService.qryCbsTxnCnt(chkActDate);
            // TODO 主机对账 暂时只核对总额和笔数
            logger.info("[主机对账]主机总额:" + totalCbsAmt + "  笔数：" + cbsCnt);
            logger.info("[主机对账]Linking总额:" + totalVchAmt + "  笔数：" + fsCnt);
            if (cbsCnt != fsCnt || totalCbsAmt.compareTo(totalVchAmt) != 0) {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = (TxnRtnCode.TXN_CHKACT_NOT_SUC.getTitle() + "[企业" + fsCnt + "笔,总额" + totalVchAmt + "]").getBytes();
                return msg;
            }
            // Linking总数明细
        }

        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        if (sysCtl.getTxnDate().compareTo(chkActDate) > 0) {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = TxnRtnCode.TXN_RECHKACT_NOT_ALLOWED.getTitle().getBytes();
            return msg;
        }
        // 查询待补票确认状态,报表用
        List<FsQdfPendingVchInfo> pendingVchInfos = pendingVchinfoService.qryPendingVchInfos(PendingVchFlag.NOT_CONFIRM);
        for (FsQdfPendingVchInfo pendingVch : pendingVchInfos) {
            LFixedLengthProtocol msg3026 = new LFixedLengthProtocol();
            msg3026.txnCode = "1533026";
            msg3026.msgBody = (areaCode + "|" + pendingVch.getDbpywxh() + "|").getBytes();
            LFixedLengthProtocol rtnmsg3026 = txn1533026Action.process(msg3026);
            logger.info("[待补票号]" + pendingVch.getDbpywxh() + " [状态]" + rtnmsg3026.msgBody);
        }
        // 查询待补票开票信息，用于对账
        LFixedLengthProtocol msg3027 = new LFixedLengthProtocol();
        msg3027.txnCode = "1533027";
        msg3027.msgBody = (areaCode + "||" + chkActDate + "|").getBytes();
        LFixedLengthProtocol rtnmsg3027 = txn1533027Action.process(msg3027);
        if (!"0000".equals(rtnmsg3027.rtnCode)) {
            throw new RuntimeException("9000|" + new String(rtnmsg3027.msgBody));
        }
        List<FsQdfPaymentInfo> infos = fsqdfPaymentService.qryPaymentInfos(areaCode, chkActDate);
        List<FsQdfChkVch> chkVches = new ArrayList<FsQdfChkVch>();
        // 交易数据（变长，字段分割符“|||”，行分割符“###”），整个数据包以‘ \0’字符为结束符。
        StringBuffer strBuffer = new StringBuffer();
        for (FsQdfPaymentInfo info : infos) {
            // 1	票据种类 2	缴款书编号 3	执收单位编码 4	收入项目编码 5	收入数量 6	收入金额
            // 7	备用字段1 8	备用字段2 9	备用字段3 10	备用字段4 11	备用字段5 12	备用字段6
            List<FsQdfPaymentItem> items = fsqdfPaymentService.qryPaymentItems(info.getPjzl(), info.getJksbh());
            for (FsQdfPaymentItem item : items) {
                FsQdfChkVch chkVch = new FsQdfChkVch();
                chkVch.setChkDate(chkActDate);
                chkVch.setDataSysId("0");
                strBuffer.append(info.getPjzl()).append("|||");
                chkVch.setPjzl(info.getPjzl());
                strBuffer.append(info.getJksbh()).append("|||");
                chkVch.setJksbh(info.getJksbh());
                strBuffer.append(info.getWtzsdwbm()).append("|||");
                chkVch.setWtzsdwbm(info.getWtzsdwbm());
                if ("1".equals(info.getSgpbz()) && item.getSrxmbm().length() > 6) {
                    strBuffer.append(item.getSrxmbm().substring(0, item.getSrxmbm().length() - 2)).append("|||");
                    chkVch.setSrxmbm(item.getSrxmbm().substring(0, item.getSrxmbm().length() - 2));
                } else {
                    strBuffer.append(item.getSrxmbm()).append("|||");
                    chkVch.setSrxmbm(item.getSrxmbm());
                }
                chkVch.setJkfs(info.getJkfs());
                chkVch.setYhskrq(info.getYhskrq());
                strBuffer.append(item.getSl() == null ? "" : String.valueOf(item.getSl())).append("|||");
                chkVch.setSl(item.getSl());
                strBuffer.append(String.format("%.2f", item.getJe())).append("|||");
                chkVch.setJe(item.getJe());
                strBuffer.append("|||");
                strBuffer.append("|||");
                strBuffer.append("|||");
                strBuffer.append("|||");
                strBuffer.append("|||");
                strBuffer.append("###");
                chkVches.add(chkVch);
            }
        }
        if (strBuffer.length() > 1) {
            strBuffer.deleteCharAt(strBuffer.length() - 1);
        }
        strBuffer.append("\0");
        // 功能号（4位定长数字）＋ 包长（8位定长数字，包括结束符）+行政区划+对账日期（对哪天的收款日期YYYYMMDD）
        String txnData = strBuffer.toString();
        StringBuilder fileDataBuilder = new StringBuilder("3003");
        fileDataBuilder.append(StringUtils.leftPad(String.valueOf(txnData.getBytes().length), 8, "0"));
        fileDataBuilder.append(areaCode);       // 行政区划
        fileDataBuilder.append(chkActDate);     // 对账日期
        fileDataBuilder.append(txnData);        // 交易数据
        String chkFileData = fileDataBuilder.toString();
        // 功能号+银行类别+行政区划+日期YYYYMMDD +时间HHMMSS
        String hhmmss = new SimpleDateFormat("HHmmss").format(new Date());
        String fileName = "3003" + sysCtl.getBankId() + areaCode + chkActDate + hhmmss;
        logger.info("[" + chkActDate + "对账文件]" + fileName);
        if (uploadLocalFileData(SystemParameter.LOCAL_FTP_FILE_PATH, fileName, chkFileData)) {
            Tia3003 tia3003 = new Tia3003();
            tia3003.BODY.DATA.DZWJM = fileName;
            Toa3003 toa3003 = (Toa3003) dataExchangeService.process(tia3003);
            if ("0".equals(toa3003.HEAD.STATUS)) {
                sysCtl.setWsysChkSts("0");
                // 更新为已对账
                for (FsQdfChkVch vch : chkVches) {
                    vch.setChkSts("1");
                    fsqdfChkVchService.insert(vch);
                }
                fsqdfPaymentService.updatePaymentInfosToChkact(infos, msg.txnTime.substring(0, 8));
            } else {
                sysCtl.setWsysChkSts("1");
                // 更新为已对账 (无论对账结果是否平账)，可重复对账
                fsqdfPaymentService.updatePaymentInfosToChkact(infos, msg.txnTime.substring(0, 8));
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = toa3003.HEAD.MESSAGE.getBytes();
                try {
                    // 查看对账失败文件内容
                    Tia3048 tia3048 = new Tia3048();
                    tia3048.BODY.DATA.DZWJM = fileName;
                    Toa3048 toa3048 = (Toa3048) dataExchangeService.process(tia3048);
                    if (!"0".equals(toa3048.HEAD.STATUS)) {
                        throw new RuntimeException(toa3048.HEAD.ERRCODE + toa3048.HEAD.MESSAGE);
                    }
                    String chkFailFileName = toa3048.BODY.DATA.DZSBWJM;

                    String chkFailFileData = dataExchangeService.getFtpfileData(chkFailFileName);
                    logger.info("对帐失败文件：" + chkFailFileName);
                    logger.info("对帐失败文件：" + chkFailFileData);
                    List<FsQdfChkVch> failVches = assembleChkVchs(chkActDate, chkFailFileData);
                    for (FsQdfChkVch vch : chkVches) {
                        boolean isFailed = false;
                        for (FsQdfChkVch failVch : failVches) {
                            if (failVch.getPjzl().equals(vch.getPjzl()) && failVch.getJksbh().equals(vch.getJksbh())) {
                                isFailed = true;
                                break;
                            }
                        }
                        // 0-失败 1-成功
                        if (isFailed) vch.setChkSts("0");
                        else vch.setChkSts("1");
                        fsqdfChkVchService.insert(vch);
                    }
                } catch (Exception e) {
                    logger.error("查看对账失败文件内容异常。", e);
                }
            }
            String chkSysDate = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
            if (chkActDate.equals(sysCtl.getTxnDate())) {
                Date txnDate = new SimpleDateFormat("yyyyMMdd").parse(sysCtl.getTxnDate());
                String nextDate = getDateAfter(txnDate, 1, "yyyyMMdd");
                sysCtl.setTxnDate(nextDate);
            }
            sysCtl.setHostChkDt(chkSysDate);
            sysCtl.setWsysChkDt(chkSysDate);
            fsqdfSysCtlService.update(sysCtl, "1");
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = TxnRtnCode.TXN_CHKFILE_NOT_UPLOADED.getTitle().getBytes();
        }

        return msg;
    }

    public boolean uploadLocalFileData(String localPath, String fileName, String fileData) throws IOException {

        logger.info(fileData);
        File file = new File(localPath, fileName);
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(fileData.getBytes());
        fos.flush();
        fos.close();
        return dataExchangeService.uploadFtpfileData(localPath, fileName);
    }

    private List<FsQdfChkVch> assembleChkVchs(String date, String fileData) {
        if (StringUtils.isEmpty(fileData)) {
            return null;
        } else {
            fileData = fileData.substring(14);
            String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(fileData, "###");
            List<FsQdfChkVch> vches = new ArrayList<FsQdfChkVch>();
            for (String line : lines) {
                String[] fields = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "|||");
                FsQdfChkVch vch = new FsQdfChkVch();
                vch.setChkDate(date);
                vch.setChkSts("0");
                vch.setPjzl(fields[0]);
                vch.setJksbh(fields[1]);
                vch.setWtzsdwbm(fields[2]);
                vch.setSrxmbm(fields[3]);
                vch.setSl(new BigDecimal(fields[4]));
                vch.setJe(new BigDecimal(fields[5]));
                vch.setYhskrq(fields[6]);
                vch.setJkfs(fields[7]);
                vch.setDzsbyy(fields[8]);
                vch.setByzd1(fields[9]);
                vch.setByzd2(fields[10]);
                vch.setByzd3(fields[11]);
                vch.setByzd4(fields[12]);
                vch.setByzd5(fields[13]);
                vch.setByzd6(fields[14]);
                vch.setChkSts("0");
                vch.setDataSysId("1");
                vches.add(vch);
                fsqdfChkVchService.insert(vch);
            }
            return vches;
        }
    }

    private String getDateAfter(Date date, int days, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return sdf.format(calendar.getTime());
    }

}
