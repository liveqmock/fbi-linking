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

// 缴款书缴款
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
        logger.info("[1533002缴款书缴款][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "[票据种类] " + voucherType + "  [缴款书编号] " + voucherNo);
        FsQdfSysCtl sysCtl = fsqdfSysCtlService.getFsQdfSysCtl("1");
        FsQdfPaymentInfo info = fsqdfPaymentService.qryPaymentInfo(voucherType, voucherNo);
        List<FsQdfPaymentItem> items = fsqdfPaymentService.qryPaymentItems(voucherType, voucherNo);
        boolean existInDB = false;

        // 若数据库已存在该缴款单(机打票)，则记账
        if (info != null && items.size() > 0) {
            existInDB = true;
            info.setJym(fieldArray[21]);
            if (new BigDecimal(fieldArray[19]).compareTo(info.getZje()) != 0) {
                msg.rtnCode = TxnRtnCode.MSG_AMT_ERROR.getCode();
                msg.msgBody = TxnRtnCode.MSG_AMT_ERROR.getTitle().getBytes();
                return msg;
            }
            info.setJkfs(fieldArray[4]);                       // 缴款方式
            info.setFkrzh(fieldArray[14]);                     // 付款人账号
            info.setSkrmc(StringUtils.isEmpty(fieldArray[15]) ? sysCtl.getCbsActnam() : fieldArray[15]);                    // 收款人名称
            info.setSkrkhh(StringUtils.isEmpty(fieldArray[16]) ? sysCtl.getBankName() : fieldArray[16]);                    // 收款人开户行
            info.setSkrzh(StringUtils.isEmpty(fieldArray[17]) ? sysCtl.getCbsActno() : fieldArray[17]);                     // 收款人账号
            info.setLxskbz(StringUtils.isEmpty(fieldArray[22]) ? "0" : fieldArray[22]);                    // 离线收款标志
            info.setYhskrq(fieldArray[24]);
            // 更新为主机记账
            info.setYhwdbm(msg.branchID);                      // 网点号码
            info.setOperid(msg.tellerID);                      // 柜员
            info.setCbsActSerial(msg.serialNo);
            if (!StringUtils.isEmpty(fieldArray[34])) {
                info.setBmkywxh(fieldArray[34]);                   // 不明款业务序号
                info.setPendingFlag("3");                          // 不明款直接补票
                info.setHostBookFlag("0");
            }
            if ("06".equals(fieldArray[4])) {         // POS缴款方式
                info.setHostBookFlag("0");             // 主机不记账
            } else {
                info.setHostBookFlag("1");
            }
            info.setByzd1(fieldArray[35]);
            info.setByzd2(fieldArray[36]);         // pos 终端号
            info.setByzd3(fieldArray[37]);                 // pos流水号
        } else {
            // 保存并记账手工票
            info = new FsQdfPaymentInfo();
            info.setPjzl(fieldArray[0]);                       // 票据种类
            info.setJksbh(fieldArray[1]);                      // 缴款书编号
            info.setXzqh(fieldArray[2]);                       // 行政区划
            info.setZsfs(fieldArray[3]);                       // 征收方式
            info.setJkfs(fieldArray[4]);                       // 缴款方式
            info.setTzrq(fieldArray[5]);                       // 填制日期
            info.setWtzsdwbm(fieldArray[6]);                   // 委托执收单位编码
            info.setWtzsdwmc(fieldArray[7]);                   // 委托执收单位名称
            info.setWtzsbz(fieldArray[8]);                     // 委托征收标志
            info.setStzsdwbm(fieldArray[9]);                   // 受托执收单位编码
            info.setStzsdwmc(fieldArray[10]);                  // 受托执收单位名称
            info.setZsdwzzjg(fieldArray[11]);                  // 执收单位组织机构
            info.setFkrmc(fieldArray[12]);                     // 付款人名称
            info.setFkrkhh(fieldArray[13]);                     // 付款人开户行
            info.setFkrzh(fieldArray[14]);                     // 付款人账号
            info.setSkrmc(StringUtils.isEmpty(fieldArray[15]) ? sysCtl.getCbsActnam() : fieldArray[15]);                    // 收款人名称
            info.setSkrkhh(StringUtils.isEmpty(fieldArray[16]) ? sysCtl.getBankName() : fieldArray[16]);                    // 收款人开户行
            info.setSkrzh(StringUtils.isEmpty(fieldArray[17]) ? sysCtl.getCbsActno() : fieldArray[17]);                     // 收款人账号
//            info.setYhwdbm(fieldArray[18]);                     // 银行网点编号
            info.setZje(StringUtils.isEmpty(fieldArray[19]) ? null : new BigDecimal(fieldArray[19]));       // 总金额
            info.setBz(fieldArray[20]);
            info.setLxskbz(fieldArray[22]);                    // 离线收款标志
            info.setSgpbz(fieldArray[23]);                     // 手工票标志
            info.setJym(fieldArray[21]);                       // 校验码
            info.setFmyy(fieldArray[25]);                       // 罚没原因
            info.setFmly(fieldArray[26]);                       // 罚没理由
            info.setDsfy(fieldArray[27]);                       // 代收法院
            info.setBgrmc(fieldArray[28]);                       // 被告人
            info.setAy(fieldArray[29]);                       // 案由
            info.setAh(fieldArray[30]);                       // 案号
            info.setZdjh(fieldArray[31]);                       // 字第几号
            info.setBde(fieldArray[32]);                       // 标的额
            info.setXzspdtwym(fieldArray[33]);                 // 行政审批大厅唯一码
            info.setBmkywxh(fieldArray[34]);                   // 不明款业务序号
            info.setByzd1(fieldArray[35]);                     // 备用字段1
            info.setByzd2(fieldArray[36]);                 // 备用字段2
            info.setByzd3(fieldArray[37]);                 // 备用字段3
            info.setYhwdbm(msg.branchID);                      // 网点号码
            info.setOperid(msg.tellerID);                      // 柜员
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
                // 若资金性质编码为空，则查询该笔收入项目信息
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

        // 发送到财政局
        Tia3002 tia3002 = new Tia3002();
        ObjectFieldsCopier.copyFields(info, tia3002.BODY.DATA);
        for (FsQdfPaymentItem item : items) {
            Tia3002.Body.Data.Xmmx xmmx = new Tia3002.Body.Data.Xmmx();
            ObjectFieldsCopier.copyFields(item, xmmx);
            tia3002.BODY.DATA.xmmxes.add(xmmx);
        }
        Toa3002 toa3002 = (Toa3002) dataExchangeService.process(tia3002);
        if ("0".equals(toa3002.HEAD.STATUS)) {
            // 本次交易缴款成功
            info.setQdfBookFlag("1");
            info.setQdfChkFlag("0");
            info.setChkActDt(sysCtl.getTxnDate());             // 对账基准日期
            if (existInDB) {
                fsqdfPaymentService.updatePaymentInfo(info);
            } else {
                fsqdfPaymentService.insertPaymentInfoAndItems(info, items);
            }
            // 返回金额的划分情况
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
